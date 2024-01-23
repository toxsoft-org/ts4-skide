package org.toxsoft.skide.task.codegen.gen;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

/**
 * Writes Java <code>interface</code> file with constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IJavaConstantsInterfaceWriter
    extends IJavaFileWriter {

  void addConstString( String aConstName, String aConstantValue, String aCommentLine );

  void addConstBool( String aConstName, boolean aConstantValue, String aCommentLine );

  void addConstInt( String aConstName, int aConstantValue, String aCommentLine );

  void addConstLong( String aConstName, long aConstantValue, String aCommentLine );

  void addConstFloat( String aConstName, float aConstantValue, String aCommentLine );

  void addConstDouble( String aConstName, double aConstantValue, String aCommentLine );

  void addSeparatorLine();

  void addCommentLine( String aCommentLine );

  // ------------------------------------------------------------------------------------
  // inline methods for convenience

  default void addConstString( String aConstName, String aConstantValue ) {
    addConstString( aConstName, aConstantValue, EMPTY_STRING );
  }

  default void addConstBool( String aConstName, boolean aConstantValue ) {
    addConstBool( aConstName, aConstantValue, EMPTY_STRING );
  }

  default void addConstInt( String aConstName, int aConstantValue ) {
    addConstInt( aConstName, aConstantValue, EMPTY_STRING );
  }

  default void addConstLong( String aConstName, long aConstantValue ) {
    addConstLong( aConstName, aConstantValue, EMPTY_STRING );
  }

  default void addConstFloat( String aConstName, float aConstantValue ) {
    addConstFloat( aConstName, aConstantValue, EMPTY_STRING );
  }

  default void addConstDouble( String aConstName, double aConstantValue ) {
    addConstDouble( aConstName, aConstantValue, EMPTY_STRING );
  }

}
