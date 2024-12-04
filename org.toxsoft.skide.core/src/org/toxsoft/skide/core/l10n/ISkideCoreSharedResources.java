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
  String STR_SPP_PROJ_ID       = Messages.getString( "STR_SPP_PROJ_ID" );       //$NON-NLS-1$
  String STR_SPP_PROJ_ID_D     = Messages.getString( "STR_SPP_PROJ_ID_D" );     //$NON-NLS-1$
  String STR_SPP_ALIAS         = Messages.getString( "STR_SPP_ALIAS" );         //$NON-NLS-1$
  String STR_SPP_ALIAS_D       = Messages.getString( "STR_SPP_ALIAS_D" );       //$NON-NLS-1$
  String STR_SPP_NAME          = Messages.getString( "STR_SPP_NAME" );          //$NON-NLS-1$
  String STR_SPP_NAME_D        = Messages.getString( "STR_SPP_NAME_D" );        //$NON-NLS-1$
  String STR_SPP_DESCRIPTION   = Messages.getString( "STR_SPP_DESCRIPTION" );   //$NON-NLS-1$
  String STR_SPP_DESCRIPTION_D = Messages.getString( "STR_SPP_DESCRIPTION_D" ); //$NON-NLS-1$
  String STR_SPP_COPYRIGHT     = Messages.getString( "STR_SPP_COPYRIGHT" );     //$NON-NLS-1$
  String STR_SPP_COPYRIGHT_D   = Messages.getString( "STR_SPP_COPYRIGHT_D" );   //$NON-NLS-1$

  /**
   * {@link QuantSkide010Workroom}
   */
  String STR_DLG_SELECT_WORKROOM_DIR = Messages.getString( "STR_DLG_SELECT_WORKROOM_DIR" ); //$NON-NLS-1$

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
   * {@link CmdSkideRunTask}
   */
  String DLG_SELECT_TASK               = Messages.getString( "DLG_SELECT_TASK" );               //$NON-NLS-1$
  String DLG_SELECT_TASK_D             = Messages.getString( "DLG_SELECT_TASK_D" );             //$NON-NLS-1$
  String DLG_SELECT_TASK_UNITS         = Messages.getString( "DLG_SELECT_TASK_UNITS" );         //$NON-NLS-1$
  String DLG_SELECT_TASK_UNITS_D       = Messages.getString( "DLG_SELECT_TASK_UNITS_D" );       //$NON-NLS-1$
  String MSG_NO_REGISTERED_SKIDE_TASK  = Messages.getString( "MSG_NO_REGISTERED_SKIDE_TASK" );  //$NON-NLS-1$
  String FMT_WARN_NO_UNITS_TO_RUN_TASK = Messages.getString( "FMT_WARN_NO_UNITS_TO_RUN_TASK" ); //$NON-NLS-1$

  /**
   * {@link CmdSkideShowPluginsList}
   */
  String DLG_SHOW_UNITS   = Messages.getString( "DLG_SHOW_UNITS" );   //$NON-NLS-1$
  String DLG_SHOW_UNITS_D = Messages.getString( "DLG_SHOW_UNITS_D" ); //$NON-NLS-1$

}
