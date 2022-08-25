package org.toxsoft.skide.core.incub.strigr;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

public interface IStridablesGroupiratorNode<T extends IStridable>
    extends IStridableParameterized {

  T item();

  IStridablesGroupiratorNode parent();

  IStridablesList<T> childs();

}
