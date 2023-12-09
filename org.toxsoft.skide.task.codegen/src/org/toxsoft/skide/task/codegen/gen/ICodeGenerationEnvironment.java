package org.toxsoft.skide.task.codegen.gen;

import java.io.*;

import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.task.codegen.gen.cw.*;

/**
 * Information, common for all generated project files.
 *
 * @author hazard157
 */
public interface ICodeGenerationEnvironment {

  /**
   * Returns the directory, where the generated files will be written.
   *
   * @return {@link File} - generated code directory
   */
  File outputDirectory();

  /**
   * Returns the Java package name of the generated code.
   *
   * @return String - the Java package name
   */
  String packageName();

  /**
   * Returns the code generation settings.
   *
   * @return {@link CodeWriterSettings} - the code generation settings
   */
  CodeWriterSettings codeWriterSettings();

  /**
   * Returns the SkIDE project properties, the developed application properties.
   *
   * @return {@link ISkideProjectPropertiesRo} - project properties
   */
  ISkideProjectPropertiesRo projProps();

}
