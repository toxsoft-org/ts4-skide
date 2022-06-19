package org.toxsoft.tool.sfv.gui.e4.uiparts;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tool.sfv.gui.IToolSfvGuiConstants.*;

import java.io.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
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
      sfvToolService.fileBindingChangeEventer().addListener( stsListenetr );
    }

    @Override
    protected void doDispose() {
      sfvToolService.fileBindingChangeEventer().removeListener( stsListenetr );
    }

    @Override
    protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
        IListEdit<ITsActionDef> aActs ) {
      aActs.addAll( ACDEF_SEPARATOR, ACDEF_OPEN, ACDEF_SAVE, ACDEF_SAVE_AS );
      return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
    }

    @Override
    protected void doProcessAction( String aActionId ) {
      switch( aActionId ) {
        case ACTID_OPEN: {
          File f =
              TsRcpDialogUtils.askFileOpen( getShell(), getAppPrefsValue( PBID_SFV_TOOL, APPRM_LAST_PATH ).asString() );
          if( f != null ) {
            sfvToolService.open( f );
            setAppPrefsValue( PBID_SFV_TOOL, APPRM_LAST_PATH, avStr( f.getAbsolutePath() ) );
            refresh();
          }
          break;
        }
        case ACTID_SAVE: {
          if( sfvToolService.getFile() != null ) {
            sfvToolService.save();
          }
          break;
        }
        case ACTID_SAVE_AS: {
          File f = TsRcpDialogUtils.askFileSave( getShell(), aActionId );
          if( f != null ) {
            sfvToolService.saveAs( f );
            setAppPrefsValue( PBID_SFV_TOOL, APPRM_LAST_PATH, avStr( f.getAbsolutePath() ) );
          }
          break;
        }
        default:
          break;
      }
    }

    @Override
    protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, ISfvSection aSel ) {
      ISfvToolService sts = tsContext().get( ISfvToolService.class );
      toolbar().setActionEnabled( ACTID_SAVE, sts.getFile() != null && sts.isAltered() );
      File f = sts.getFile();
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
