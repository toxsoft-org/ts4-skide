package org.toxsoft.skide.core.glib;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.qtree.*;
import org.toxsoft.core.tsgui.bricks.qtree.impl.*;
import org.toxsoft.core.tsgui.bricks.qtree.mgr.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Panel displayes {@link IQNode} subtree starting from the given subtree root node.
 * <p>
 * Contains:
 * <ul>
 * <li>toolbar - content depends on the currently selected node;</li>
 * <li>filter pane - allows to filter tree content;</li>
 * <li>tree - contains given subtree root nodes childs, the root node is not visible;</li>
 * <li>bottom bar - contains some summary and selected node info.</li>
 * </ul>
 *
 * @author hazard157
 */
public class PanelQSubtree
    extends TsPanel
    implements ITsToolbarListener {

  private static final IList<ITsActionDef> PERMAMENT_ACT_DEFS = new ElemArrayList<>( //
      ACDEF_COLLAPSE_ALL, ACDEF_SEPARATOR, ACDEF_REFRESH //
  );

  private final IQTreeContributorsManager contributorsManager;

  private final Composite    toolbarBoard;
  private final FilterPane   filterPane;
  private final IQTreeViewer treeViewer;
  private final BottomPane   bottomPane;

  private TsToolbar toolbar = null;

  /**
   * Node kind ID used last time to build toolbar actions.
   */
  private String toolbarNodeKindId = null;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @param aContributorsManager {@link IQTreeContributorsManager} - tree content & actions provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelQSubtree( Composite aParent, ITsGuiContext aContext, IQTreeContributorsManager aContributorsManager ) {
    super( aParent, aContext );
    contributorsManager = TsNullArgumentRtException.checkNull( aContributorsManager );
    this.setLayout( new BorderLayout() );
    // toobar
    toolbarBoard = new Composite( this, SWT.NONE );
    recreateHdrToolbar( null );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    // middle board
    Composite middleBoard = new Composite( this, SWT.NONE );
    middleBoard.setLayout( new BorderLayout() );
    middleBoard.setLayoutData( BorderLayout.CENTER );
    // filterPane
    filterPane = new FilterPane( aContext );
    filterPane.createControl( middleBoard );
    filterPane.getControl().setLayoutData( BorderLayout.NORTH );
    // treeViwer
    treeViewer = new QTreeViewer( aContext );
    treeViewer.createControl( middleBoard );
    treeViewer.getControl().setLayoutData( BorderLayout.CENTER );
    // bottomPane
    bottomPane = new BottomPane( aContext );
    bottomPane.createControl( this );
    bottomPane.getControl().setLayoutData( BorderLayout.SOUTH );

    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Removes old and creates new toolbar with the acions of the given node.
   *
   * @param aNode {@link IQNode} - the selected node or <code>null</code>
   */
  private void recreateHdrToolbar( IQNode aNode ) {

    if( toolbar != null && Objects.equals( toolbarNodeKindId, aNode.kind().id() ) ) {
      return;
    }
    IList<ITsActionDef> nodeActions = listNodeActions( aNode );
    toolbarBoard.setLayoutDeferred( true );
    if( toolbar != null ) {
      toolbar.removeListener( this );
      toolbar.getControl().dispose();
      toolbar = null;
    }
    toolbar = TsToolbar.create( toolbarBoard, tsContext(), nodeActions );
    toolbar.getControl().setLayoutData( BorderLayout.CENTER );
    toolbar.addListener( this );
    toolbarBoard.setLayoutDeferred( false );
    toolbarBoard.getParent().layout( true, true );
  }

  /**
   * Makes list of actions for the given node from the contributing providers.
   * <p>
   * If argument is <code>null</code> actions for any node is returned.
   *
   * @param aNode {@link IQNode} - the node or <code>null</code> for actions for all nodes
   * @return {@link IList}&lt;{@link ITsActionDef}&gt; - list of bode actions
   */
  private IList<ITsActionDef> listNodeActions( IQNode aNode ) {
    String nodeKindId = aNode != null ? aNode.kind().id() : TsLibUtils.EMPTY_STRING;
    IListEdit<ITsActionDef> nodeActions = new ElemArrayList<>();
    for( IQTreeNodeActionsProvider p : contributorsManager.listNodeActionsProviders( nodeKindId ) ) {
      // remove duplcate actions
      IListEdit<ITsActionDef> ll = new ElemArrayList<>();
      for( ITsActionDef acdef : p.listActionDefs() ) {
        if( !nodeActions.hasElem( acdef ) ) {
          ll.add( acdef );
        }
      }
      // separators betweed actions of different contributos
      if( !ll.isEmpty() ) {
        if( !nodeActions.isEmpty() ) {
          nodeActions.add( ACDEF_SEPARATOR );
        }
        nodeActions.addAll( ll );
      }
    }
    return nodeActions;
  }

  // ------------------------------------------------------------------------------------
  // ITsToolbarListener
  //

  @Override
  public void onToolButtonPressed( String aActionId ) {
    // TODO Auto-generated method stub

  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the root node of the subtree.
   * <p>
   * Root node is not displayed in tree instead it's child nodes are displayed.
   *
   * @return {@link IQNode} - displayed subtree node
   */
  public IQNode getRootNode() {
    // TODO PanelSkideProjectSubtree.getRootNode()
    return null;
  }

  /**
   * Sets root node of the subtree.
   * <p>
   * Root node is not displayed in tree instead it's child nodes are displayed.
   *
   * @param aSubtreeRoot
   */
  public void setRootNode( IQNode aSubtreeRoot ) {
    // TODO PanelSkideProjectSubtree.setRootNode()
  }

  /**
   * Returns console to work with tree.
   *
   * @return {@link IQTreeConsole} - the managing console
   */
  public IQTreeConsole console() {
    return treeViewer.console();
  }

  /**
   * Determines if filter pane is visible.
   *
   * @return boolean - the filter pane visibility flag
   */
  public boolean isFilterPaneVisible() {
    // TODO PanelSkideProjectSubtree.isFilterPaneVisible()
    return true;
  }

  /**
   * Set if filter pane is visible.
   *
   * @param aVisible boolean - the filter pane visibility flag
   */
  public void setFilterPaneVisible( boolean aVisible ) {
    // TODO PanelSkideProjectSubtree.setFilterPaneVisible()
  }

}
