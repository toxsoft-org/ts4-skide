package org.toxsoft.skide.task.codegen.gen.cw;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Output text writer for java code creation.
 * <p>
 * For each created Java source code file the dedicated writer instance is created.
 *
 * @author hazard157
 */
public interface ICodeWriter {

  /**
   * Prints Java multi-line comment surrounded with "&#47;** .... *&#47;".
   * <p>
   * Does nothing if the argument is an empty array.
   *
   * @param aCommentLines String[] - comment lines
   */
  void printComment( String... aCommentLines );

  /**
   * Writes string as is and append EOL.
   *
   * @param aLine String - the string to output
   */
  void println( String aLine );

  /**
   * Writes formated string to the output.
   * <p>
   * {@link String#format(String, Object...)} formating rules are used.
   *
   * @param aFormatString String - the format string
   * @param aArgs Object[] - format arguments
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void p( String aFormatString, Object... aArgs );

  /**
   * Writes formated string to the output and appends EOL (end of line).
   * <p>
   * {@link String#format(String, Object...)} formating rules are used.
   *
   * @param aFormatString String - the format string
   * @param aArgs Object[] - format arguments
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void pl( String aFormatString, Object... aArgs );

  /**
   * Writes EOL (end of line) withh indenting rules applied.
   */
  void nl();

  /**
   * Increases indenting level and writes EOL (end of line).
   */
  void incNewLine();

  /**
   * Decreases indenting level and writes EOL (end of line).
   */
  void decNewLine();

  /**
   * Returns the code writing (formatting) settings.
   *
   * @return {@link ICodeWriterSettings} -
   */
  ICodeWriterSettings settings();

}
