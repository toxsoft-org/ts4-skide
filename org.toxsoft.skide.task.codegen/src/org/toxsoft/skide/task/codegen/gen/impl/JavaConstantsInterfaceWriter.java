package org.toxsoft.skide.task.codegen.gen.impl;

import java.io.*;

import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.task.codegen.gen.*;
import org.toxsoft.skide.task.codegen.gen.cw.*;

/**
 * {@link IJavaConstantsInterfaceWriter} implementation.
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "boxing" } )
public class JavaConstantsInterfaceWriter
    extends AbstractJavaFileWriter
    implements IJavaConstantsInterfaceWriter {

  /**
   * FIXME need to check that class generated constant IDs may be duplicated<br>
   */

  private final IStringListEdit constDefLines = new StringLinkedBundleList();

  /**
   * Constructor.
   * <p>
   * Creates file <code>aFileName.java</code> in the <code>aDirectory</code>.
   *
   * @param aDirectory {@link File} - directory, where to write file
   * @param aPackage String - Java package name for <code>package</code> instruction
   * @param aInterfaceName String - Java interface name, also is the file name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException unaccessible directory
   * @throws TsIllegalArgumentRtException invalid name encountered
   */
  public JavaConstantsInterfaceWriter( File aDirectory, String aPackage, String aInterfaceName ) {
    super( ECodegenJavaType.INTERFACE, aDirectory, aPackage, aInterfaceName );
  }

  // ------------------------------------------------------------------------------------
  // IJavaConstantsInterfaceWriter
  //

  @Override
  public void addConstString( String aConstName, String aConstantValue, String aCommentLine ) {
    StringBuilder sb = new StringBuilder();
    sb.append( String.format( "String %s = \"%s\";", aConstName, aConstantValue ) );
    if( !aCommentLine.isBlank() ) {
      sb.append( " // " ).append( aCommentLine );
    }
    constDefLines.add( sb.toString() );
  }

  @Override
  public void addConstBool( String aConstName, boolean aConstantValue, String aCommentLine ) {
    StringBuilder sb = new StringBuilder();
    sb.append( String.format( "boolean %s = %s;", aConstName, Boolean.toString( aConstantValue ) ) );
    if( !aCommentLine.isBlank() ) {
      sb.append( " // " ).append( aCommentLine );
    }
    constDefLines.add( sb.toString() );
  }

  @Override
  public void addConstInt( String aConstName, int aConstantValue, String aCommentLine ) {
    StringBuilder sb = new StringBuilder();
    sb.append( String.format( "int %s = %s;", aConstName, aConstantValue ) );
    if( !aCommentLine.isBlank() ) {
      sb.append( " // " ).append( aCommentLine );
    }
    constDefLines.add( sb.toString() );
  }

  @Override
  public void addConstLong( String aConstName, long aConstantValue, String aCommentLine ) {
    StringBuilder sb = new StringBuilder();
    sb.append( String.format( "long %s = %s;", aConstName, aConstantValue ) );
    if( !aCommentLine.isBlank() ) {
      sb.append( " // " ).append( aCommentLine );
    }
    constDefLines.add( sb.toString() );
  }

  @Override
  public void addConstFloat( String aConstName, float aConstantValue, String aCommentLine ) {
    StringBuilder sb = new StringBuilder();
    sb.append( String.format( "float %s = %f;", aConstName, aConstantValue ) );
    if( !aCommentLine.isBlank() ) {
      sb.append( " // " ).append( aCommentLine );
    }
    constDefLines.add( sb.toString() );
  }

  @Override
  public void addConstDouble( String aConstName, double aConstantValue, String aCommentLine ) {
    StringBuilder sb = new StringBuilder();
    sb.append( String.format( "double %s = %f;", aConstName, aConstantValue ) );
    if( !aCommentLine.isBlank() ) {
      sb.append( " // " ).append( aCommentLine );
    }
    constDefLines.add( sb.toString() );
  }

  @Override
  public void addSeparatorLine() {
    constDefLines.add( TsLibUtils.EMPTY_STRING );
  }

  @Override
  public void addCommentLine( String aCommentLine ) {
    constDefLines.add( String.format( "// %s", aCommentLine ) );
  }

  @Override
  public void addConstOther( String aType, String aConstName, String aRawValue, String aCommentLine ) {
    StringBuilder sb = new StringBuilder();
    sb.append( String.format( "%s %s = %s;", aType, aConstName, aRawValue ) );
    if( !aCommentLine.isBlank() ) {
      sb.append( " // " ).append( aCommentLine );
    }
    constDefLines.add( sb.toString() );
  }

  @Override
  public void addRawLine( String aLine ) {
    constDefLines.add( aLine );
  }

  // ------------------------------------------------------------------------------------
  // AbstractJavaFileWriter
  //

  @Override
  protected void doWriteTypeBody( ICodeWriter aCw ) {
    for( String s : constDefLines ) {
      aCw.println( s );
    }
  }

}
