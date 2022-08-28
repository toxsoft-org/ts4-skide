package org.toxsoft.skide.projtree.e4.uiparts;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.base.gui.e4.uiparts.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Project tree view.
 *
 * @author hazard157
 */
public class UipartSkideProjectTree
    extends SkMwsAbstractPart {

  @Override
  public ISkConnection skConn() {
    ISkConnectionSupplier cs = tsContext().get( ISkConnectionSupplier.class );
    return cs.defConn();
  }

  @Override
  protected void doCreateContent( TsComposite aParent ) {

    Button b = new Button( aParent, SWT.PUSH );
    b.setText( getClass().getName() );
    b.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        TsDialogUtils.underDevelopment( getShell() );
      }

    } );

  }

}
