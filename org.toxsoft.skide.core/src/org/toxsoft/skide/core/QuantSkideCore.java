package org.toxsoft.skide.core;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * The library quant initializes all quants in the library.
 *
 * @author hazard157
 */
public class QuantSkideCore
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantSkideCore() {
    super( QuantSkideCore.class.getSimpleName() );
    registerQuant( new QuantSkide010Workroom() );
    registerQuant( new QuantSkide020SkConnection() );
    registerQuant( new QuantSkide030Environment() );
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
