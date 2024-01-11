package org.toxsoft.skide.task.codegen.gen;

import static org.toxsoft.core.tslib.bricks.validator.ValidationResult.*;
import static org.toxsoft.skide.task.codegen.gen.ISkCodegenSharedResources.*;

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
   * The validator without specifying the concrete Java type.
   */
  public static final ITsValidator<String> VALIDATOR = new ValidatorJavaTypeName( null );

  private static final int    MIN_RECOMMENDED_LENGTH  = 3;
  private static final char   INTERFACE_STARTING_CHAR = 'I';
  private static final char   ENUM_STARTING_CHAR      = 'E';
  private static final String NOT_RECOMMENDED_CHAR    = "_"; //$NON-NLS-1$

  private final ECodegenJavaType javaType;

  /**
   * Package-wide constructor. Constructor.
   *
   * @param aJavaType {@link ECodegenJavaType} - kind of java type to validate name for or <code>null</code>
   */
  ValidatorJavaTypeName( ECodegenJavaType aJavaType ) {
    javaType = aJavaType;
  }

  @Override
  public ValidationResult validate( String aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    if( aValue.isBlank() ) {
      return error( MSG_ERR_JAVA_TYPE_NAME_IS_BLANK );
    }
    if( !StridUtils.isValidIdName( aValue ) ) {
      return error( FMT_ERR_JAVA_TYPE_NAME_NON_IDNAME, aValue );
    }
    ValidationResult vr = SUCCESS;
    if( aValue.length() < MIN_RECOMMENDED_LENGTH ) {
      vr = warn( FMT_WARN_JAVA_TYPE_NAME_SHORT, aValue, Integer.valueOf( MIN_RECOMMENDED_LENGTH ) );
    }
    if( !Character.isUpperCase( aValue.charAt( 0 ) ) ) {
      vr = firstNonOk( vr, warn( FMT_WARN_JAVA_TYPE_NAME_NON_UPPER_FIRST_CHAR, aValue ) );
    }
    if( aValue.contains( NOT_RECOMMENDED_CHAR ) ) {
      vr = firstNonOk( vr, warn( FMT_WARN_JAVA_TYPE_NAME_NOT_RECOMMENDED_CHAR, aValue, NOT_RECOMMENDED_CHAR ) );
    }
    if( javaType == null ) {
      return vr;
    }
    switch( javaType ) {
      case INTERFACE: {
        if( aValue.charAt( 0 ) != INTERFACE_STARTING_CHAR ) {
          vr = firstNonOk( vr, warn( FMT_WARN_JAVA_INTERFACE_NOT_I_FIRST_CHAR, aValue ) );
        }
        // TODO check for camel case after starting char and warn if not
        break;
      }
      case CLASS: {
        // TODO check for camel case and warn if not
        break;
      }
      case ENUM: {
        if( aValue.charAt( 0 ) != ENUM_STARTING_CHAR ) {
          vr = firstNonOk( vr, warn( FMT_WARN_JAVA_ENUM_NOT_E_FIRST_CHAR, aValue ) );
        }
        // TODO check for camel case after starting char and warn if not
        break;
      }
      case RECORD: {
        // TODO check for camel case and warn if not
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    return vr;
  }

}
