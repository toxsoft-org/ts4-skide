package org.toxsoft.skide.core.glib;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Filter panel for {@link PanelQSubtree}.
 *
 * @author hazard157
 */
public class FilterPane
    extends AbstractLazyPanel<Control> {

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException аргумент = null
   */
  public FilterPane( ITsGuiContext aContext ) {
    super( aContext );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    TsComposite board = new TsComposite( aParent );
    // TODO Auto-generated method stub
    return board;
  }

}
