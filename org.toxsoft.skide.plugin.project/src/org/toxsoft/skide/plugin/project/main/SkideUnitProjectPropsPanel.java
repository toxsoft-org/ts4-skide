package org.toxsoft.skide.plugin.project.main;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.inpled.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * {@link AbstractSkideUnitPanel} implementation.
 *
 * @author hazard157
 */
class SkideUnitProjectPropsPanel
    extends AbstractSkideUnitPanel {

  public SkideUnitProjectPropsPanel( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    Composite board = new Composite( aParent, SWT.BORDER );
    board.setLayout( new BorderLayout() );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    InplaceContentAbstractOptionSetPanel panel = new InplaceContentAbstractOptionSetPanel( ctx ) {

      @Override
      protected void doDoApplyChanges( IOptionSet aChangedOpset ) {
        skEnv().projectProperties().setProperties( aChangedOpset );
      }
    };
    IInplaceEditorPanel inpled = new InplaceEditorContainerPanel( ctx, panel );
    inpled.createControl( board );
    inpled.getControl().setLayoutData( BorderLayout.CENTER );
    panel.setContentToEdit( skEnv().projectProperties().params(), ISkideProjectPropertiesConstants.ALL_SPP_OPS );
    return board;
  }

}
