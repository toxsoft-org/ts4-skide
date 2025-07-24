package org.toxsoft.skide.plugin.exconn.service;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnSharedResources.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.idgen.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.threadexec.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.login.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.backend.s5.gui.utils.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.conn.cfg.*;
import org.toxsoft.uskat.core.gui.conn.m5.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.s5.client.*;
import org.toxsoft.uskat.s5.client.remote.*;

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
  public String selectConfig( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    // prepare M5-based config selection dialog
    IConnectionConfigService ccService = aContext.get( IConnectionConfigService.class );
    IM5Domain m5 = aContext.get( IM5Domain.class );
    IM5Model<IConnectionConfig> model =
        m5.getModel( IConnectionConfigM5Constants.MID_SK_CONN_CFG, IConnectionConfig.class );
    IM5LifecycleManager<IConnectionConfig> lm = model.getLifecycleManager( ccService );
    TsDialogInfo di = new TsDialogInfo( aContext, DLG_SELECT_CFG_AND_OPEN, DLG_SELECT_CFG_AND_OPEN_D );
    // invoke dialog
    di.setMinSize( new TsPoint( -30, -40 ) );
    IConnectionConfig conConf = M5GuiUtils.askSelectItem( di, model, null, lm.itemsProvider(), lm );
    if( conConf == null ) {
      return null;
    }
    return conConf.id();
  }

  @Override
  public IdChain openConnection( String aConfigId, ITsGuiContext aContext, ILoginInfo aLoginInfo ) {
    TsNullArgumentRtException.checkNulls( aConfigId, aContext );
    // get connection config by the specified ID
    Shell shell = aContext.get( Shell.class );
    IConnectionConfigService ccService = aContext.get( IConnectionConfigService.class );
    IConnectionConfig conConf = ccService.listConfigs().findByKey( aConfigId );
    if( conConf == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_SUCH_CONN_CONF_ID, aConfigId );
    }
    // fill arguments with the GUI-specific references
    ITsContext args = new TsContext();
    Display display = aContext.get( Display.class );
    ISkCoreConfigConstants.REFDEF_THREAD_EXECUTOR.setRef( args, new SkGuiThreadExecutor( display ) );
    // fill opening arguments with the connection kind specific options and references
    IConnectionConfigProvider ccProvider = ccService.listProviders().findByKey( conConf.providerId() );
    if( ccProvider == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_UNREGISTERED_PROVIDER, conConf.id() );
    }

    // fill LoginInfo even (this is harmless operation for connection that does not need login)
    ISkConnectionConstants.ARGDEF_LOGIN.setValue( args.params(), avStr( aLoginInfo.login() ) );
    ISkConnectionConstants.ARGDEF_PASSWORD.setValue( args.params(), avStr( aLoginInfo.password() ) );
    ISkConnectionConstants.ARGDEF_ROLE.setValue( args.params(), avStr( aLoginInfo.role() ) );

    // FIXME temporary code
    IS5ConnectionParams.OP_USERNAME.setValue( args.params(), avStr( aLoginInfo.login() ) );
    IS5ConnectionParams.OP_PASSWORD.setValue( args.params(), avStr( aLoginInfo.password() ) );
    // 2025-05-12 mvk---+++
    // dima 22.01.25 current role was not root
    // Skid rootRole = ISkUserServiceHardConstants.SKID_ROLE_ROOT;
    // IS5ConnectionParams.OP_ROLE.setValue( args.params(), avValobj( rootRole ) );
    String role = ISkUserServiceHardConstants.ROLE_ID_ROOT;
    IS5ConnectionParams.OP_ROLE.setValue( args.params(), avStr( role ) );

    TsTestUtils.pl( " ==== LoginInfo = %s", aLoginInfo.toString() );
    // ---

    ccProvider.fillArgs( args, conConf.opValues() );

    // create connection
    ISkConnectionSupplier conSup = aContext.get( ISkConnectionSupplier.class );
    IdChain connId = new IdChain( conConf.id(), idGen.nextId() );
    ISkConnection skConn = conSup.createConnection( connId, aContext );
    // open connection
    try {
      // dima 24.07.25
      // Отображение диалога прогресса подключения к серверу
      ITsThreadExecutor threadExecutor = new SkGuiThreadExecutor( display );
      ISkCoreConfigConstants.REFDEF_THREAD_EXECUTOR.setRef( args, threadExecutor );
      ISkCoreConfigConstants.REFDEF_BACKEND_PROVIDER.setRef( args, new S5RemoteBackendProvider() );
      S5BackendGuiUtils.showConnProgressDialog( shell, skConn, args, MSG_WAIT_CONNECT, 10000 );
      // skConn.open( args );
      return connId;
    }
    catch( Exception ex ) {
      TsDialogUtils.error( shell, ex );
      conSup.removeConnection( connId );
      LoggerUtils.errorLogger().error( ex );
      return null;
    }
  }

  // @Override
  // public IdChain selectConfigAndOpenConnection1( ITsGuiContext aContext ) {
  // TsNullArgumentRtException.checkNull( aContext );
  // Shell shell = aContext.get( Shell.class );
  // // select the connection
  // IConnectionConfigService ccService = aContext.get( IConnectionConfigService.class );
  // IM5Domain m5 = aContext.get( IM5Domain.class );
  // IM5Model<IConnectionConfig> model =
  // m5.getModel( IConnectionConfigM5Constants.MID_SK_CONN_CFG, IConnectionConfig.class );
  // IM5LifecycleManager<IConnectionConfig> lm = model.getLifecycleManager( ccService );
  // TsDialogInfo di = new TsDialogInfo( aContext, DLG_SELECT_CFG_AND_OPEN, DLG_SELECT_CFG_AND_OPEN_D );
  // // установим нормальный размер диалога
  // di.setMinSize( new TsPoint( -30, -40 ) );
  // IConnectionConfig conConf = M5GuiUtils.askSelectItem( di, model, null, lm.itemsProvider(), lm );
  // if( conConf == null ) {
  // return null;
  // }
  // // prepare arguments
  // IConnectionConfigProvider ccProvider = ccService.listProviders().findByKey( conConf.providerId() );
  // if( ccProvider == null ) {
  // TsDialogUtils.error( shell, FMT_ERR_UNREGISTERED_PROVIDER, conConf.id() );
  // return null;
  // }
  // ITsContext args = new TsContext();
  // ccProvider.fillArgs( args, conConf.opValues() );
  //
  // /**
  // * TODO вниманию dima и MVK: выделенный внизу код неправильный! во-первых, он предназначен ТОЛЬКО для S5 соединения,
  // * а здесь код SkIDE, который может соединятся любым бекендм. Во-вторых, бекенд-специфичный код настроки аргументов
  // * дожен находится в классах, наследниках ConnectionConfigProvider. То есть, в реализации провайдера
  // * IConnectionConfigProvider.<br>
  // * При таком подходе, достаточно одной строки выше:<br>
  // * ccProvider.fillArgs( args, conConf.opValues() );<br>
  // * <p>
  // * По-хорошему, указанный код должен находится в теле метода S5ConnectionConfigProvider.doProcessArgs().
  // */
  // // TODO ---
  //
  // String login = "root";
  // String password = "1";
  // args.params().setStr( IS5ConnectionParams.OP_USERNAME, login );
  // args.params().setStr( IS5ConnectionParams.OP_PASSWORD, password );
  // IS5ConnectionParams.REF_CONNECTION_LOCK.setRef( args, new S5Lockable() );
  // // necessary staff for RCP
  // IS5ConnectionParams.REF_CLASSLOADER.setRef( args, getClass().getClassLoader() );
  // ISkCoreConfigConstants.REFDEF_THREAD_SEPARATOR.setRef( args, SwtThreadSeparatorService.CREATOR );
  // Display display = aContext.get( Display.class );
  // SwtThreadSeparatorService.REF_DISPLAY.setRef( args, display );
  //
  // // create connection
  // ISkConnectionSupplier conSup = aContext.get( ISkConnectionSupplier.class );
  // IdChain connId = new IdChain( conConf.id(), idGen.nextId() );
  // ISkConnection skConn = conSup.createConnection( connId, aContext );
  // // open connection
  // try {
  // // TODO invoke connection progress dialog
  // skConn.open( args );
  // return connId;
  // }
  // catch( Exception ex ) {
  // TsDialogUtils.error( shell, ex );
  // conSup.removeConnection( connId );
  // LoggerUtils.errorLogger().error( ex );
  // return null;
  // }
  // }

}
