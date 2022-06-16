package org.toxsoft.tool.sfv.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IToolSfvGuiConstants {

  String SKIDE_ID = "org.toxsoft.skide"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // E4

  String PERSPID_SFV_TOOL = "org.toxsoft.too.sfv.persp.main"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";   //$NON-NLS-1$
  String ICONID_SFV_TOOL           = "sfv-tools"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IToolSfvGuiConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
  }

}
