package org.toxsoft.skide.core.api;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Starting point for SkIDE plugins.
 *
 * @author hazard157
 */
public interface ISkidePluginsRegistrator {

  /**
   * Returns all registered plug-ins.
   *
   * @return {@link IStridablesList}&lt;{@link AbstractSkidePlugin}&gt; - the list of plug-ins
   */
  IStridablesList<AbstractSkidePlugin> listRegisteredPlugins();

  /**
   * Registers plugin and adds it's functionality to the SkIDE.
   *
   * @param aPlugin {@link AbstractSkidePlugin} - the plugin to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException plugin with the same IS is alreay registered
   */
  void registerPlugin( AbstractSkidePlugin aPlugin );

  /**
   * Lists created units of all initialized plugins.
   *
   * @return {@link IStridablesList}&lt;{@link ISkideUnit}&gt; - list of created units
   */
  IStridablesList<ISkideUnit> listUnits();

}
