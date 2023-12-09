package org.toxsoft.skide.task.codegen.gen;

import static org.toxsoft.skide.task.codegen.gen.ISkResources.*;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Java type name validator.
 *
 * @author hazard157
 */
public class ValidatorJavaTypeName
    implements ITsValidator<String> {

  /**
   * The singleton instance.
   */
  public static final ITsValidator<String> VALIDATOR = new ValidatorJavaTypeName();

  private ValidatorJavaTypeName() {
    // nop
  }

  @Override
  public ValidationResult validate( String aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    if( aValue.isBlank() ) {
      return ValidationResult.error( MSG_ERR_JAVA_TYPE_NAME_IS_BLANK );
    }
    if( !StridUtils.isValidIdName( aValue ) ) {
      return ValidationResult.error( FMT_ERR_JAVA_TYPE_NAME_NON_IDNAME, aValue );
    }
    if( !Character.isUpperCase( aValue.charAt( 0 ) ) ) {
      return ValidationResult.warn( FMT_WARN_JAVA_TYPE_NAME_NON_UPPER_FIRST_CHAR, aValue );
    }
    return ValidationResult.SUCCESS;
  }

}
