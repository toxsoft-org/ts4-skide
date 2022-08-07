package org.toxsoft.skide.welcome.e4.uiparts;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.mws.bases.*;

/**
 * The view contains available actions of SkIDE welcome perspective.
 *
 * @author hazard157
 */
public class UipartSkideWelcomeBrowser
    extends MwsAbstractPart {

  @Override
  protected void doInit( Composite aParent ) {

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
