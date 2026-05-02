package org.toxsoft.uskat.core.gui.sded2;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;

/**
 * SDED v2 initialization quant.
 *
 * @author hazard157
 */
public class QuantSkCoreSded2
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantSkCoreSded2() {
    super( QuantSkCoreSded2.class.getSimpleName() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQuant
  //

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop

  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // nop
  }

}
