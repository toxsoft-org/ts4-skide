package org.toxsoft.skide.task.codegen.gen.l10n;

import org.toxsoft.skide.task.codegen.gen.*;
import org.toxsoft.skide.task.codegen.valed.Messages;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkCodegenSharedResources {

  /**
   * {@link ECodegenJavaType}
   */
  String STR_CG_JT_INTERFACE   = Messages.getString( "STR_CG_JT_INTERFACE" );   //$NON-NLS-1$
  String STR_CG_JT_INTERFACE_D = Messages.getString( "STR_CG_JT_INTERFACE_D" ); //$NON-NLS-1$
  String STR_CG_JT_CLASS       = Messages.getString( "STR_CG_JT_CLASS" );       //$NON-NLS-1$
  String STR_CG_JT_CLASS_D     = Messages.getString( "STR_CG_JT_CLASS_D" );     //$NON-NLS-1$
  String STR_CG_JT_ENUM        = Messages.getString( "STR_CG_JT_ENUM" );        //$NON-NLS-1$
  String STR_CG_JT_ENUM_D      = Messages.getString( "STR_CG_JT_ENUM_D" );      //$NON-NLS-1$
  String STR_CG_JT_RECORD      = Messages.getString( "STR_CG_JT_RECORD" );      //$NON-NLS-1$
  String STR_CG_JT_RECORD_D    = Messages.getString( "STR_CG_JT_RECORD_D" );    //$NON-NLS-1$

  String MSG_ERR_JAVA_TYPE_NAME_IS_BLANK = Messages.getString( "MSG_ERR_JAVA_TYPE_NAME_IS_BLANK" ); //$NON-NLS-1$

  String FMT_ERR_JAVA_TYPE_NAME_NON_IDNAME = Messages.getString( "FMT_ERR_JAVA_TYPE_NAME_NON_IDNAME" ); //$NON-NLS-1$

  String FMT_WARN_JAVA_TYPE_NAME_SHORT = Messages.getString( "FMT_WARN_JAVA_TYPE_NAME_SHORT" ); //$NON-NLS-1$

  String FMT_WARN_JAVA_TYPE_NAME_NON_UPPER_FIRST_CHAR =
      Messages.getString( "FMT_WARN_JAVA_TYPE_NAME_NON_UPPER_FIRST_CHAR" ); //$NON-NLS-1$

  String FMT_WARN_JAVA_TYPE_NAME_NOT_RECOMMENDED_CHAR =
      Messages.getString( "FMT_WARN_JAVA_TYPE_NAME_NOT_RECOMMENDED_CHAR" ); //$NON-NLS-1$

  String FMT_WARN_JAVA_INTERFACE_NOT_I_FIRST_CHAR = Messages.getString( "FMT_WARN_JAVA_INTERFACE_NOT_I_FIRST_CHAR" ); //$NON-NLS-1$

  String FMT_WARN_JAVA_ENUM_NOT_E_FIRST_CHAR = Messages.getString( "FMT_WARN_JAVA_ENUM_NOT_E_FIRST_CHAR" ); //$NON-NLS-1$

}
