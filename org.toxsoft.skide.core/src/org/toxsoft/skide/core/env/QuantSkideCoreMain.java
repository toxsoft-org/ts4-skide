package org.toxsoft.skide.core.env;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.env.ISkResources.*;
import static org.toxsoft.uskat.backend.memtext.MtbBackendToFile.*;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.tslib.utils.progargs.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.uskat.backend.memtext.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.base.gui.conn.cfg.*;
import org.toxsoft.uskat.s5.client.remote.*;

/**
 * SkIDE core initialization quant.
 * <p>
 * Quant is placed in this package to hide the visibility of the implementation classes.
 *
 * @author hazard157
 */
public class QuantSkideCoreMain
    extends AbstractQuant {

  /**
   * Do NOT store SkIDE built-in connection in IConnectionConfigService.<br>
   * Remove IConnectionConfigService validator of FIXED connection<br>
   */

  /**
   * The command line option to specify the workroom directory.
   */
  public static final String CMD_LINE_ARG_WORKROOM = "skide-workroom"; //$NON-NLS-1$

  /**
   * The application workroom flavor.
   */
  public static final WorkroomFlavor WORKROOM_FLAVOR = new WorkroomFlavor( SKIDE_FULL_ID, 1 );

  /**
   * Connection configuration ID to the built into the SkIDE workroom storage.
   * <p>
   * Configuration with this ID is the fixed configuration.
   */
  public static final String SKIDE_LOCAL_CONN_ID = SKIDE_ID + ".skconn.local"; //$NON-NLS-1$

  /**
   * The file name the built into the SkIDE workroom storage.
   * <p>
   * File is placer in workroom root.
   */
  public static final String WORKROOM_FILE_SKIDE_SYSTEM = "uskat-system.textual"; //$NON-NLS-1$

  /**
   * Option ID storing list of configurations {@link IConnectionConfig} in workroom storage.
   * <p>
   * {@link ITsWorkroom#getApplicationStorage()} is used. For options read/write methods
   * {@link IKeepablesStorage#readColl(String, IEntityKeeper)} /
   * {@link IKeepablesStorage#writeColl(String, ITsCollection, IEntityKeeper)}.
   */
  public static final String OPID_CONNECTION_CONFIGS = "SkideMainConnectionConfigs"; //$NON-NLS-1$

  /**
   * Option for {@link IConnectionConfig#params()}: the fixed connection flag.
   * <p>
   * Configuration of the fixed connections can not be edited.
   */
  public static final IDataDef OPDEF_FIXED_CONNECTION = DataDef.create( SKIDE_ID + ".Fixed", BOOLEAN, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * The validator to disable editing of fixed connections.
   * <p>
   * Fixed connections are connections with the option {@link #OPDEF_FIXED_CONNECTION} set in
   * {@link IConnectionConfig#params()}.
   */
  public static final IConnectionConfigServiceValidator CCSV_VALIDATOR = new IConnectionConfigServiceValidator() {

    @Override
    public ValidationResult canAddConfig( IConnectionConfigService aSource, IConnectionConfig aCfg ) {
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canReplaceConfig( IConnectionConfigService aSource, IConnectionConfig aCfg,
        IConnectionConfig aOldCfg ) {
      if( OPDEF_FIXED_CONNECTION.getValue( aOldCfg.params() ).asBool() ) {
        return ValidationResult.error( FMT_ERR_CANT_EDIT_FIXED_CONN, aOldCfg.nmName() );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canRemoveConfig( IConnectionConfigService aSource, String aCfgId ) {
      IConnectionConfig cc = aSource.listConfigs().findByKey( aCfgId );
      if( cc == null ) {
        return ValidationResult.warn( FMT_WARN_NO_SUCH_CONN, aCfgId );
      }
      if( OPDEF_FIXED_CONNECTION.getValue( cc.params() ).asBool() ) {
        return ValidationResult.error( FMT_ERR_CANT_REMOVE_FIXED_CONN, cc.nmName() );
      }
      return ValidationResult.SUCCESS;
    }

  };

  /**
   * Constructor.
   */
  public QuantSkideCoreMain() {
    super( QuantSkideCoreMain.class.getSimpleName() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Opens the workroom specified in the command line or selected byb the user.
   *
   * @param aWinContext {@link IEclipseContext} - the windows level context
   * @return boolean - the flag that workroom was actually opened
   */
  private static boolean internalOpenWorkroom( IEclipseContext aWinContext ) {
    Shell shell = aWinContext.get( Shell.class );
    Display display = aWinContext.get( Display.class );
    ITsE4Helper e4Helper = aWinContext.get( ITsE4Helper.class );
    MTrimmedWindow window = aWinContext.get( MTrimmedWindow.class );
    // initialize workroom, by default in program startup directory
    ProgramArgs pa = aWinContext.get( ProgramArgs.class );
    // String wrPath = pa.getArgValue( CMD_LINE_ARG_WORKROOM, "workroom" ); //$NON-NLS-1$
    String wrPath = pa.getArgValue( CMD_LINE_ARG_WORKROOM, TsLibUtils.EMPTY_STRING );
    File wrDir;
    if( wrPath.isEmpty() ) {
      // ask user for SkIDE directory if not specified in command line
      wrDir = TsRcpDialogUtils.askDirOpen( shell, TsLibUtils.EMPTY_STRING );
      if( wrDir == null ) {
        window.setVisible( false );
        display.asyncExec( () -> e4Helper.quitApplication() );
        return false;
      }
    }
    else {
      wrDir = new File( wrPath );
    }
    try {
      ITsWorkroom wroom = TsWorkroom.openWorkroom( wrDir, WORKROOM_FLAVOR );
      aWinContext.set( ITsWorkroom.class, wroom );
      return true;
    }
    catch( Exception ex ) {
      TsDialogUtils.error( shell, ex );
      LoggerUtils.errorLogger().error( ex );
      display.asyncExec( () -> e4Helper.quitApplication() );
      return false;
    }
  }

  private static void internalInitConnCfgService( IEclipseContext aAppContext ) {
    ConnectionConfigService ccs = (ConnectionConfigService)aAppContext.get( IConnectionConfigService.class );
    // load connection configuration settings
    ITsWorkroom skideWorkroom = aAppContext.get( ITsWorkroom.class );
    IKeepablesStorage ks = skideWorkroom.getApplicationStorage().ktorStorage();
    IList<IConnectionConfig> ll = ks.readColl( OPID_CONNECTION_CONFIGS, ConnectionConfig.KEEPER );
    ccs.setConfigsList( new StridablesList<>( ll ) );
    // add listener to save configurations on change
    ccs.eventer().addListener( new IConnectionConfigServiceListener() {

      @Override
      public void onProvidersListChanged( IConnectionConfigService aSource ) {
        // nop
      }

      @Override
      public void onConfigsListChanged( IConnectionConfigService aSource ) {
        ks.writeColl( OPID_CONNECTION_CONFIGS, ccs.listConfigs(), ConnectionConfig.KEEPER );
      }

    } );
    // register providers
    ccs.registerPovider( new ConnectionConfigProvider( MtbBackendToFile.PROVIDER, IOptionSet.NULL ) );
    ccs.registerPovider( new ConnectionConfigProvider( new S5RemoteBackendProvider(), IOptionSet.NULL ) );
    // add validator to manage fixed connections
    ccs.svs().addValidator( CCSV_VALIDATOR );
    // add fixed connection to the local system storage on first run
    if( !ccs.listConfigs().hasKey( SKIDE_LOCAL_CONN_ID ) ) {
      File localSkFile = new File( skideWorkroom.wrDir(), WORKROOM_FILE_SKIDE_SYSTEM );
      IConnectionConfig localCfg = new ConnectionConfig( SKIDE_LOCAL_CONN_ID, //
          MtbBackendToFile.PROVIDER.getMetaInfo().id(), //
          OptionSetUtils.createOpSet( //
              TSID_NAME, STR_N_LOCAL_CONN, //
              TSID_DESCRIPTION, STR_D_LOCAL_CONN, //
              TSID_ICON_ID, ICONID_SKIDE_LOCAL_CONN, //
              OPDEF_FIXED_CONNECTION, AV_TRUE //
          ), //
          OptionSetUtils.createOpSet( //
              OPDEF_FILE_PATH, avStr( localSkFile.getAbsolutePath() ) //
          ) );
      ccs.defineConfig( localCfg );
    }
  }

  /**
   * Opens default {@link ISkConnectionSupplier#defConn()} as the workroom-local textual backend.
   *
   * @param aWinContext {@link IEclipseContext} - the windows level context
   * @param aWorkroom {@link ITsWorkroom} - the workroom to store ackend file
   */
  private static void internalOpenSkConnection( IEclipseContext aWinContext, ITsWorkroom aWorkroom ) {

    /**
     * TODO create application (or project?) preference "Last used Sk-connection"<br>
     * TODO respect preference and open last connection
     */

    ISkConnectionSupplier connSupplier = aWinContext.get( ISkConnectionSupplier.class );
    TsInternalErrorRtException.checkNull( connSupplier );
    TsInternalErrorRtException.checkTrue( connSupplier.defConn().state().isOpen() );
    IConnectionConfigService ccService = aWinContext.get( IConnectionConfigService.class );
    TsInternalErrorRtException.checkNull( ccService );
    IConnectionConfig cfg = ccService.listConfigs().getByKey( SKIDE_LOCAL_CONN_ID );
    IConnectionConfigProvider ccProvider = ccService.listProviders().getByKey( cfg.providerId() );
    ITsContext args = new TsContext();
    ccProvider.fillArgs( args, cfg.opValues() );
    connSupplier.defConn().open( args );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQuant
  //

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    ConnectionConfigService ccs = new ConnectionConfigService();
    aAppContext.set( IConnectionConfigService.class, ccs );
    ISkideEnvironment skideEnv = new SkideEnvironment();
    aAppContext.set( ISkideEnvironment.class, skideEnv );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // open workroom
    if( !internalOpenWorkroom( aWinContext ) ) {
      return;
    }
    ITsWorkroom skideWorkroom = aWinContext.get( ITsWorkroom.class );
    TsInternalErrorRtException.checkNull( skideWorkroom );
    // conn cfgs
    internalInitConnCfgService( aWinContext );
    // open Sk-connection
    internalOpenSkConnection( aWinContext, skideWorkroom );
    // prepare plugins
    ISkideEnvironment skideEnv = aWinContext.get( ISkideEnvironment.class );
    SkidePluginsRegistrator registrator = (SkidePluginsRegistrator)skideEnv.pluginsRegistrator();
    TsInternalErrorRtException.checkNull( registrator );
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    IStridablesList<AbstractSkidePlugin> pluginsList = (IStridablesList)registrator.listRegisteredPlugins();
    ITsGuiContext ctx = new TsGuiContext( aWinContext );
    for( AbstractSkidePlugin p : pluginsList ) {
      try {
        ITsWorkroomStorage wrs = skideWorkroom.getStorage( p.id() );
        p.initialize( ctx, wrs );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

}
