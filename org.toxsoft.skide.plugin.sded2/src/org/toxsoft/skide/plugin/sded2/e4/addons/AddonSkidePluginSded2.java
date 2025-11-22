package org.toxsoft.skide.plugin.sded2.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.plugin.sded2.*;
import org.toxsoft.skide.plugin.sded2.main.*;

/**
 * Plugin addon.
 *
 * @author hazard157
 */
public class AddonSkidePluginSded2
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkidePluginSded2() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ISkideEnvironment skEnv = aAppContext.get( ISkideEnvironment.class );
    skEnv.pluginsRegistrator().registerPlugin( SkidePluginSded.INSTANCE );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkidePluginSded2Constants.init( aWinContext );
    //
  }

}
