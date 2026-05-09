package org.toxsoft.uskat.core.gui.sded2.gui;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.gui.glib.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * Viewer panel displays all properties of {@link ISkClassInfo} as a single tree.
 *
 * @author hazard157
 */
public class PanelSkClassPropBrowser
    extends AbstractSkLazyPanel
    implements IGenericEntityPanel<ISkClassInfo>, ISkConnected {

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aUsedConnId {@link IdChain} - ID of connection to be used, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelSkClassPropBrowser( ITsGuiContext aContext, IdChain aUsedConnId ) {
    super( aContext, aUsedConnId );
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkLazyPanel
  //

  @Override
  protected void doInitGui( Composite aParent ) {
    // TODO Auto-generated method stub

  }

  // ------------------------------------------------------------------------------------
  // IGenericEntityPanel
  //

  @Override
  public boolean isViewer() {
    return true;
  }

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setEntity( ISkClassInfo aEntity ) {
    // TODO Auto-generated method stub

  }

}
