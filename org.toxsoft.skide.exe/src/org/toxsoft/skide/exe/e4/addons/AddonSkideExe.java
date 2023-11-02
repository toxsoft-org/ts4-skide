package org.toxsoft.skide.exe.e4.addons;

import static org.toxsoft.core.tsgui.graphics.icons.EIconSize.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.graphics.icons.impl.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tsgui.rcp.*;
import org.toxsoft.skide.exe.Activator;

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
    aQuantRegistrator.registerQuant( new QuantTsGuiRcp() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // set application and window icon
    MApplication app = aAppContext.get( MApplication.class );
    EModelService modelService = aAppContext.get( EModelService.class );
    MTrimmedWindow mainWindow = (MTrimmedWindow)modelService.find( IMwsCoreConstants.MWSID_WINDOW_MAIN, app );
    mainWindow.setIconURI( TsIconManagerUtils.makeStdIconUriString( org.toxsoft.skide.core.Activator.PLUGIN_ID,
        ICONID_APP_ICON, IS_48X48 ) );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    Display d = aWinContext.get( Display.class );
    ITsE4Helper e4Helper = aWinContext.get( ITsE4Helper.class );
    d.asyncExec( () -> e4Helper.switchToPerspective( PERPSID_SKIDE_PROJECT, null ) );
  }

}
