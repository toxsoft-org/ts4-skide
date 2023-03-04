package org.toxsoft.skide.core.env;

import org.toxsoft.core.tsgui.bricks.qtree.mgr.*;
import org.toxsoft.core.tslib.bricks.qnodes.helpers.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * The mutable implementation of {@link ISkideProjectTreeContribution}.
 *
 * @author hazard157
 */
public final class SkideProjectTreeContribution
    implements ISkideProjectTreeContribution {

  private final IStringMapEdit<IQNodeChildsContributor> childsContributors   = new StringMap<>();
  private final IListEdit<IQTreeNodeActionsProvider>    nodeActionsProviders = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public SkideProjectTreeContribution() {
    // nop
  }

  @Override
  public IStringMapEdit<IQNodeChildsContributor> childsContributors() {
    return childsContributors;
  }

  @Override
  public IListEdit<IQTreeNodeActionsProvider> nodeActionsProviders() {
    return nodeActionsProviders;
  }

}
