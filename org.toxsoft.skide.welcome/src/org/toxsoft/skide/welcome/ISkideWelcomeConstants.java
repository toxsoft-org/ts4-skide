package org.toxsoft.skide.welcome;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideWelcomeConstants {

  String SKIDE_ID = "org.toxsoft.skide"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // E4

  String PERSPID_SKIDE_WELCOME       = "org.toxsoft.skide.persp.welcome";        //$NON-NLS-1$
  String PARTID_SKIDE_WELCOM_BROWSER = "org.toxsoft.skide.part.welcome_browser"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICON_";         //$NON-NLS-1$
  String ICON_SKIDE_WELCOME        = "skide-welcome"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkideWelcomeConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
  }

}
