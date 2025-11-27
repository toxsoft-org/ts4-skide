package org.toxsoft.skide.plugin.sded2;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Application common constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkidePluginSded2Constants {

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";            //$NON-NLS-1$
  String ICONID_SYSDESCR           = "sysdescr";           //$NON-NLS-1$
  String ICONID_CLASS_EDITOR       = "app-classes-editor"; //$NON-NLS-1$
  String ICONID_OBJS_EDITOR        = "app-objects-editor"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkidePluginSded2Constants.class,
        PREFIX_OF_ICON_FIELD_NAME );
    //
  }

}
