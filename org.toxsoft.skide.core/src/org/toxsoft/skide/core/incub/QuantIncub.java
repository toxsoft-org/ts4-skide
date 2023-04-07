package org.toxsoft.skide.core.incub;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.uskat.s5.utils.*;

/**
 * Temporary quant while incubation code is here.
 *
 * @author hazard157
 */
public class QuantIncub
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantIncub() {
    super( QuantIncub.class.getSimpleName() );
    S5ValobjUtils.registerS5Keepers();
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
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new S5HostM5Model() );
    m5.addModel( new S5HostListM5Model() );
    //
    IValedControlFactoriesRegistry vcfReg = aWinContext.get( IValedControlFactoriesRegistry.class );
    vcfReg.registerFactory( ValedS5HostList.FACTORY );
    vcfReg.registerFactory( ValedAvValobjS5HostList.FACTORY );
  }

}
