package org.toxsoft.skide.core.env;

import org.toxsoft.core.tsgui.bricks.qtree.mgr.*;
import org.toxsoft.core.tslib.bricks.qnodes.helpers.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Contains contributors to the SkIDE project tree provided by the SkIDE plugin.
 *
 * @author hazard157
 */
public sealed interface ISkideProjectTreeContribution permits SkideProjectTreeContribution {

  /**
   * Returns the contributors to build the SkIDE project tree.
   *
   * @return {@link IStringMap}&lt;{@link IQNodeChildsContributor}&gt; - the map "node kind ID" - "childs contributor"
   */
  IStringMap<IQNodeChildsContributor> childsContributors();

  /**
   * Returns the actions bind to the nodes of the SkIDE project tree.
   *
   * @return {@link IList}&lt;{@link IQTreeNodeActionsProvider}&gt; - the list of providers
   */
  IList<IQTreeNodeActionsProvider> nodeActionsProviders();

}
