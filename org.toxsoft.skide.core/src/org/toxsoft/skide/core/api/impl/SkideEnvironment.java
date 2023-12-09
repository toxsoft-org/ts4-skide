package org.toxsoft.skide.core.api.impl;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.skide.core.api.*;

/**
 * {@link ISkideEnvironment} implementation.
 *
 * @author hazard157
 */
public class SkideEnvironment
    implements ISkideEnvironment {

  private final ISkidePluginsRegistrator plugReg;
  private final ISkideProjectProperties  projProps;
  private final ISkideGenericTaskManager taskManager;

  /**
   * Constructor.
   *
   * @param aAppContext {@link IEclipseContext} - application level context
   */
  public SkideEnvironment( IEclipseContext aAppContext ) {
    plugReg = new SkidePluginsRegistrator();
    ITsWorkroom wr = aAppContext.get( ITsWorkroom.class );
    projProps = new SkideProjectProperties( wr.getApplicationStorage() );
    taskManager = new SkideGenericTaskManager( this );
  }

  // ------------------------------------------------------------------------------------
  // ISkideEnvironment
  //

  @Override
  public ISkidePluginsRegistrator pluginsRegistrator() {
    return plugReg;
  }

  @Override
  public ISkideProjectProperties projectProperties() {
    return projProps;
  }

  @Override
  public ISkideGenericTaskManager taskManager() {
    return taskManager;
  }

}
