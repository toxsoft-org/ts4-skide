package org.toxsoft.skide.plugin.sded.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skide.core.main.*;
import org.toxsoft.skide.plugin.sded.*;
import org.toxsoft.uskat.sded.gui.*;
import org.toxsoft.uskat.sded.gui.Activator;

/**
 * Plugin addon.
 *
 * @author hazard157
 */
public class AddonSkideSded
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkideSded() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantSkSdedGui() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ISkidePluginsRegistrator skideRegistrator = aAppContext.get( ISkidePluginsRegistrator.class );
    skideRegistrator.registerPlugin( SkidePlugin.INSTANCE );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    // nop
  }

}
