package org.toxsoft.skide.core.gui;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.gui.m5.*;

/**
 * SkIDE project explorer panel.
 * <p>
 * Contains:
 * <ul>
 * <li>left list of project units;</li>
 * <li>right unit panel changes every time when selection in left list changes.</li>
 * </ul>
 *
 * @author hazard157
 */
public class PanelSkideProjectExplorer
    extends TsPanel {

  // TODO toolbar with selected unit's actions
  // TODO pop-up menu with selected unit's actions

  private final IM5CollectionPanel<ISkideUnit> unitsList;
  private final ContentPanel                   contentPanel;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelSkideProjectExplorer( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    SashForm sfMain = new SashForm( this, SWT.HORIZONTAL );
    // unitsList
    IM5Model<ISkideUnit> model = m5().getModel( SkideUnitM5Model.MODEL_ID, ISkideUnit.class );
    ISkideEnvironment skEnv = tsContext().get( ISkideEnvironment.class );
    IM5LifecycleManager<ISkideUnit> lm = model.getLifecycleManager( skEnv.pluginsRegistrator() );
    IM5ItemsProvider<ISkideUnit> ip = lm.itemsProvider();
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IPrefBundle pb = prefBundle( PBID_SKIDE_MAIN );
    EIconSize nodeIconSize = APPREF_UNITS_LIST_ICON_SIZE.getValue( pb.prefs() ).asValobj();
    OPDEF_NODE_ICON_SIZE.setValue( ctx.params(), avValobj( nodeIconSize ) );
    OPDEF_IS_SUPPORTS_TREE.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_ACTIONS_TREE_MODES.setValue( ctx.params(), AV_TRUE );
    OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AV_FALSE );
    MultiPaneComponentModown<ISkideUnit> mpc = new MultiPaneComponentModown<>( ctx, model, ip );
    TreeModeInfo<ISkideUnit> tmiByCategory = new TreeModeInfo<>( "ByCateg",

        "By category", "By categories",
        // FIXME STR_TMI_BY_CATEGORY, STR_TMI_BY_CATEGORY_D,

        null, new SkideUnitM5TreeMakerByCategory() );
    mpc.treeModeManager().addTreeMode( tmiByCategory );
    mpc.treeModeManager().setCurrentMode( tmiByCategory.id() );
    unitsList = new M5CollectionPanelMpcModownWrapper<>( mpc, true );

    // FIXME ???
    // unitsList = model.panelCreator().createCollViewerPanel( ctx, ip );
    unitsList.createControl( sfMain );
    // content panel
    ctx = new TsGuiContext( tsContext() );
    contentPanel = new ContentPanel( sfMain, ctx );
    // setup
    sfMain.setWeights( 2000, 8000 );
    unitsList.addTsSelectionListener( ( src, sel ) -> contentPanel.setUnitToDisplay( sel ) );
  }

}
