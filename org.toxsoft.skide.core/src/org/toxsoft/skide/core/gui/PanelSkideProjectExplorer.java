package org.toxsoft.skide.core.gui;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
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

  private final IM5CollectionPanel<ISkideUnit> unitlsList;
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
    // TODO iconSize from application preferences!
    IMultiPaneComponentConstants.OPDEF_NODE_ICON_SIZE.setValue( ctx.params(), avValobj( EIconSize.IS_48X48 ) );
    IMultiPaneComponentConstants.OPDEF_NODE_ICON_SIZE.setValue( ctx.params(), avValobj( EIconSize.IS_48X48 ) );
    unitlsList = model.panelCreator().createCollViewerPanel( ctx, ip );
    unitlsList.createControl( sfMain );
    // content panel
    ctx = new TsGuiContext( tsContext() );
    contentPanel = new ContentPanel( sfMain, ctx );
    // setup
    sfMain.setWeights( 2000, 8000 );
    unitlsList.addTsSelectionListener( ( src, sel ) -> contentPanel.setUnitToDisplay( sel ) );
  }

}
