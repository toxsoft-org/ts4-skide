package org.toxsoft.skide.core.api.impl;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;

/**
 * {@link ISkideEnvironment} implementation.
 * <p>
 * SkIDE core is not a SkIDE plugin, however it is considered as {@link ITsWorkroom} subsystem with the ID
 * {@link #WORKROOM_SUBSYSTEM_ID_SKIDE_CORE}.
 *
 * @author hazard157
 */
public class SkideEnvironment
    implements ISkideEnvironment {

  /**
   * This is the subsystem ID to retrieve SkIDE core storage.
   */
  private static final String WORKROOM_SUBSYSTEM_ID_SKIDE_CORE = SKIDE_FULL_ID + ".core"; //$NON-NLS-1$

  private final ITsWorkroom              workroom;
  private final ISkidePluginsRegistrator plugReg;
  private final ISkideProjectProperties  projProps;
  private final ISkideTaskManager        taskManager;

  /**
   * Constructor.
   *
   * @param aAppContext {@link IEclipseContext} - application level context
   */
  public SkideEnvironment( IEclipseContext aAppContext ) {
    plugReg = new SkidePluginsRegistrator();
    workroom = aAppContext.get( ITsWorkroom.class );
    projProps = new SkideProjectProperties( workroom.getApplicationStorage() );
    taskManager = new SkideTaskManager( this );
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  ITsWorkroomStorage getSkideCoreStorage() {
    return workroom.getStorage( WORKROOM_SUBSYSTEM_ID_SKIDE_CORE );
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
  public ISkideTaskManager taskManager() {
    return taskManager;
  }

}
