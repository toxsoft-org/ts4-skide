package org.toxsoft.skide.task.codegen.gen.impl;

import static org.toxsoft.core.tslib.bricks.strid.impl.StridUtils.*;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.task.codegen.gen.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Code generation helpers.
 *
 * @author hazard157
 */
public class CodegenUtils {

  private static final String PREFIX_OBJECT = "SKID"; //$NON-NLS-1$

  /**
   * Adds Java constant of the Sk-object SKID to the interface file.
   * <p>
   * Writes constant definition line like
   * <p>
   * <code>Skid SKID_CLASS_ID___OBJ_STRID = new Skid( "classId", "strid" ); // object name</code>
   *
   * @param aJw {@link IJavaConstantsInterfaceWriter} - file writer
   * @param aObject {@link ISkObject} - the object to write
   */
  public static void jwAddObjectSkid( IJavaConstantsInterfaceWriter aJw, ISkObject aObject ) {
    // value of the constant is "new Skid( classId, strid )";
    String classId = aObject.skid().classId();
    String strid = aObject.skid().strid();
    String rawConstValue = String.format( "new Skid( \"%s\", \"%s\" )", classId, strid ); //$NON-NLS-1$
    // name of the constant is "SKID_CLASS_ID___OBJ_STRID"
    String nClassId = classId.replace( CHAR_ID_PATH_DELIMITER, '_' ).toUpperCase();
    String nStrid = strid.replace( CHAR_ID_PATH_DELIMITER, '_' ).toUpperCase();
    String cnObj = String.format( "%s_%s___%s", PREFIX_OBJECT, nClassId, nStrid ); //$NON-NLS-1$
    // write line "Skid SKID_CLASS_ID___OBJ_STRID = new Skid( classId, strid )".
    aJw.addConstOther( "Skid", cnObj, rawConstValue, aObject.nmName() ); //$NON-NLS-1$
  }

  /**
   * Creates CamelCase string from the IDpath.
   * <p>
   * IDpath components are capitalized and separator dots are removed.
   *
   * @param aIdPath String - the IDpath
   * @return String - CamelCase representation of the argument
   */
  public static String camelizeIdPath( String aIdPath ) {
    StridUtils.checkValidIdPath( aIdPath );
    StringBuilder sb = new StringBuilder();
    char prevCh = StridUtils.CHAR_ID_PATH_DELIMITER;
    for( int i = 0; i < aIdPath.length(); i++ ) {
      char ch = aIdPath.charAt( i );
      if( prevCh == StridUtils.CHAR_ID_PATH_DELIMITER ) {
        ch = Character.toUpperCase( ch );
      }
      if( ch != StridUtils.CHAR_ID_PATH_DELIMITER ) { // точки просто пропустим
        sb.append( ch );
      }
      prevCh = ch;
    }
    return sb.toString();
  }

  /**
   * Creates a Java constant name whose value is the requested identifier with the prefix added.
   *
   * @param aPrefix String - the ID prefix (an IDname)
   * @param aIdPath String - the identifier (an IDpath)
   * @return String - upper-case Java constant name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException prefix is not an IDname
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  public static String makeJavaConstName( String aPrefix, String aIdPath ) {
    StridUtils.checkValidIdName( aPrefix );
    String right = idpath2ConstName( aIdPath );
    String left = aPrefix.toUpperCase() + '_';
    return left + right;
  }

  /**
   * Convert an IDpath to the upper-case IDname to be used in Java constants name.
   *
   * @param aIdPath String - the IDpath
   * @return String - IDname corresponding to argument
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsValidationFailedRtException argument is not an IDpath
   */
  public static String idpath2ConstName( String aIdPath ) {
    StridUtils.checkValidIdPath( aIdPath );
    return aIdPath.replace( StridUtils.CHAR_ID_PATH_DELIMITER, '_' ).toUpperCase();
  }

  private CodegenUtils() {
    // nop
  }

}
