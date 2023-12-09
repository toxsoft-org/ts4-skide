package org.toxsoft.skide.task.codegen.gen.cw;

/**
 * Output text writer for java code creation.
 * <p>
 * For each created Java source code file the dedicated writer instance is created.
 *
 * @author hazard157
 */
public interface ICodeWriter {

  void printComment( String... aCommentLines );

  void println( String aLine );

  void p( String aFomatString, Object... aArgs );

  void pl( String aFomatString, Object... aArgs );

  void nl();

  void incNewLine();

  void decNewLine();

  /**
   * Returns the code writing (formatting) settings.
   *
   * @return {@link ICodeWriterSettings} -
   */
  ICodeWriterSettings settings();

}
