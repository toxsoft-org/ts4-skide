package org.toxsoft.skide.core.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.skide.core.env.*;

/**
 * Manage SkIDE main windows title.
 * <p>
 * Title will look like "workroom_dir - SkIDE project name - SkIDE"
 *
 * @author hazard157
 */
public class AddonSkideCoreWindowsTitle
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkideCoreWindowsTitle() {
    super( AddonSkideCoreWindowsTitle.class.getSimpleName() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void updateWindowTitle( IEclipseContext aWinContext ) {
    ITsWorkroom workroom = aWinContext.get( ITsWorkroom.class );
    String wrName = workroom.wsDir().getName();
    ISkideEnvironment skideEnv = aWinContext.get( ISkideEnvironment.class );
    String projName = skideEnv.projectProperties().name();
    IMwsOsgiService mwsService = getOsgiService( IMwsOsgiService.class );
    String appName = mwsService.appInfo().nmName();
    String title = String.format( "%s - %s - %s", //$NON-NLS-1$
        wrName, projName, appName );
    MWindow window = aWinContext.get( MWindow.class );
    window.setLabel( title );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    updateWindowTitle( aWinContext );
    ISkideEnvironment skideEnv = aWinContext.get( ISkideEnvironment.class );
    skideEnv.projectProperties().genericChangeEventer().addListener( s -> updateWindowTitle( aWinContext ) );
  }

}
