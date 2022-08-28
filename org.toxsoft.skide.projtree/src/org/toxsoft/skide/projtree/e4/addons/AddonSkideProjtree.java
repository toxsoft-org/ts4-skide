package org.toxsoft.skide.projtree.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skide.projtree.*;

/**
 * Module addon.
 *
 * @author hazard157
 */
public class AddonSkideProjtree
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkideProjtree() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkideProjtreeConstants.init( aWinContext );
  }

}
