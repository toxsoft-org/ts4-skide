package org.toxsoft.tool.sfv.mws.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.tool.sfv.gui.*;

/**
 * Module addon.
 *
 * @author hazard157
 */
public class AddonToolSfvMws
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonToolSfvMws() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantToolSfvGui() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    // nop
  }

}
