package org.toxsoft.skide.task.codegen.gen.impl;

import static org.toxsoft.skide.task.codegen.gen.ICodegenConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.skide.task.codegen.gen.*;
import org.toxsoft.skide.task.codegen.gen.cw.*;

/**
 * {@link IJavaFileWriter} base implementation.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public abstract class AbstractJavaFileWriter
    implements IJavaFileWriter {

  private final ECodegenJavaType javaType;
  private final File             file;

  private final String packageName;
  private final String typeName;

  private final IStringListEdit staticImportNames = new StringArrayList();
  private final IStringListEdit importNames       = new StringArrayList();
  private final IStringListEdit implementsTypes   = new StringArrayList();
  private final IStringListEdit extendsTypes      = new StringArrayList();

  private String typeComment;

  /**
   * Constructor for subclasses.
   *
   * @param aDirectory {@link File} - directory, where to write file
   * @param aPackage String - Java package name for <code>package</code> instruction
   * @param aInterfaceName String - Java interface name, also is the file name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException unaccessible directory
   * @throws TsIllegalArgumentRtException invalid name encountered
   */
  protected AbstractJavaFileWriter( ECodegenJavaType aType, File aDirectory, String aPackage, String aInterfaceName ) {
    TsNullArgumentRtException.checkNulls( aType, aDirectory, aPackage, aInterfaceName );

    // Java record is not supported yet
    if( aType == ECodegenJavaType.RECORD ) {
      throw new TsUnderDevelopmentRtException( ECodegenJavaType.RECORD + " code generation is not supported yet" ); //$NON-NLS-1$
    }

    TsFileUtils.checkDirWriteable( aDirectory );
    StridUtils.checkValidIdPath( aPackage );
    ECodegenJavaType.INTERFACE.nameValidator().checkValid( aInterfaceName );
    javaType = aType;
    file = new File( aDirectory, aInterfaceName + JAVA_FILE_DOT_EXT );
    packageName = aPackage;
    typeName = aInterfaceName;
    typeComment = aInterfaceName;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void internalWritePackage( ICodeWriter aCw ) {
    Long date = Long.valueOf( System.currentTimeMillis() );
    aCw.pl( "/**" );
    aCw.pl( " * Auto-generated file, %tF %tT", date, date );
    aCw.pl( " */" );
    aCw.pl( "package %s;", packageName );
  }

  private boolean internalWriteStaticImports( ICodeWriter aCw ) {
    if( staticImportNames.isEmpty() ) {
      return false;
    }
    for( String s : staticImportNames ) {
      aCw.pl( "import static %s.*;", s );
    }
    return true;
  }

  private boolean internalWriteImports( ICodeWriter aCw ) {
    if( importNames.isEmpty() ) {
      return false;
    }
    for( String s : importNames ) {
      aCw.pl( "import %s.*;", s );
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // IJavaFileWriter
  //

  @Override
  public ECodegenJavaType javaType() {
    return javaType;
  }

  @Override
  public String typeName() {
    return typeName;
  }

  @Override
  public String packageName() {
    return packageName;
  }

  @Override
  public IStringList getStaticImports() {
    return staticImportNames;
  }

  @Override
  public void setStaticImports( IStringList aTypeFullNames ) {
    TsNullArgumentRtException.checkNull( aTypeFullNames );
    for( String s : aTypeFullNames ) {
      StridUtils.checkValidIdPath( s );
    }
    staticImportNames.setAll( aTypeFullNames );
  }

  @Override
  public IStringList getImports() {
    return importNames;
  }

  @Override
  public void setImports( IStringList aTypeFullNames ) {
    TsNullArgumentRtException.checkNull( aTypeFullNames );
    for( String s : aTypeFullNames ) {
      StridUtils.checkValidIdPath( s );
    }
    importNames.setAll( aTypeFullNames );
  }

  @Override
  public String getTypeComment() {
    return typeComment;
  }

  @Override
  public void setTypeComment( String aComment ) {
    TsNullArgumentRtException.checkNull( aComment );
    typeComment = aComment;
  }

  @Override
  public IStringList getImplementsTypes() {
    return implementsTypes;
  }

  @Override
  public IStringList getExtendsTypes() {
    return extendsTypes;
  }

  @Override
  public void setImplementsTypes( IStringList aTypeNames ) {
    TsNullArgumentRtException.checkNull( aTypeNames );
    for( String s : aTypeNames ) {
      StridUtils.checkValidIdName( s );
    }
    implementsTypes.setAll( aTypeNames );
  }

  @Override
  public void setExtendsTypes( IStringList aTypeNames ) {
    TsNullArgumentRtException.checkNull( aTypeNames );
    for( String s : aTypeNames ) {
      StridUtils.checkValidIdName( s );
    }
    extendsTypes.setAll( aTypeNames );
  }

  @Override
  public void writeFile() {
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( file ) ) {
      ICodeWriter cw = new CodeWriter( chOut );
      // header - package and imports
      internalWritePackage( cw );
      cw.nl();
      if( internalWriteStaticImports( cw ) ) {
        cw.nl();
      }
      if( internalWriteImports( cw ) ) {
        cw.nl();
      }
      // type declaration
      cw.println( "/**" );
      cw.pl( " * %s", typeComment.replace( '\n', ' ' ) );
      cw.println( " */" );
      cw.println( "@SuppressWarnings( { \"javadoc\", \"nls\" } )" );
      cw.p( "public interface %s", typeName );
      // TODO process #implementsTypes and #extendsTypes
      cw.pl( " {" );
      // the type body
      cw.incNewLine();
      doWriteTypeBody( cw );
      cw.decNewLine();
      // footer
      cw.pl( "}" );
      cw.nl();
    }
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  protected abstract void doWriteTypeBody( ICodeWriter aCw );

}
