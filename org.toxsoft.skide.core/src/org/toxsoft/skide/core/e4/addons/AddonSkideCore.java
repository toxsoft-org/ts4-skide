package org.toxsoft.skide.core.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skide.core.*;
import org.toxsoft.skide.core.Activator;
import org.toxsoft.skide.core.main.*;
import org.toxsoft.uskat.base.gui.*;
import org.toxsoft.uskat.users.gui.*;

/**
 * Plugin addon.
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
    aQuantRegistrator.registerQuant( new QuantSkUsersGui() );
    aQuantRegistrator.registerQuant( new QuantSkideCoreMain() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkideCoreConstants.init( aWinContext );
    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new SkidePluginM5Model() );
  }

}
