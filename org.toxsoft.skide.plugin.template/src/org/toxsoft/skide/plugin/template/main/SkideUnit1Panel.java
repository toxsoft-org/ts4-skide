package org.toxsoft.skide.plugin.template.main;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * {@link AbstractSkideUnitPanel} implementation.
 *
 * @author hazard157
 */
class SkideUnit1Panel
    extends AbstractSkideUnitPanel {

  public SkideUnit1Panel( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    CLabel label = new CLabel( aParent, SWT.CENTER );
    label.setText( "Unit template 1: right panel" ); //$NON-NLS-1$
    return label;
  }

}
