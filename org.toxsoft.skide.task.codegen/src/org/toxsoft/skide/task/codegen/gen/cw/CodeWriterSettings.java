package org.toxsoft.skide.task.codegen.gen.cw;

import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ICodeWriterSettings} implementation.
 *
 * @author hazard157
 */
public final class CodeWriterSettings
    implements ICodeWriterSettings {

  /**
   * Allowed range of the {@link #margin()} value.
   */
  public static final IntRange MARGIN_RANGE = new IntRange( 40, 1024 );

  /**
   * Allowed range of the {@link #indent()} value.
   */
  public static final IntRange INDENT_RANGE = new IntRange( 1, 8 );

  private int margin = 120;
  private int indent = 2;

  /**
   * Constructor.
   */
  public CodeWriterSettings() {
    // nop
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ICodeWriterSettings} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public CodeWriterSettings( ICodeWriterSettings aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    margin = aSource.margin();
    indent = aSource.indent();
  }

  // ------------------------------------------------------------------------------------
  // ICodeWriterSettings
  //

  @Override
  public int margin() {
    return margin;
  }

  @Override
  public int indent() {
    return indent;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the {@link #margin} value.
   *
   * @param aMargin int - margin number will be fit in {@link #MARGIN_RANGE}
   */
  public void setMargin( int aMargin ) {
    margin = MARGIN_RANGE.inRange( aMargin );
  }

  /**
   * Sets the {@link #indent} value.
   *
   * @param aIndent int - indent number will be fit in {@link #INDENT_RANGE}
   */
  public void setIndent( int aIndent ) {
    indent = INDENT_RANGE.inRange( aIndent );
  }

}
