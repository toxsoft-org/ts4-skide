package org.toxsoft.skide.task.codegen.gen.cw;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ICodeWriter} implementation.
 *
 * @author hazard157
 */
public class CodeWriter
    implements ICodeWriter {

  private final ICodeWriterSettings cwSettings;
  private final IStrioWriter        sw;

  /**
   * Constructor.
   *
   * @param aCharOut {@link ICharOutputStream} - the character output stream to write ro
   * @param aSettings {@link ICodeWriterSettings} - formatting settings
   */
  public CodeWriter( ICharOutputStream aCharOut, ICodeWriterSettings aSettings ) {
    TsNullArgumentRtException.checkNulls( aCharOut, aSettings );
    cwSettings = new CodeWriterSettings( aSettings );
    sw = new StrioWriter( aCharOut );
    sw.setIndentSpaces( cwSettings.indent() );
  }

  // ------------------------------------------------------------------------------------
  // ICodeWriter
  //

  @Override
  public void printComment( String... aCommentLines ) {
    TsErrorUtils.checkArrayArg( aCommentLines );
    int len = aCommentLines.length;
    if( len == 0 ) {
      return;
    }
    sw.pl( "/**" ); //$NON-NLS-1$
    for( String s : aCommentLines ) {
      sw.writeAsIs( " * " ); //$NON-NLS-1$
      sw.writeAsIs( s );
      sw.writeEol();
      if( s != aCommentLines[len - 1] ) {
        sw.pl( " * <p>" ); //$NON-NLS-1$
      }
    }
    sw.pl( "*/" ); //$NON-NLS-1$
  }

  @Override
  public void println( String aLine ) {
    sw.writeAsIs( aLine );
    sw.writeEol();
  }

  @Override
  public void p( String aFomatString, Object... aArgs ) {
    sw.p( aFomatString, aArgs );
  }

  @Override
  public void pl( String aFomatString, Object... aArgs ) {
    sw.pl( aFomatString, aArgs );
  }

  @Override
  public void nl() {
    sw.writeEol();
  }

  @Override
  public void incNewLine() {
    sw.incNewLine();
  }

  @Override
  public void decNewLine() {
    sw.decNewLine();
  }

  @Override
  public ICodeWriterSettings settings() {
    return cwSettings;
  }

}
