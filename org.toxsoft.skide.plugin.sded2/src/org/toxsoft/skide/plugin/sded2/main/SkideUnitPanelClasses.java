package org.toxsoft.skide.plugin.sded2.main;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.uskat.core.gui.glib.*;
import org.toxsoft.uskat.core.gui.glib2.*;

/**
 * {@link AbstractSkideUnitPanel} implementation for {@link SkideUnitSdedClasses}.
 *
 * @author hazard157
 */
class SkideUnitPanelClasses
    extends AbstractSkideUnitPanel {

  public SkideUnitPanelClasses( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    SdedClassEditor sdedClasses = new SdedClassEditor( tsContext(), null );
    return sdedClasses.createControl( aParent );
  }

}
