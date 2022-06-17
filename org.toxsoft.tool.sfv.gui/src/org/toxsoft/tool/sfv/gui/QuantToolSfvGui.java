package org.toxsoft.tool.sfv.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.tool.sfv.gui.e4.services.*;
import org.toxsoft.tool.sfv.gui.m5.*;

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
    ISfvToolService sts = new SfvToolService();
    aAppContext.set( ISfvToolService.class, sts );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IToolSfvGuiConstants.init( aWinContext );
    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new SfvSectionM5Model() );
  }

}
