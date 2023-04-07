package org.toxsoft.skide.plugin.sded;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideSdedConstants {

  // ------------------------------------------------------------------------------------
  // E4

  String PERSPID_SDED_CLASSES_EDITOR = "org.toxsoft.skide.sded.persp.classes_editor"; //$NON-NLS-1$
  String PERSPID_SDED_OBJECTS_EDITOR = "org.toxsoft.skide.sded.persp.objects_editor"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICON_"; //$NON-NLS-1$
  // String ICON_FOO = "foo"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkideSdedConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
  }

}