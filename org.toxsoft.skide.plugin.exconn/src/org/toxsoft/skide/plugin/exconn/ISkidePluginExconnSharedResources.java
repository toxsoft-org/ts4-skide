package org.toxsoft.skide.plugin.exconn;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkidePluginExconnSharedResources {

  String STR_SKIDE_PLUGIN_EXCONN   = Messages.getString( "STR_SKIDE_PLUGIN_EXCONN" );   //$NON-NLS-1$
  String STR_SKIDE_PLUGIN_EXCONN_D = Messages.getString( "STR_SKIDE_PLUGIN_EXCONN_D" ); //$NON-NLS-1$
  String STR_SKIDE_EXCONN_UNIT     = Messages.getString( "STR_SKIDE_EXCONN_UNIT" );     //$NON-NLS-1$
  String STR_SKIDE_EXCONN_UNIT_D   = Messages.getString( "STR_SKIDE_EXCONN_UNIT_D" );   //$NON-NLS-1$

  String STR_TASK_UPLOAD   = Messages.getString( "STR_TASK_UPLOAD" );   //$NON-NLS-1$
  String STR_TASK_UPLOAD_D = Messages.getString( "STR_TASK_UPLOAD_D" ); //$NON-NLS-1$

  String STR_OP_IN_EXCONN_ID   = Messages.getString( "STR_OP_IN_EXCONN_ID" );   //$NON-NLS-1$
  String STR_OP_IN_EXCONN_ID_D = Messages.getString( "STR_OP_IN_EXCONN_ID_D" ); //$NON-NLS-1$

  String DLG_SELECT_CFG_AND_OPEN                 = Messages.getString( "DLG_SELECT_CFG_AND_OPEN" );        //$NON-NLS-1$
  String DLG_SELECT_CFG_AND_OPEN_D               = Messages.getString( "DLG_SELECT_CFG_AND_OPEN_D" );      //$NON-NLS-1$
  String FMT_ERR_UNREGISTERED_PROVIDER           = Messages.getString( "FMT_ERR_UNREGISTERED_PROVIDER" );  //$NON-NLS-1$
  String FMT_ERR_NO_SUCH_CONN_CONF_ID            = Messages.getString( "FMT_ERR_NO_SUCH_CONN_CONF_ID" );   //$NON-NLS-1$
  String STR_N_EXPORT_SYSDESCR                   = "экспорт системного описания";
  String STR_D_EXPORT_SYSDESCR                   = "Перенос системного описания в выделенное соединение";
  String STR_EXPORT_SYSDESCR                     = "Экспорт системного описания";
  String MSG_OPEN_TARGET_CONN                    = "Подключение к целевому серверу...";
  String MSG_EXPORTING                           = "Экпортиуем сущности";
  String MSG_ERR_EXPORT                          = "Ошибка операции экспорта!\nОписание ошибки:\n%s ";
  String MSG_EXPORT_PROCESS_COMPLETED_ERROR_FREE = "Процесс завершился без ошибок, данные перенесены в %s";
  String MSG_EXPORT_PROCESS_FAILED               = "Ошибка при работе с %s, обновление не прошло!";

}
