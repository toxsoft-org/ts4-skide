package org.toxsoft.skide.task.codegen.gen.impl;

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
    String cnObj = makeJavaConstName2( PREFIX_OBJECT, classId, strid );
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
   * Creates a Java constant name from two entity IDs.
   * <p>
   * This method may be used when making constant name for class property (ClassID and PropID) or and object SKID name
   * (ClassID and ObjStrid).
   *
   * @param aPrefix String - the ID prefix (an IDname)
   * @param aIdPath1 String - the identifier 1 (an IDpath)
   * @param aIdPath2 String - the identifier 2 (an IDpath)
   * @return String - upper-case Java constant name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException prefix is not an IDname
   * @throws TsIllegalArgumentRtException any identifier is not an IDpath
   */
  public static String makeJavaConstName2( String aPrefix, String aIdPath1, String aIdPath2 ) {
    StridUtils.checkValidIdName( aPrefix );
    String p1 = idpath2ConstName( aIdPath1 );
    String p2 = idpath2ConstName( aIdPath2 );
    return aPrefix + "__" + p1 + "__" + p2; //$NON-NLS-1$//$NON-NLS-2$
  }

  /**
   * Creates a Java constant name from three entity IDs.
   * <p>
   * This method may be used when making constant name for class property (ClassID and PropID) or and object SKID name
   * (ClassID and ObjStrid).
   *
   * @param aPrefix String - the ID prefix (an IDname)
   * @param aIdPath1 String - the identifier 1 (an IDpath)
   * @param aIdPath2 String - the identifier 2 (an IDpath)
   * @param aIdPath3 String - the identifier 3 (an IDpath)
   * @return String - upper-case Java constant name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException prefix is not an IDname
   * @throws TsIllegalArgumentRtException any identifier is not an IDpath
   */
  public static String makeJavaConstName3( String aPrefix, String aIdPath1, String aIdPath2, String aIdPath3 ) {
    StridUtils.checkValidIdName( aPrefix );
    String p1 = idpath2ConstName( aIdPath1 );
    String p2 = idpath2ConstName( aIdPath2 );
    String p3 = idpath2ConstName( aIdPath3 );
    return aPrefix + "__" + p1 + "__" + p2 + "__" + p3; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
