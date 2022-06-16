package org.toxsoft.tool.sfv.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;

/**
 * The library quant.
 *
 * @author goga
 */
public class QuantToolSfvGui
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantToolSfvGui() {
    super( QuantToolSfvGui.class.getSimpleName() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IToolSfvGuiConstants.init( aWinContext );
  }

}
