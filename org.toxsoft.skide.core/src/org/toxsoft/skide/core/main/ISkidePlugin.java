package org.toxsoft.skide.core.main;

import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * This interface is to be implemented by SkIDE contributor and to be registered in {@link ISkidePluginsRegistrator}.
 *
 * @author hazard157
 */
public sealed interface ISkidePlugin
    extends IStridable permits AbstractSkidePlugin {

  // nop

}
