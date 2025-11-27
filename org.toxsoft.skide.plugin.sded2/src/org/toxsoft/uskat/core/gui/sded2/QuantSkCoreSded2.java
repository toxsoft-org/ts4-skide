package org.toxsoft.uskat.core.gui.sded2;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.gui.sded2.km5.*;

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
    KM5Utils.registerContributorCreator( KM5Sded2Contributor.CREATOR );
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
