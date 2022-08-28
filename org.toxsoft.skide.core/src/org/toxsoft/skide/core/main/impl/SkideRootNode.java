package org.toxsoft.skide.core.main.impl;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.main.impl.ISkResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.qtree.mgr.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.bricks.qnodes.helpers.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.skide.core.main.*;

/**
 * Implementation of QTree root node returned by {@link ISkideEnvironment#createRootNode(ITsGuiContext)}.
 * <p>
 * The tree context {@link IQNode#tsContext()} must contain reference to the {@link ISkideEnvironment}.
 *
 * @author hazard157
 */
public class SkideRootNode
    extends AbstractQRootNode<ITsProject> {

  /**
   * ID of this node.
   */
  public static final String NODE_ID = SKIDE_ID;

  /**
   * ID of this node kind.
   */
  public static final String NODE_KIND_ID = SKIDE_ID;

  /**
   * Node kind.
   */
  public static final IQNodeKind<ITsProject> NODE_KIND =
      new QNodeKind<>( NODE_KIND_ID, ITsProject.class, true, ICONID_APP_ICON );

  /**
   * Constructor.
   *
   * @param aContext {@link ITsContext} - tree context
   * @param aEntity &lt;T&gt; - entity in this node
   * @param aParams {@link IOptionSet} - {@link #nodeData()} params initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public SkideRootNode( ITsContext aContext, ITsProject aEntity, IOptionSet aParams ) {
    super( NODE_ID, NODE_KIND, aContext, aEntity, aParams );
    setNameAndDescription( STR_N_SKIDE_ROOT_NODE, STR_D_SKIDE_ROOT_NODE );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  protected IStridablesList<IQNode> doGetNodes() {
    ISkideEnvironment skideEnv = tsContext().get( ISkideEnvironment.class );
    IQTreeContributorsManager treeManager = skideEnv.projTreeManager();
    IList<IQNodeChildsContributor> childsContributor = treeManager.listNodeContributors( NODE_KIND_ID );
    IStridablesListEdit<AbstractQNode<?>> nodes = new StridablesList<>();
    for( IQNodeChildsContributor cc : childsContributor ) {
      cc.fillNodeChilds( this, nodes );
    }
    return (IStridablesList)nodes;
  }

}
