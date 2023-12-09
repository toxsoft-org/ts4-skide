package org.toxsoft.skide.task.codegen.gen.cw;

/**
 * Java code generation writing (formatting) settings.
 *
 * @author hazard157
 */
public sealed interface ICodeWriterSettings
    permits CodeWriterSettings {

  /**
   * Returns generated code right margin.
   *
   * @return int - right margin characters in range {@link CodeWriterSettings#MARGIN_RANGE}
   */
  int margin();

  /**
   * Returns number io characters when indenting the generated code.
   *
   * @return int - indent characters in range {@link CodeWriterSettings#INDENT_RANGE}
   */
  int indent();

}
