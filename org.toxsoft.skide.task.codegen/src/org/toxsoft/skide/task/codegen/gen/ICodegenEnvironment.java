package org.toxsoft.skide.task.codegen.gen;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Code generation environment is input for code generation tasks.
 * <p>
 * Environment is designed to write all files in the {@link #outputDiirectory()} and all files will be of Java package
 * {@link #packageName()}.
 * <p>
 * Must be supplied as reference {@link ICodegenConstants#REFDEF_CODEGEN_ENV} in the task input context.
 *
 * @author hazard157
 */
public interface ICodegenEnvironment {

  /**
   * Returns the directory where the generated files will be placed.
   *
   * @return {@link File} - the directory
   */
  File outputDiirectory();

  /**
   * Returns Java package name for <code>package</code> instruction of generated files.
   *
   * @return String - Java package name
   */
  String packageName();

  /**
   * Creates the Java interface writer with the specified name.
   *
   * @param aInterfaceName String - Java interface name, also is the file name
   * @return {@link IJavaConstantsInterfaceWriter} - created writer
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid name
   */
  IJavaConstantsInterfaceWriter createJavaInterfaceWriter( String aInterfaceName );

}
