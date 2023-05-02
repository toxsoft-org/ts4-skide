package org.toxsoft.skide.core.e4.uiparts;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skide.core.gui.*;

/**
 * UIpart: SkIDE project explorer.
 *
 * @author hazard157
 */
public class UipartProjectExplorer
    extends MwsAbstractPart {

  @Override
  protected void doInit( Composite aParent ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    @SuppressWarnings( "unused" )
    PanelSkideProjectExplorer panel = new PanelSkideProjectExplorer( aParent, ctx );
  }

}
