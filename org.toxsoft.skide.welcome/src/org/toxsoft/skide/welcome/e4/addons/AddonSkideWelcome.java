package org.toxsoft.skide.welcome.e4.addons;

import static org.toxsoft.skide.welcome.ISkideWelcomeConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.bound.*;
import org.toxsoft.skide.welcome.*;

/**
 * Module addon.
 *
 * @author hazard157
 */
public class AddonSkideWelcome
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkideWelcome() {
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
    // switch to welcome perspective if no project was loaded
    ITsProjectFileBound pfBound = aWinContext.get( ITsProjectFileBound.class );
    TsInternalErrorRtException.checkNull( pfBound );
    if( pfBound.getFile() == null ) {
      ITsE4Helper helper = aWinContext.get( ITsE4Helper.class );
      Display display = aWinContext.get( Display.class );
      display.asyncExec( () -> helper.switchToPerspective( PERSPID_SKIDE_WELCOME, PARTID_SKIDE_WELCOM_BROWSER ) );
    }
  }

}
