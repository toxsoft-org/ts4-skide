package org.toxsoft.skide.core.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.skf.users.gui.*;
import org.toxsoft.skide.core.*;
import org.toxsoft.skide.core.Activator;
import org.toxsoft.skide.core.env.*;
import org.toxsoft.skide.core.incub.*;
import org.toxsoft.uskat.core.gui.*;
import org.toxsoft.uskat.core.gui.conn.cfg.m5.*;

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
    aQuantRegistrator.registerQuant( new QuantIncub() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkideCoreConstants.init( aWinContext );
    //
    SkideEnvironment skideEnv = (SkideEnvironment)aWinContext.get( ISkideEnvironment.class );
    skideEnv.initWin( new TsGuiContext( aWinContext ) );
    //
    ValedControlFactoriesRegistry vcfRegistry = aWinContext.get( ValedControlFactoriesRegistry.class );
    vcfRegistry.registerFactory( ValedProviderIdCombo.FACTORY );
    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new SkidePluginM5Model() );
  }

}
