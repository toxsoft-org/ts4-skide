package org.toxsoft.skide.core;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideCoreConstants {

  String SKIDE_ID = "org.toxsoft.skide"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // E4

  String CATEGID_SKIDE_GENERAL           = "org.toxsoft.skide.category.skide_general";  //$NON-NLS-1$
  String CMDID_SKIDE_OPEN_PROJECT_SERVER = "org.toxsoft.skide.cmd.open_project_server"; //$NON-NLS-1$
  String CMDID_SKIDE_OPEN_PROJECT_FILE   = "org.toxsoft.skide.cmd.open_project_file";   //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";  //$NON-NLS-1$
  String ICONID_APP_ICON           = "app-icon"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkideCoreConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
  }

}
