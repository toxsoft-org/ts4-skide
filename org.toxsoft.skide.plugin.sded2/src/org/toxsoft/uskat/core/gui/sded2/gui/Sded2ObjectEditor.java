package org.toxsoft.uskat.core.gui.sded2.gui;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.inpled.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.glib.*;
import org.toxsoft.uskat.core.gui.sded2.km5.skobj.*;
import org.toxsoft.uskat.core.gui.sded2.km5.sysdecsr.*;

/**
 * Object editor - allows to create, edit and delete objects in object service.<br>
 * FIXME ??? maybe also of other claimed objects?.
 * <p>
 * Panel contains 3 panes:
 * <ul>
 * <li>left pane - classes list;</li>
 * <li>middle pane - objects list of the class selected in left pane (no subclasses, exactly of selected class);</li>
 * <li>right pane - selected object editor.</li>
 * </ul>
 * <p>
 * Notes:
 * <ul>
 * <li>FIXME Handles {@link ISkSysdescr#eventer()} events to update displayed classes list;</li>
 * <li>FIXME Handles {@link ISkObjectService#eventer()} events to update displayed objects list;</li>
 * <li>To access USkat core, the {@link ISkConnection} is used as specified in constructor.</li>
 * </ul>
 *
 * @author hazard157
 */
public class Sded2ObjectEditor
    extends AbstractSkLazyPanel {

  private final IM5CollectionPanel<ISkClassInfo> clsTree;

  private IM5CollectionPanel<ISkObject> objsList;
  private IInplaceEditorPanel           objEditor;

  private Composite holdObjsList;
  private Composite holdObjEditor;

  /**
   * Constructor.
   * <p>
   * Panel will use {@link ISkConnection} with the given ID from {@link ISkConnectionSupplier#getConn(IdChain)}. If
   * <code>aSuppliedConnectionId</code> = <code>null</code>, then {@link ISkConnectionSupplier#defConn()} will be used.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aSuppliedConnectionId {@link IdChain} - ID of connection or <code>null</code> for default
   */
  public Sded2ObjectEditor( ITsGuiContext aContext, IdChain aSuppliedConnectionId ) {
    super( aContext, aSuppliedConnectionId );
    // clsTree
    IM5Model<ISkClassInfo> m5mClass = m5().getModel( Sded2SkClassInfoM5Model.MODEL_ID, ISkClassInfo.class );
    IM5LifecycleManager<ISkClassInfo> lmClass = m5mClass.getLifecycleManager( skConn() );
    IM5ItemsProvider<ISkClassInfo> ipClass = lmClass.itemsProvider();
    ITsGuiContext ctx1 = new TsGuiContext( tsContext() );
    OPDEF_IS_FILTER_PANE.setValue( ctx1.params(), AV_TRUE );
    clsTree = m5mClass.panelCreator().createCollViewerPanel( ctx1, ipClass );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenClassSelectionChanges() {
    // remove previous objects list and editor
    clearMiddleAndRightPanes();
    // if no class is selected, leave panels blank
    ISkClassInfo selClass = clsTree.selectedItem();
    if( selClass == null ) {
      return;
    }
    // create objects list of the selected class
    IM5Model<ISkObject> m5mObjs = m5().getModel( Sded2SkObjectM5Model.MODEL_ID, ISkObject.class );
    IM5LifecycleManager<ISkObject> lmObjs = new Sded2SkObjectM5LifecycleManager( m5mObjs, skConn(), selClass.id() );
    IM5ItemsProvider<ISkObject> ipObjs = lmObjs.itemsProvider();
    ITsGuiContext ctx1 = new TsGuiContext( tsContext() );
    OPDEF_IS_ACTIONS_TREE_MODES.setValue( ctx1.params(), AV_FALSE );
    OPDEF_IS_SUPPORTS_TREE.setValue( ctx1.params(), AV_FALSE );
    objsList = m5mObjs.panelCreator().createCollEditPanel( ctx1, ipObjs, lmObjs );
    objsList.createControl( holdObjsList );
    objsList.getControl().setLayoutData( new BorderData( SWT.CENTER ) );
    // setup
    objsList.addTsSelectionListener( ( src, sel ) -> whenObjectSelectionChanges() );
  }

  private void whenObjectSelectionChanges() {
    clearRightPane();

    ISkObject selObj = objsList.selectedItem();
    if( selObj == null ) {
      return;
    }
    // create M5 editor panel
    IM5Model<ISkObject> model = m5().getModel( selObj.classId(), ISkObject.class );
    IM5LifecycleManager<ISkObject> lm = model.getLifecycleManager( skConn() );
    ITsGuiContext ctx1 = new TsGuiContext( tsContext() );
    OPDEF_IS_ACTIONS_TREE_MODES.setValue( ctx1.params(), AV_FALSE );
    OPDEF_IS_SUPPORTS_TREE.setValue( ctx1.params(), AV_FALSE );
    IM5EntityPanel<ISkObject> m5Panel = model.panelCreator().createEntityEditorPanel( ctx1, lm );
    // wrap in inplace editor
    ITsGuiContext ctx2 = new TsGuiContext( tsContext() );
    AbstractContentPanel contentPanel = new InplaceContentM5EntityPanelWrapper<>( ctx2, m5Panel );
    objEditor = new InplaceEditorContainerPanel( tsContext(), contentPanel );
    objEditor.createControl( holdObjEditor );
    objEditor.getControl().setLayoutData( new BorderData( SWT.CENTER ) );
    m5Panel.setEntity( selObj );
    objEditor.cancelAndFinishEditing();
    // m5Panel.setEditable( false );
    holdObjEditor.layout( true, true );
  }

  private void clearMiddleAndRightPanes() {
    clearRightPane();
    if( objsList != null ) {
      objsList.getControl().dispose();
      objsList = null;
    }
  }

  private void clearRightPane() {
    if( objEditor != null ) {
      objEditor.getControl().dispose();
      objEditor = null;
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkLazyPanel
  //

  @Override
  protected void doInitGui( Composite aParent ) {
    SashForm sfMain = new SashForm( aParent, SWT.HORIZONTAL );
    // clsTree
    clsTree.createControl( sfMain );
    holdObjsList = new Composite( sfMain, SWT.BORDER );
    holdObjsList.setLayout( new BorderLayout() );
    holdObjEditor = new Composite( sfMain, SWT.BORDER );
    holdObjEditor.setLayout( new BorderLayout() );
    // setup
    sfMain.setWeights( 2500, 2500, 5000 );
    clsTree.addTsSelectionListener( ( src, sel ) -> whenClassSelectionChanges() );
    whenClassSelectionChanges();
  }

}
