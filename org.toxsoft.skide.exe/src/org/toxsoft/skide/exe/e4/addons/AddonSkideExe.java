package org.toxsoft.skide.exe.e4.addons;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.tsgui.graphics.icons.EIconSize.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.toxsoft.skide.core.QuantSkideCore;
import org.toxsoft.skide.exe.Activator;
import org.toxsoft.tsgui.bricks.quant.IQuantRegistrator;
import org.toxsoft.tsgui.graphics.icons.impl.TsIconManagerUtils;
import org.toxsoft.tsgui.mws.IMwsCoreConstants;
import org.toxsoft.tsgui.mws.bases.MwsAbstractAddon;

/**
 * Alloication addon.
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
