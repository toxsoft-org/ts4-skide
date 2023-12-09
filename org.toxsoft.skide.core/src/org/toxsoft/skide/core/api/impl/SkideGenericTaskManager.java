package org.toxsoft.skide.core.api.impl;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;

/**
 * {@link ISkideGenericTaskManager} implementation.
 *
 * @author hazard157
 */
public class SkideGenericTaskManager
    implements ISkideGenericTaskManager {

  private final ISkideEnvironment skideEnv;

  private final IStridablesListEdit<IGenericTaskInfo> registeredTasks = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aEnv {@link ISkideEnvironment} - the owner environment
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkideGenericTaskManager( ISkideEnvironment aEnv ) {
    TsNullArgumentRtException.checkNull( aEnv );
    skideEnv = aEnv;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // ------------------------------------------------------------------------------------
  // ISkideGenericTaskManager
  //

  @Override
  public IStridablesList<IGenericTaskInfo> listTasks() {
    return registeredTasks;
  }

  @Override
  public IStringMap<IGenericTask> runSyncSequentially( String aTaskId, ITsContextRo aInput ) {
    // check preconditions
    TsNullArgumentRtException.checkNulls( aTaskId, aInput );
    IGenericTaskInfo taskInfo = registeredTasks.getByKey( aTaskId );
    TsValidationFailedRtException.checkError( GenericTaskUtils.validateInput( taskInfo, aInput ) );
    // run only for capable items that can run the task
    IStridablesListEdit<ISkideUnit> unitsToRun = new StridablesList<>();
    for( ISkideUnit u : listCapableUnits( aTaskId ) ) {
      IGenericTaskRunner taskRunner = u.getGenericTaskRunners().getByKey( aTaskId );
      ValidationResult vr = taskRunner.canRun( aInput );
      if( !vr.isError() ) {
        unitsToRun.add( u );
      }
    }
    if( unitsToRun.isEmpty() ) {
      return IStringMap.EMPTY;
    }

    // TODO run tasks sequentially

    // TODO Auto-generated method stub

    return null;
  }

  @Override
  public IStridablesList<ISkideUnit> listCapableUnits( String aTaskId ) {
    TsNullArgumentRtException.checkNull( aTaskId );
    IStridablesListEdit<ISkideUnit> ll = new StridablesList<>();
    for( ISkideUnit u : skideEnv.pluginsRegistrator().listUnits() ) {
      if( u.getGenericTaskRunners().hasKey( aTaskId ) ) {
        ll.add( u );
      }
    }
    return ll;
  }

  @Override
  public void registerTask( IGenericTaskInfo aTaskInfo ) {
    TsNullArgumentRtException.checkNull( aTaskInfo );
    TsItemAlreadyExistsRtException.checkTrue( registeredTasks.hasKey( aTaskInfo.id() ) );
    registeredTasks.add( aTaskInfo );
  }

}
