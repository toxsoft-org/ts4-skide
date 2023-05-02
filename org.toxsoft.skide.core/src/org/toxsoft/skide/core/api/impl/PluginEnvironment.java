package org.toxsoft.skide.core.api.impl;

import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.skide.core.api.*;

/**
 * {@link IPluginEnvironment} implementation.
 *
 * @author hazard157
 */
class PluginEnvironment
    implements IPluginEnvironment {

  private final ITsWorkroomStorage wrStorage;

  public PluginEnvironment( ITsWorkroomStorage aWrStorage ) {
    wrStorage = aWrStorage;
  }

  // ------------------------------------------------------------------------------------
  // IPluginEnvironment
  //

  @Override
  public ITsWorkroomStorage wrStorage() {
    return wrStorage;
  }

}
