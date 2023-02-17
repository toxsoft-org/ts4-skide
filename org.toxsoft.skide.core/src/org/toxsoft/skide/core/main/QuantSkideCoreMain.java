package org.toxsoft.skide.core.main;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.uskat.backend.memtext.MtbBackendToFile.*;
import static org.toxsoft.uskat.core.impl.ISkCoreConfigConstants.*;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.tslib.utils.progargs.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.uskat.backend.memtext.*;
import org.toxsoft.uskat.base.gui.conn.*;

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
   * The command line option to specify the workroom directory.
   */
  public static final String CMD_LINE_ARG_WORKROOM = "skide-workroom"; //$NON-NLS-1$

  /**
   * The application workroom flavor.
   */
  public static final WorkroomFlavor WORKROOM_FLAVOR = new WorkroomFlavor( SKIDE_FULL_ID, 1 );

  /**
   * The name of the USkat system content file in the project workroom.
   */
  public static final String WORKROOM_FILE_SKIDE_SYSTEM = "uskat-system.textual"; //$NON-NLS-1$

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

  /**
   * Opens default {@link ISkConnectionSupplier#defConn()} as the workroom-local textual backend.
   *
   * @param aWinContext {@link IEclipseContext} - the windows level context
   * @param aWorkroom {@link ITsWorkroom} - the workroom to store ackend file
   */
  private static void internalOpenSkConnection( IEclipseContext aWinContext, ITsWorkroom aWorkroom ) {
    ISkConnectionSupplier cs = aWinContext.get( ISkConnectionSupplier.class );
    TsInternalErrorRtException.checkNull( cs );
    TsInternalErrorRtException.checkTrue( cs.defConn().state().isOpen() );
    ITsContext args = new TsContext();
    File file = new File( aWorkroom.wsDir(), WORKROOM_FILE_SKIDE_SYSTEM );
    OPDEF_FILE_PATH.setValue( args.params(), avStr( file.getAbsolutePath() ) );
    REFDEF_BACKEND_PROVIDER.setRef( args, MtbBackendToFile.PROVIDER );
    cs.defConn().open( args );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQuant
  //

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    ISkidePluginsRegistrator pluginsRegistrator = new SkidePluginsRegistrator();
    aAppContext.set( ISkidePluginsRegistrator.class, pluginsRegistrator );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // open workroom
    if( !internalOpenWorkroom( aWinContext ) ) {
      return;
    }
    ITsWorkroom skideWorkroom = aWinContext.get( ITsWorkroom.class );
    TsInternalErrorRtException.checkNull( skideWorkroom );
    // TODO open Sk-connection
    internalOpenSkConnection( aWinContext, skideWorkroom );
    // prepare plugins
    SkidePluginsRegistrator registrator = (SkidePluginsRegistrator)aWinContext.get( ISkidePluginsRegistrator.class );
    TsInternalErrorRtException.checkNull( registrator );
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    IStridablesList<AbstractSkidePlugin> pluginsList = (IStridablesList)registrator.listRegisteredPlugins();
    for( AbstractSkidePlugin p : pluginsList ) {
      try {
        ITsWorkroomStorage wrs = skideWorkroom.getStorage( p.id() );
        p.initialize( wrs );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

}
