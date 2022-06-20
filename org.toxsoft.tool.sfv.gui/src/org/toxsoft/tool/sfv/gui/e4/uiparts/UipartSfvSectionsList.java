package org.toxsoft.tool.sfv.gui.e4.uiparts;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.io.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.tool.sfv.gui.e4.main.*;
import org.toxsoft.tool.sfv.gui.e4.services.*;
import org.toxsoft.tool.sfv.gui.m5.*;

/**
 * SFV tool: sections list of the open file.
 *
 * @author hazard157
 */
public class UipartSfvSectionsList
    extends MwsAbstractPart {

  /**
   * Multi=pane cntent panel.
   *
   * @author hazard157
   */
  class Mpc
      extends MultiPaneComponentModown<ISfvSection> {

    private final IGenericChangeListener stsListenetr = aSource -> {
      refresh();
      updateActionsState();
    };

    Mpc( ITsGuiContext aContext, IM5Model<ISfvSection> aModel, IM5ItemsProvider<ISfvSection> aItemsProvider,
        IM5LifecycleManager<ISfvSection> aLifecycleManager ) {
      super( aContext, aModel, aItemsProvider, aLifecycleManager );
      sfvToolService.bound().genericChangeEventer().addListener( stsListenetr );
    }

    @Override
    protected void doDispose() {
      sfvToolService.bound().genericChangeEventer().removeListener( stsListenetr );
    }

    @Override
    protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, ISfvSection aSel ) {
      ISfvToolService sts = tsContext().get( ISfvToolService.class );
      File f = sts.bound().file();
      if( f != null ) {
        toolbar().setNameLabelText( f.getName() );
        toolbar().setTooltipText( f.getAbsolutePath() );
      }
      else {
        toolbar().setNameLabelText( model().nmName() );
        toolbar().setTooltipText( model().description() );
      }
    }

  }

  @Inject
  ISfvToolService sfvToolService;

  Mpc panel;

  @Override
  protected void doInit( Composite aParent ) {
    IM5Model<ISfvSection> model = m5().getModel( SfvSectionM5Model.MODEL_ID, ISfvSection.class );
    IM5LifecycleManager<ISfvSection> lm = model.getLifecycleManager( tsContext().get( ISfvToolService.class ) );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_ACTIONS_REFRESH.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_ACTIONS_REORDER.setValue( ctx.params(), AV_TRUE );
    panel = new Mpc( ctx, model, lm.itemsProvider(), lm );
    panel.createControl( aParent );
    panel.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
      sfvToolService.setCurrentSection( aSelectedItem );
    } );
  }

}
