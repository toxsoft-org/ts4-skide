package org.toxsoft.skide.projtree;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideProjtreeConstants {

  String SKIDE_ID = "skide"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // E4

  String PERSPID_SKIDE_PROJTREE = "org.toxsoft.skide.persp.proj_tree"; //$NON-NLS-1$
  String PARTID_PROJTREE        = "org.toxsoft.skide.part.proj_tree";  //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";   //$NON-NLS-1$
  String ICONID_PROJ_TREE          = "proj-tree"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkideProjtreeConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
  }

}
