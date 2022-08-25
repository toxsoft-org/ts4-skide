package org.toxsoft.skide.core.incub.strigr;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Provides information how to create hierarchical tree of {@link IStridable} entities.
 * <p>
 * It if often needed to visualize linear list of {@link IStridable} entities in hierarchical tree. Node in tree may be
 * either items from list and/or 'fake' nodes created only for visualization needs.
 *
 * @author hazard157
 */
public interface IStridablesGroupirator<T extends IStridable> {

  IStridablesGroupiratorNode<T> root();

  IList<T> listItems();

  IdChain getPathToFirst( String aId );

  IdChain listPathsTo( String aId );

}
