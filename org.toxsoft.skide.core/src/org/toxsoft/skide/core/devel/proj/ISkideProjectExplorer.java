package org.toxsoft.skide.core.devel.proj;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skide.core.devel.sys.*;

/**
 * Data of current project to be presented various views in SkIDE.
 *
 * @author hazard157
 */
public interface ISkideProjectExplorer {

  /**
   * Returns builtin USkat system with always opened connection.
   *
   * @return {@link ISkideSystemExplorer} - project builtin system explorer
   */
  ISkideSystemExplorer projSys();

  IStridablesList<ISkideProjectExplorerAddon> addons();

}
