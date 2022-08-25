package org.toxsoft.skide.core.devel;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skide.core.devel.proj.*;
import org.toxsoft.skide.core.devel.sys.*;

/**
 * SkIDE explorers manager.
 *
 * @author hazard157
 */
public interface ISkideExplorersManager {

  ISkideProjectExplorer projectExplorer();

  IStridablesList<ISkideSystemExplorer> sysExplorers(); // TODO does it contains builtin system?

  IStridablesList<ISkideProjectExplorerAddonProvider> listProjectAddonProviders();

  IStridablesList<ISkideSystemExplorerAddonProvider> listServiceAddonProviders();

  void registerProjectExplorerAddonProvider( ISkideSystemExplorerAddonProvider aProvider );

  void registerSystemExplorerAddonProvider( ISkideSystemExplorerAddonProvider aProvider );

}
