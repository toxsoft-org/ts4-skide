package org.toxsoft.skide.plugin.sded.main;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.uskat.core.gui.glib.*;

/**
 * {@link AbstractSkideUnitPanel} implementation for {@link SkideUnitObjects}.
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
    SdedObjectEditor sdedObjects = new SdedObjectEditor( tsContext(), null );
    return sdedObjects.createControl( aParent );
  }

}
