package org.toxsoft.skide.task.codegen.gen.impl;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Code generation helpers.
 *
 * @author hazard157
 */
public class CodegenUtils {

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
    StridUtils.checkValidIdPath( aIdPath );
    String right = aIdPath.replace( StridUtils.CHAR_ID_PATH_DELIMITER, '_' ).toUpperCase();
    String left = aPrefix.toUpperCase() + '_';
    return left + right;
  }

  private CodegenUtils() {
    // nop
  }

}
