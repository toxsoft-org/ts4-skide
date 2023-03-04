package org.toxsoft.skide.core.env;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ISkidePluginsRegistrator} implementation.
 *
 * @author hazard157
 */
public final class SkidePluginsRegistrator
    implements ISkidePluginsRegistrator {

  private IStridablesListEdit<ISkidePlugin> registeredPlugins = new StridablesList<>();

  /**
   * Constructor.
   */
  SkidePluginsRegistrator() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ISkidePluginsRegistrator
  //

  @Override
  public IStridablesList<ISkidePlugin> listRegisteredPlugins() {
    return registeredPlugins;
  }

  @Override
  public void registerPlugin( ISkidePlugin aPlugin ) {
    TsNullArgumentRtException.checkNull( aPlugin );
    TsItemAlreadyExistsRtException.checkTrue( registeredPlugins.hasKey( aPlugin.id() ) );
    registeredPlugins.add( aPlugin );
    // TODO use added plugin. How ?
  }

}
