
package org.toxsoft.skide.core.env;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Access point to the SkIDE API.
 *
 * @author hazard157
 */
public sealed interface ISkidePluginsRegistrator permits SkidePluginsRegistrator {

  /**
   * Returns all registered plug-ins.
   *
   * @return {@link IStridablesList}&lt;{@link ISkidePlugin}&gt; - the list of plug-ins
   */
  IStridablesList<ISkidePlugin> listRegisteredPlugins();

  /**
   * Registers plugin and adds it's functionality to the SkIDE.
   *
   * @param aPlugin {@link ISkidePlugin} - the plugin to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException plugin with the same IS is alreay registered
   */
  void registerPlugin( ISkidePlugin aPlugin );

}
