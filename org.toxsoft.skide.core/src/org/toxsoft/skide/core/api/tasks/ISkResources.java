package org.toxsoft.skide.core.api.tasks;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ISkResources {

  String STR_UNKNOWN_TASK_ID            = Messages.getString( "STR_UNKNOWN_TASK_ID" );            //$NON-NLS-1$
  String STR_NO_TASK_CAPABLE_UNITS      = Messages.getString( "STR_NO_TASK_CAPABLE_UNITS" );      //$NON-NLS-1$
  String FMT_ERR_NON_REGISTERED_UNIT_ID = Messages.getString( "FMT_ERR_NON_REGISTERED_UNIT_ID" ); //$NON-NLS-1$
  String FMT_ERR_UNIT_NOT_SUPPRTS_TASK  = Messages.getString( "FMT_ERR_UNIT_NOT_SUPPRTS_TASK" );  //$NON-NLS-1$

  String MSG_ASK_RUN_TASK_ON_WARNING = Messages.getString( "MSG_ASK_RUN_TASK_ON_WARNING" ); //$NON-NLS-1$

}
