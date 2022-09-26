package org.toxsoft.skide.core.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skide.core.*;
import org.toxsoft.skide.core.Activator;
import org.toxsoft.skide.core.main.*;
import org.toxsoft.skide.core.main.impl.*;
import org.toxsoft.uskat.base.gui.*;

/**
 * Plugin adoon.
 *
 * @author hazard157
 */
public class AddonSkideCore
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkideCore() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantSkBaseGui() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ISkideEnvironment skideEnv = new SkideEnvironment();
    aAppContext.set( ISkideEnvironment.class, skideEnv );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkideCoreConstants.init( aWinContext );
  }

}
