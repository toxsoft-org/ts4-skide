package org.toxsoft.skide.plugin.sded.e4.uiparts;

import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.e4.uiparts.*;
import org.toxsoft.uskat.core.gui.glib.*;

/**
 * Classes editor.
 * <p>
 *
 * @author vs
 */
public class UipartSkideClassesEditor
    extends SkMwsAbstractPart {

  @Override
  public ISkConnection skConn() {
    ISkConnectionSupplier cs = tsContext().get( ISkConnectionSupplier.class );
    return cs.defConn();
  }

  @Override
  protected void doCreateContent( TsComposite aParent ) {
    SdedClassEditor sdedClasses = new SdedClassEditor( tsContext(), null );
    sdedClasses.createControl( aParent );
  }

}
