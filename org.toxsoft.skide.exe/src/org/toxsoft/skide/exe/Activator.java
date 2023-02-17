package org.toxsoft.skide.exe;

import static org.toxsoft.skide.exe.ISkideExeConstants.*;

import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.osgi.*;

/**
 * The plugin activator.
 *
 * @author hazard157
 */
public class Activator
    extends MwsActivator {

  /**
   * The plugin ID (for Java static imports).
   */
  public static final String PLUGIN_ID = "org.toxsoft.skide.exe"; //$NON-NLS-1$

  private static Activator instance = null;

  /**
   * Constructor.
   */
  public Activator() {
    super( PLUGIN_ID );
    checkInstance( instance );
    instance = this;
  }

  @Override
  protected void doStart() {
    IMwsOsgiService mws = findOsgiService( IMwsOsgiService.class );
    mws.setAppInfo( APP_INFO );
  }

  /**
   * Returns the reference to the activator singleton.
   *
   * @return {@link Activator} - the activator singleton
   */
  public static Activator getInstance() {
    return instance;
  }

}
