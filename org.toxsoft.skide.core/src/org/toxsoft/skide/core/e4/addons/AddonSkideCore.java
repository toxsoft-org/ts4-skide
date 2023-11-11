package org.toxsoft.skide.core.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.users.gui.*;
import org.toxsoft.skide.core.Activator;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.uskat.core.gui.*;

/**
 * The plugin addon.
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
    aQuantRegistrator.registerQuant( new QuantSkCoreGui() );
    aQuantRegistrator.registerQuant( new QuantSkUsersGui() );
    aQuantRegistrator.registerQuant( new QuantSkide010Workroom() );

    // textual or SQLite?
    // aQuantRegistrator.registerQuant( new QuantSkide020SkConnection() ); // Textual
    aQuantRegistrator.registerQuant( new QuantSkide022SkConnection() ); // SQLite
    // ---

    aQuantRegistrator.registerQuant( new QuantSkide030Environment() );
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
