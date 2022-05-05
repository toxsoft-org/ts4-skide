package org.toxsoft.skide.core;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;

/**
 * The library quant.
 *
 * @author goga
 */
public class QuantSkideCore
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantSkideCore() {
    super( QuantSkideCore.class.getSimpleName() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ISkideCoreConstants.init( aWinContext );
  }

}
