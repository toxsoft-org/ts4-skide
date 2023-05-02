package org.toxsoft.skide.exe.e4.addons;

import static org.toxsoft.core.tsgui.graphics.icons.EIconSize.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.graphics.icons.impl.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.skf.users.gui.*;
import org.toxsoft.skide.core.*;
import org.toxsoft.skide.exe.Activator;
import org.toxsoft.uskat.core.gui.*;

/**
 * Application addon.
 *
 * @author hazard157
 */
public class AddonSkideExe
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkideExe() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantSkCoreGui() );
    aQuantRegistrator.registerQuant( new QuantSkUsersGui() );
    aQuantRegistrator.registerQuant( new QuantSkideCore() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // set application and window icon
    MApplication app = aAppContext.get( MApplication.class );
    EModelService modelService = aAppContext.get( EModelService.class );
    MTrimmedWindow mainWindow = (MTrimmedWindow)modelService.find( IMwsCoreConstants.MWSID_WINDOW_MAIN, app );
    mainWindow.setIconURI( TsIconManagerUtils.makeStdIconUriString( org.toxsoft.skide.core.Activator.PLUGIN_ID,
        ICONID_APP_ICON, IS_48X48 ) );

    // initial size of the window BIG
    Display display = aAppContext.get( Display.class );
    Rectangle dBounds = display.getBounds();
    mainWindow.setX( dBounds.x + 8 );
    mainWindow.setY( 0 );
    mainWindow.setWidth( dBounds.width - 4 * 8 );
    mainWindow.setHeight( dBounds.height );

    // initial size of the window SMALL for DEBUG
    // Display display = aAppContext.get( Display.class );
    // Rectangle dBounds = display.getBounds();
    // int dx = dBounds.width / 8;
    // int dy = dBounds.height / 8;
    // mainWindow.setX( 4 * dx );
    // mainWindow.setY( 2 * dy );
    // mainWindow.setWidth( 3 * dx );
    // mainWindow.setHeight( 5 * dy );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    Display d = aWinContext.get( Display.class );
    ITsE4Helper e4Helper = aWinContext.get( ITsE4Helper.class );
    d.asyncExec( () -> e4Helper.switchToPerspective( PERPSID_SKIDE_PROJECT, null ) );
  }

}
