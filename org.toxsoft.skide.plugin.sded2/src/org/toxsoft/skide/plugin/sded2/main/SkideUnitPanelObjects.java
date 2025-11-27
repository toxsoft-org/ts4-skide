package org.toxsoft.skide.plugin.sded2.main;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.uskat.core.gui.sded2.gui.*;

/**
 * {@link AbstractSkideUnitPanel} implementation for {@link SkideUnitSdedObjects}.
 *
 * @author hazard157
 */
class SkideUnitPanelObjects
    extends AbstractSkideUnitPanel {

  public SkideUnitPanelObjects( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    Sded2ObjectEditor sdedObjects = new Sded2ObjectEditor( tsContext(), null );
    return sdedObjects.createControl( aParent );
  }

}
