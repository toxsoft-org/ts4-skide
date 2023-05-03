package org.toxsoft.skide.core;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.skide.core.l10n.ISkideCoreSharedResources.*;

import java.time.Month;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideCoreConstants {

  String SKIDE_ID      = "skide";               //$NON-NLS-1$
  String SKIDE_FULL_ID = TS_FULL_ID + ".skide"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // App info

  TsVersion APP_VERSION = new TsVersion( 5, 0, 2023, Month.MARCH, 23 );

  ITsApplicationInfo APP_INFO =
      new TsApplicationInfo( SKIDE_FULL_ID, STR_APP_INFO, STR_APP_INFO_D, SKIDE_ID, APP_VERSION );

  // ------------------------------------------------------------------------------------
  // E4

  String PERPSID_SKIDE_PROJECT = "org.toxsoft.skide.persp.project"; //$NON-NLS-1$

  String CMDID_SKIDE_SHOW_PLUGINS = "org.toxsoft.skide.cmd.show_plugins"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";        //$NON-NLS-1$
  String ICONID_APP_ICON           = "app-icon";       //$NON-NLS-1$
  String ICONID_FOLDER_GENERAL     = "folder-general"; //$NON-NLS-1$
  String ICONID_FILE_GENERAL       = "file-general";   //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context.
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkideCoreConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
  }

}
