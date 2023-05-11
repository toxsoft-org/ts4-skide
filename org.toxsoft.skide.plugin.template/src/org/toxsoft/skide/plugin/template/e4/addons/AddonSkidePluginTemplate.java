package org.toxsoft.skide.plugin.template.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.plugin.template.*;
import org.toxsoft.skide.plugin.template.main.*;

/**
 * Plugin addon.
 *
 * @author hazard157
 */
public class AddonSkidePluginTemplate
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkidePluginTemplate() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ISkideEnvironment skEnv = aAppContext.get( ISkideEnvironment.class );
    skEnv.pluginsRegistrator().registerPlugin( SkidePluginTemplate.INSTANCE );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkidePluginTemplateConstants.init( aWinContext );
    //
  }

}
