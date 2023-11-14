package org.toxsoft.skide.core.api.impl;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.tslib.utils.progargs.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.skide.core.api.*;

/**
 * SkIDE workroom initialization quant.
 * <p>
 * Reference to the open and initialized {@link ITsWorkroom} is put in the windows level context.
 * <p>
 * Note<b>:</b> This quant should be registered as a first quant in the application launcher.
 *
 * @author hazard157
 */
public class QuantSkide010Workroom
    extends AbstractQuant {

  // ------------------------------------------------------------------------------------
  // Workroom info

  /**
   * The command line option to specify the workroom directory.
   */
  public static final String CMD_LINE_ARG_WORKROOM = "skide-workroom"; //$NON-NLS-1$

  /**
   * The application workroom flavor.
   */
  public static final WorkroomFlavor WORKROOM_FLAVOR = new WorkroomFlavor( SKIDE_FULL_ID, 4 );

  /**
   * Constructor.
   */
  public QuantSkide010Workroom() {
    super( QuantSkide010Workroom.class.getSimpleName() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Opens the workroom specified in the command line or selected by the user.
   *
   * @param aAppContext {@link IEclipseContext} - the application level context
   */
  private static void internalOpenWorkroom( IEclipseContext aAppContext ) {
    Display display = aAppContext.get( Display.class );
    TsInternalErrorRtException.checkNull( display );
    // initialize workroom, by default in program startup directory
    ProgramArgs pa = aAppContext.get( ProgramArgs.class );
    // String wrPath = pa.getArgValue( CMD_LINE_ARG_WORKROOM, "workroom" ); //$NON-NLS-1$
    String wrPath = pa.getArgValue( CMD_LINE_ARG_WORKROOM, TsLibUtils.EMPTY_STRING );
    File wrDir;
    if( wrPath.isEmpty() ) {
      // ask user for SkIDE directory if not specified in command line
      Shell shell = new Shell( display );
      wrDir = TsRcpDialogUtils.askDirOpen( shell, TsLibUtils.EMPTY_STRING );
      if( wrDir == null ) {
        System.exit( 1 );
        return;
      }
    }
    else {
      wrDir = new File( wrPath );
    }
    try {
      ITsWorkroom wroom = TsWorkroom.openWorkroom( wrDir, WORKROOM_FLAVOR );
      aAppContext.set( ITsWorkroom.class, wroom );
    }
    catch( Exception ex ) {
      Shell shell = new Shell( display );
      TsDialogUtils.error( shell, ex );
      LoggerUtils.errorLogger().error( ex );
      System.exit( 1 );
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Update main windows title reflecting project properties and workroom name.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  void updateWindowsTitle( IEclipseContext aWinContext ) {
    ITsWorkroom wr = aWinContext.get( ITsWorkroom.class );
    ISkideProjectProperties spp = aWinContext.get( ISkideEnvironment.class ).projectProperties();
    String title = String.format( "%s (%s) - %s", spp.projAlias(), wr.wrDir().getName(), APP_INFO.nmName() ); //$NON-NLS-1$
    MWindow window = aWinContext.get( MWindow.class );
    window.setLabel( title );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQuant
  //

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    internalOpenWorkroom( aAppContext );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ISkideEnvironment skEnv = aWinContext.get( ISkideEnvironment.class );
    skEnv.projectProperties().genericChangeEventer().addListener( s -> updateWindowsTitle( aWinContext ) );
    //
    updateWindowsTitle( aWinContext );
  }

}
