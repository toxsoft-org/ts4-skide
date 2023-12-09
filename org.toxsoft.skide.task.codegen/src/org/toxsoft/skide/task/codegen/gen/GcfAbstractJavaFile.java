package org.toxsoft.skide.task.codegen.gen;

import java.io.*;
import java.nio.channels.*;

import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.task.codegen.gen.cw.*;

/**
 * Base class of the the data to create and write Java source code files.
 *
 * @author hazard157
 */
public abstract class GcfAbstractJavaFile {

  protected static final String JAVA_FILE_DOT_EXT = ".java"; //$NON-NLS-1$

  private final String                     javaTypeName;
  private final ICodeGenerationEnvironment cgEnv;

  /**
   * Used by internal code writing methods, non-<code>null</code> only when {@link #writeFileTo(File)} is running.
   */
  private ICodeWriter internalGeneratedCodeWriter = null;

  /**
   * Constructor.
   *
   * @param aJavaTypeName String - Java type name, will be the created file name also
   * @param aEnv {@link IllegalChannelGroupException} - commen settings and environment
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed validation by {@link ValidatorJavaTypeName#VALIDATOR}
   */
  GcfAbstractJavaFile( String aJavaTypeName, ICodeGenerationEnvironment aEnv ) {
    ValidatorJavaTypeName.VALIDATOR.checkValid( aJavaTypeName );
    TsNullArgumentRtException.checkNulls( aEnv );
    javaTypeName = aJavaTypeName;
    cgEnv = aEnv;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void internalWriteFile() {
    ISkideProjectPropertiesRo pp = cgEnv.projProps();
    // package name
    cw().printComment( //
        "Auto-generated file.", //$NON-NLS-1$
        String.format( "Project ID/Alias-Name: %s / %s - ", pp.projId(), pp.projAlias(), pp.name() ), //$NON-NLS-1$
        String.format( "(c) %s", pp.projCopyright() ), //$NON-NLS-1$
        "" //$NON-NLS-1$
    );
    cw().pl( "package %s;", cgEnv.packageName() ); //$NON-NLS-1$
    cw().nl();
    // imports
    IStringList ss = doListStaticImports();
    if( !ss.isEmpty() ) {
      for( String s : ss ) {
        cw().pl( "import static %s.*;", s ); //$NON-NLS-1$
      }
      cw().nl();
    }
    ss = doListImports();
    if( !ss.isEmpty() ) {
      for( String s : ss ) {
        cw().pl( "import %s.*;", s ); //$NON-NLS-1$
      }
      cw().nl();
    }
    // TODO type header
    cw().printComment( doGetTypeComments().toArray() );
    cw().pl( "public %s %s {", doGetTypeSpecification(), javaTypeName() ); //$NON-NLS-1$
    cw().incNewLine();
    // type body
    cw().println( "//@formatter:off" ); //$NON-NLS-1$
    cw().nl();
    doWriteTypeBody();
    cw().nl();
    cw().println( "//@formatter:on" ); //$NON-NLS-1$
    // type footer
    cw().decNewLine();
    cw().println( "}" ); //$NON-NLS-1$
    cw().nl();
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the output file writer.
   * <p>
   * <code>WARNING:</code> may be used only when {@link #writeFileTo(File)} is running!
   *
   * @return {@link ICodeWriter} - output writer
   * @throws TsIllegalStateRtException called outside {@link #writeFileTo(File)}
   */
  protected ICodeWriter cw() {
    TsIllegalStateRtException.checkNull( internalGeneratedCodeWriter );
    return internalGeneratedCodeWriter;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the Java type name of the source code file.
   *
   * @return String - Java type name, will be the created file name also
   */
  public final String javaTypeName() {
    return javaTypeName;
  }

  /**
   * Writes file to the specified directory.
   *
   * @param aDirectory {@link File} - the directory to write the file
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException can not write to the directory or other I/O error
   */
  public void writeFileTo( File aDirectory ) {
    TsFileUtils.checkDirWriteable( aDirectory );
    TsIllegalStateRtException.checkNoNull( internalGeneratedCodeWriter );
    File gcfFile = new File( aDirectory, javaTypeName + JAVA_FILE_DOT_EXT );
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( gcfFile ) ) {
      internalGeneratedCodeWriter = new CodeWriter( chOut, cgEnv.codeWriterSettings() );
      internalWriteFile();
    }
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  protected IStringList doListStaticImports() {
    return IStringList.EMPTY;
  }

  protected IStringList doListImports() {
    return IStringList.EMPTY;
  }

  protected IStringList doGetTypeComments() {
    return IStringList.EMPTY;
  }

  protected abstract String doGetTypeSpecification();

  protected abstract void doWriteTypeBody();

}
