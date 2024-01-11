package org.toxsoft.skide.core.api.impl;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.skide.core.api.*;

/**
 * {@link ISkideTaskManager} implementation.
 *
 * @author hazard157
 */
public class SkideTaskManager
    implements ISkideTaskManager {

  /**
   * The ID of the section storing task input option values.
   * <p>
   * The map "task ID" - "input options set" is stored in {@link SkideEnvironment#getSkideCoreStorage()} using
   * {@link IKeepablesStorage#writeStridMap(String, IStringMap, IEntityKeeper)} in
   * {@link ITsWorkroomStorage#ktorStorage()}.
   */
  private static final String SECTID_TASK_INPUT_PARAMS_MAP = SKIDE_ID + ".Enivironment.TaskManager.TaskInputs"; //$NON-NLS-1$

  private final SkideEnvironment                      skideEnv;
  private final IStridablesListEdit<IGenericTaskInfo> registeredTasks = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aEnv {@link SkideEnvironment} - the owner environment
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkideTaskManager( SkideEnvironment aEnv ) {
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
      IGenericTask task = u.listSupportedTasks().getByKey( aTaskId );
      ValidationResult vr = task.canRun( aInput );
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
      if( u.listSupportedTasks().hasKey( aTaskId ) ) {
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

  @Override
  public IOptionSet getTaskInputOptions( String aTaskId ) {
    IGenericTaskInfo taskInfo = registeredTasks.getByKey( aTaskId ); // also checks if aTaskId exists
    IKeepablesStorage ktorStorage = skideEnv.getSkideCoreStorage().ktorStorage();
    IStringMap<IOptionSet> insMap = ktorStorage.readStridMap( SECTID_TASK_INPUT_PARAMS_MAP, OptionSetKeeper.KEEPER );
    IOptionSet inOps = insMap.getByKey( aTaskId );
    if( inOps != null ) {
      return inOps;
    }
    return OptionSetUtils.initOptionSet( new OptionSet(), taskInfo.inOps() );
  }

  @Override
  public void setTaskInputOptions( String aTaskId, IOptionSet aTaskInputOps ) {
    IGenericTaskInfo taskInfo = registeredTasks.getByKey( aTaskId ); // also checks if aTaskId exists
    OptionSetUtils.checkOptionSet( aTaskInputOps, taskInfo.inOps() );
    IKeepablesStorage ktorStorage = skideEnv.getSkideCoreStorage().ktorStorage();
    IStringMap<IOptionSet> oldMap = ktorStorage.readStridMap( SECTID_TASK_INPUT_PARAMS_MAP, OptionSetKeeper.KEEPER );
    IStringMapEdit<IOptionSet> newMap = new StringMap<>( oldMap );
    newMap.put( aTaskId, aTaskInputOps );
    ktorStorage.writeStridMap( SECTID_TASK_INPUT_PARAMS_MAP, newMap, OptionSetKeeper.KEEPER );
  }

}
