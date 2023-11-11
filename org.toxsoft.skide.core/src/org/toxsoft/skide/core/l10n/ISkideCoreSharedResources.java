package org.toxsoft.skide.core.l10n;

import org.toxsoft.skide.core.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.core.e4.handlers.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideCoreSharedResources {

  /**
   * {@link ISkideCoreConstants}
   */
  String STR_APP_INFO   = Messages.getString( "STR_APP_INFO" );   //$NON-NLS-1$
  String STR_APP_INFO_D = Messages.getString( "STR_APP_INFO_D" ); //$NON-NLS-1$

  /**
   * {@link ISkideProjectPropertiesConstants}
   */
  String STR_SPP_NAME          = Messages.getString( "STR_N_SPP_NAME" );        //$NON-NLS-1$
  String STR_SPP_NAME_D        = Messages.getString( "STR_D_SPP_NAME" );        //$NON-NLS-1$
  String STR_SPP_DESCRIPTION   = Messages.getString( "STR_N_SPP_DESCRIPTION" ); //$NON-NLS-1$
  String STR_SPP_DESCRIPTION_D = Messages.getString( "STR_D_SPP_DESCRIPTION" ); //$NON-NLS-1$

  /**
   * {@link QuantSkide020SkConnection}
   */
  String LOG_FMT_INFO_TEXTUAL_SYSDB = Messages.getString( "LOG_FMT_INFO_TEXTUAL_SYSDB" ); //$NON-NLS-1$
  String LOG_FMT_INFO_SQLITE_SYSDB  = Messages.getString( "LOG_FMT_INFO_SQLITE_SYSDB" );  //$NON-NLS-1$

  /**
   * {@link QuantSkide030Environment}
   */
  String FMT_ERR_CANT_INIT_SKIDE_PLUGIN = Messages.getString( "FMT_ERR_CANT_INIT_SKIDE_PLUGIN" ); //$NON-NLS-1$

  /**
   * {@link SkidePluginsRegistrator}
   */
  String FMT_ERR_DUPLICATE_PROJ_UNIT_ID = Messages.getString( "FMT_ERR_DUPLICATE_PROJ_UNIT_ID" ); //$NON-NLS-1$

  /**
   * {@link CmdSkideShowPluginsList}
   */
  String DLG_SHOW_UNITS   = Messages.getString( "DLG_SHOW_UNITS" );   //$NON-NLS-1$
  String DLG_SHOW_UNITS_D = Messages.getString( "DLG_SHOW_UNITS_D" ); //$NON-NLS-1$

}
