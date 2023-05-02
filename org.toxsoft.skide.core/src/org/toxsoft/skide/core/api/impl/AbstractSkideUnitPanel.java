package org.toxsoft.skide.core.api.impl;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.skide.core.api.*;

/**
 * Panel to access content and functionality of the SkIDE project unit.
 *
 * @author hazard157
 */
public abstract class AbstractSkideUnitPanel
    extends AbstractLazyPanel<Control> {

  private final ISkideUnit unit;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aUnit {@link ISkideUnit} - the project unit, creator of the panel
   */
  public AbstractSkideUnitPanel( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext );
    unit = aUnit;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Retruns the project unit of expected type;
   *
   * @param <U> - expected type of the unit
   * @return &lt;U&gt; - the project unit
   */
  @SuppressWarnings( "unchecked" )
  public <U extends ISkideUnit> U unit() {
    return (U)unit;
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected abstract Control doCreateControl( Composite aParent );

}
