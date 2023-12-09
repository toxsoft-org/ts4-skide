package org.toxsoft.skide.task.codegen.gen;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ISkResources {

  // String MSG_ERR_JAVA_TYPE_NAME_IS_BLANK = Messages.getString( "MSG_ERR_JAVA_TYPE_NAME_IS_BLANK" ); //$NON-NLS-1$

  String MSG_ERR_JAVA_TYPE_NAME_IS_BLANK              = "Java type name can not be a blank string";
  String FMT_ERR_JAVA_TYPE_NAME_NON_IDNAME            =
      "Java type name '%s' for SkIDE code generation must be an IDname";
  String FMT_WARN_JAVA_TYPE_NAME_NON_UPPER_FIRST_CHAR = "Java type name '%s' should start with uppercase char";

}
