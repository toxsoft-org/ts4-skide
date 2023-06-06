package org.toxsoft.skide.plugin.exconn.service;

import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnSharedResources.*;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tsgui.dialogs.datarec.TsDialogInfo;
import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tsgui.m5.IM5Model;
import org.toxsoft.core.tsgui.m5.gui.M5GuiUtils;
import org.toxsoft.core.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.core.tslib.bricks.ctx.ITsContext;
import org.toxsoft.core.tslib.bricks.ctx.impl.TsContext;
import org.toxsoft.core.tslib.bricks.strid.idgen.IStridGenerator;
import org.toxsoft.core.tslib.bricks.strid.idgen.SimpleStridGenaretor;
import org.toxsoft.core.tslib.bricks.strid.more.IdChain;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.gui.conn.cfg.*;
import org.toxsoft.uskat.core.gui.conn.cfg.m5.IConnectionConfigM5Constants;
import org.toxsoft.uskat.core.impl.ISkCoreConfigConstants;
import org.toxsoft.uskat.s5.client.IS5ConnectionParams;
import org.toxsoft.uskat.s5.utils.threads.impl.S5Lockable;

/**
 * {@link ISkideExternalConnectionsService} implementation.
 *
 * @author hazard157
 */
public class SkideExternalConnectionsService
    implements ISkideExternalConnectionsService {

  private final IStridGenerator idGen = new SimpleStridGenaretor( SimpleStridGenaretor.DEFAULT_INITIAL_STATE );

  /**
   * Constructor.
   */
  public SkideExternalConnectionsService() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ISkideExternalConnectionsService
  //

  @Override
  public IdChain selectConfigAndOpenConnection( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    Shell shell = aContext.get( Shell.class );
    // select the connection
    IConnectionConfigService ccService = aContext.get( IConnectionConfigService.class );
    IM5Domain m5 = aContext.get( IM5Domain.class );
    IM5Model<IConnectionConfig> model =
        m5.getModel( IConnectionConfigM5Constants.MID_SK_CONN_CFG, IConnectionConfig.class );
    IM5LifecycleManager<IConnectionConfig> lm = model.getLifecycleManager( ccService );
    TsDialogInfo di = new TsDialogInfo( aContext, DLG_SELECT_CFG_AND_OPEN, DLG_SELECT_CFG_AND_OPEN_D );
    IConnectionConfig conConf = M5GuiUtils.askSelectItem( di, model, null, lm.itemsProvider(), lm );
    if( conConf == null ) {
      return null;
    }
    // prepare arguments
    // dima 15.05 fix
    // IConnectionConfigProvider ccProvider = ccService.listProviders().findByKey( conConf.id() );
    IConnectionConfigProvider ccProvider = ccService.listProviders().findByKey( conConf.providerId() );
    if( ccProvider == null ) {
      TsDialogUtils.error( shell, FMT_ERR_UNREGISTERED_PROVIDER, conConf.id() );
      return null;
    }
    ITsContext args = new TsContext();
    ccProvider.fillArgs( args, conConf.opValues() );
    // dima 15.05 fix
    args.params().setStr( IS5ConnectionParams.OP_USERNAME, "root" );
    IS5ConnectionParams.REF_CONNECTION_LOCK.setRef( args, new S5Lockable() );
    // 2022-10-25 mvk обязательно для RCP
    IS5ConnectionParams.REF_CLASSLOADER.setRef( args, getClass().getClassLoader() );
    ISkCoreConfigConstants.REFDEF_THREAD_SEPARATOR.setRef( args, SwtThreadSeparatorService.CREATOR );
    SwtThreadSeparatorService.REF_DISPLAY.setRef( args, Display.getCurrent() );
    String login = "root"; //$NON-NLS-1$
    String password = "1"; //$NON-NLS-1$
    args.params().setStr( IS5ConnectionParams.OP_USERNAME, login );
    args.params().setStr( IS5ConnectionParams.OP_PASSWORD, password );

    // create connection
    ISkConnectionSupplier conSup = aContext.get( ISkConnectionSupplier.class );
    IdChain connId = new IdChain( conConf.id(), idGen.nextId() );
    ISkConnection skConn = conSup.createConnection( connId, aContext );
    // open connection
    try {
      // TODO invoke connection progress dialog
      skConn.open( args );
      return connId;
    }
    catch( Exception ex ) {
      TsDialogUtils.error( shell, ex );
      conSup.removeConnection( connId );
      LoggerUtils.errorLogger().error( ex );
      return null;
    }
  }

}
