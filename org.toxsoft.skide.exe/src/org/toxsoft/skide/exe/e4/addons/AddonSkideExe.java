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
import org.toxsoft.skide.core.*;
import org.toxsoft.skide.exe.Activator;
import org.toxsoft.uskat.base.gui.*;

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

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantSkBaseGui() );
    aQuantRegistrator.registerQuant( new QuantSkideCore() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // задаем значок окна и приложения
    MApplication app = aAppContext.get( MApplication.class );
    EModelService modelService = aAppContext.get( EModelService.class );
    MTrimmedWindow mainWindow = (MTrimmedWindow)modelService.find( IMwsCoreConstants.MWSID_WINDOW_MAIN, app );
    mainWindow.setIconURI( TsIconManagerUtils.makeStdIconUriString( org.toxsoft.skide.core.Activator.PLUGIN_ID,
        ICONID_APP_ICON, IS_48X48 ) );
    // начальные размеры окна
    Display display = aAppContext.get( Display.class );
    Rectangle dBounds = display.getBounds();
    mainWindow.setX( dBounds.x + 8 );
    mainWindow.setY( 0 );
    mainWindow.setWidth( dBounds.width - 4 * 8 );
    mainWindow.setHeight( dBounds.height );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    // nop
  }

}
