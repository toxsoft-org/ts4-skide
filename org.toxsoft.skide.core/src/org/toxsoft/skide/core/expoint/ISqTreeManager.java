package org.toxsoft.skide.core.expoint;

import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.bricks.qnodes.helpers.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages SkIDE project content visualisation tree.
 * <p>
 * As project content is represented as tree of nodes of type {@link IQNode}, <i>SqTree</i> is abbreviated from
 * "<b><i>S</i></b>kIDE <b><i>Q</i></b>Nodes <i>Tree</i>".
 *
 * @author hazard157
 */
public interface ISqTreeManager {

  // ------------------------------------------------------------------------------------
  // node child contributon

  /**
   * Registers node child contributor.
   * <p>
   * Duplicate registration has no effect.
   *
   * @param aNodeKindId String - the node kind ID
   * @param aContributor {@link IQNodeChildsContributor} - the contributor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws TsIllegalArgumentRtException childs contribution is not allowed for this node kind
   */
  void registerNodeChildsContibutor( String aNodeKindId, IQNodeChildsContributor aContributor );

  /**
   * Returns all contributors, registered for the given node kind.
   *
   * @param aNodeKindId String - the node kind ID
   * @return {@link IList}&lt;{@link IQNodeChildsContributor}&gt; - list of node childs contributors
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<IQNodeChildsContributor> listNodeContributors( String aNodeKindId );

  /**
   * Returns the IDs of node kinds with at least one contributor registered.
   *
   * @return {@link IStringList} - list of contributed node kind IDs
   */
  IStringList listNodeKindIdsWithContributors();

  // ------------------------------------------------------------------------------------
  // node GUI providers

  /**
   * Registers node GUI contributor.
   * <p>
   * The provider is registered for the given kind of node or, if kind ID is an empty string - for literally all nodes.
   * <p>
   * Duplicate registration has no effect. When registering provider for all nodes, such provider will never be listed
   * as node kind specific provider in {@link #listNodeGuiProviders(String)}.
   *
   * @param aNodeKindId String - the node kind ID or an empty string for any kind of nodes
   * @param aProvider {@link ISqTreeNodeGuiProvider} - the GUI provider for node
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  void registerNodeGuiProvider( String aNodeKindId, ISqTreeNodeGuiProvider aProvider );

  /**
   * Returns all GUI providers, registered for the given node kind.
   * <p>
   * Returned list does <b>not</b> contains GUI providers for all nodes.
   *
   * @param aNodeKindId String - the node kind ID
   * @return {@link IList}&lt;{@link ISqTreeNodeGuiProvider}&gt; - list of node GUI providers
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<ISqTreeNodeGuiProvider> listNodeGuiProviders( String aNodeKindId );

  /**
   * Returns all GUI providers, registered for all node kinds.
   *
   * @return {@link IList}&lt;{@link ISqTreeNodeGuiProvider}&gt; - list of GUI providers for all nodes
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<ISqTreeNodeGuiProvider> listAllNodeGuiProviders();

  /**
   * Returns the IDs of node kinds with at least one GUI provider registered.
   * <p>
   * Providers registered for all nodes are not counted as node kind specific provider.
   *
   * @return {@link IStringList} - list of GUI provided node kind IDs
   */
  IStringList listNodeKindIdsWithGuiProviders();

}
