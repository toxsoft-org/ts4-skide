package org.toxsoft.skide.core.api.impl;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.*;
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
    implements ISkideEnvironment, ICloseable {

  /**
   * This is the subsystem ID to retrieve SkIDE core storage.
   */
  private static final String WORKROOM_SUBSYSTEM_ID_SKIDE_CORE = SKIDE_FULL_ID + ".core"; //$NON-NLS-1$

  private final ITsWorkroom             workroom;
  private final SkidePluginsRegistrator plugReg;
  private final SkideTaskRegistrator    taskReg;
  private final ISkideProjectProperties projProps;

  /**
   * Constructor.
   *
   * @param aAppContext {@link IEclipseContext} - application level context
   */
  public SkideEnvironment( IEclipseContext aAppContext ) {
    plugReg = new SkidePluginsRegistrator();
    workroom = aAppContext.get( ITsWorkroom.class );
    projProps = new SkideProjectProperties( workroom.getApplicationStorage() );
    taskReg = new SkideTaskRegistrator( this );
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    plugReg.close();
    taskReg.close();
  }

  // ------------------------------------------------------------------------------------
  // Internal API
  //

  @SuppressWarnings( "javadoc" )
  public void papiInitialize( ITsGuiContext aContext, ISkideEnvironment aSkEnv, ITsWorkroom aWorkroom ) {
    plugReg.papiInitialize( aContext, aSkEnv, aWorkroom );
    taskReg.papiInitialize( aContext, aSkEnv );
  }

  @SuppressWarnings( "javadoc" )
  public ITsWorkroomStorage papiGetSkideCoreStorage() {
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
  public ISkideTaskRegistrator taskRegistrator() {
    return taskReg;
  }

}
