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
import org.toxsoft.core.tslib.bricks.geometry.impl.TsPoint;
import org.toxsoft.core.tslib.bricks.strid.idgen.IStridGenerator;
import org.toxsoft.core.tslib.bricks.strid.idgen.SimpleStridGenerator;
import org.toxsoft.core.tslib.bricks.strid.more.IdChain;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.skf.rri.lib.impl.SkRegRefInfoService;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.gui.conn.cfg.*;
import org.toxsoft.uskat.core.gui.conn.cfg.m5.IConnectionConfigM5Constants;
import org.toxsoft.uskat.core.impl.ISkCoreConfigConstants;
import org.toxsoft.uskat.core.impl.SkCoreUtils;
import org.toxsoft.uskat.s5.client.IS5ConnectionParams;
import org.toxsoft.uskat.s5.utils.threads.impl.S5Lockable;

/**
 * {@link ISkideExternalConnectionsService} implementation.
 *
 * @author hazard157
 */
public class SkideExternalConnectionsService
    implements ISkideExternalConnectionsService {

  private final IStridGenerator idGen = new SimpleStridGenerator( SimpleStridGenerator.DEFAULT_INITIAL_STATE );

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
    // установим нормальный размер диалога
    di.setMinSize( new TsPoint( -30, -40 ) );
    IConnectionConfig conConf = M5GuiUtils.askSelectItem( di, model, null, lm.itemsProvider(), lm );
    if( conConf == null ) {
      return null;
    }
    // prepare arguments
    IConnectionConfigProvider ccProvider = ccService.listProviders().findByKey( conConf.providerId() );
    if( ccProvider == null ) {
      TsDialogUtils.error( shell, FMT_ERR_UNREGISTERED_PROVIDER, conConf.id() );
      return null;
    }
    ITsContext args = new TsContext();
    ccProvider.fillArgs( args, conConf.opValues() );

    /**
     * TODO вниманию dima и MVK: выделенный внизу код неправильный! во-первых, он предназначен ТОЛЬКО для S5 соединения,
     * а здесь код SkIDE, который может соединятся любым бекендм. Во-вторых, бекенд-специфичный код настроки аргументов
     * дожен находится в классах, наследниках ConnectionConfigProvider. То есть, в реализации провайдера
     * IConnectionConfigProvider.<br>
     * При таком подходе, достаточно одной строки выше:<br>
     * ccProvider.fillArgs( args, conConf.opValues() );<br>
     * <p>
     * По-хорошему, указанный код должен находится в теле метода S5ConnectionConfigProvider.doProcessArgs().
     */
    // TODO ---

    String login = "root";
    String password = "1";
    args.params().setStr( IS5ConnectionParams.OP_USERNAME, login );
    args.params().setStr( IS5ConnectionParams.OP_PASSWORD, password );
    IS5ConnectionParams.REF_CONNECTION_LOCK.setRef( args, new S5Lockable() );
    // necessary staff for RCP
    IS5ConnectionParams.REF_CLASSLOADER.setRef( args, getClass().getClassLoader() );
    ISkCoreConfigConstants.REFDEF_THREAD_SEPARATOR.setRef( args, SwtThreadSeparatorService.CREATOR );
    Display display = aContext.get( Display.class );
    SwtThreadSeparatorService.REF_DISPLAY.setRef( args, display );

    // ---

    // 2023-12-20 mvk
    // register core api extension
    SkCoreUtils.registerSkServiceCreator( SkRegRefInfoService.CREATOR );

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
