package org.toxsoft.skide.task.codegen.gen.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.skide.task.codegen.gen.*;

/**
 * {@link ICodegenEnvironment} implementation.
 *
 * @author hazard157
 */
public class CodegenEnvironment
    implements ICodegenEnvironment {

  private final File   directory;
  private final String packageName;

  /**
   * Constructor.
   *
   * @param aDirectory {@link File} - directory, where to write file
   * @param aPackageName String - Java package name for <code>package</code> instruction
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException unaccessible directory
   * @throws TsIllegalArgumentRtException invalid name
   */
  public CodegenEnvironment( File aDirectory, String aPackageName ) {
    TsFileUtils.checkDirWriteable( aDirectory );
    StridUtils.checkValidIdPath( aPackageName );
    directory = aDirectory;
    packageName = aPackageName;
  }

  // ------------------------------------------------------------------------------------
  // ICodegenEnvironment
  //

  @Override
  public File outputDiirectory() {
    return directory;
  }

  @Override
  public String packageName() {
    return packageName;
  }

  @Override
  public IJavaConstantsInterfaceWriter createJavaInterfaceWriter( String aInterfaceName ) {
    return new JavaConstantsInterfaceWriter( directory, packageName, aInterfaceName );
  }

}
