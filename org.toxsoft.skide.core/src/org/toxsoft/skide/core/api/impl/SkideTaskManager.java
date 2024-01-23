package org.toxsoft.skide.core.api.impl;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.api.impl.ISkResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
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
import org.toxsoft.core.tslib.utils.*;
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

  private final SkideEnvironment skideEnv;

  private final IStridablesListEdit<IGenericTaskInfo>     registeredTasks = new StridablesList<>();
  private final IStringMapEdit<ISkideTaskInputPreparator> preparatorsMap  = new StringMap<>();

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

  private ITsContext createPreparedITaskInput( String aTaskId, ITsGuiContext aWinContext ) {
    IGenericTaskInfo taskInfo = registeredTasks.getByKey( aTaskId );
    ITsGuiContext input = new TsGuiContext( aWinContext );
    input.params().setAll( getTaskInputOptions( taskInfo.id() ) );
    REFDEF_IN_PROGRESS_MONITOR.setRef( input, ILongOpProgressCallback.CONSOLE );
    ISkideTaskInputPreparator inputPreparator = preparatorsMap.getByKey( aTaskId );
    inputPreparator.prepareTaskInput( input, skideEnv, aWinContext );
    return input;

  }

  // ------------------------------------------------------------------------------------
  // ISkideGenericTaskManager
  //

  @Override
  public IStridablesList<IGenericTaskInfo> listTasks() {
    return registeredTasks;
  }

  @Override
  public ValidationResult canRun( String aTaskId, ITsGuiContext aWinContext ) {
    TsNullArgumentRtException.checkNulls( aTaskId, aWinContext );
    // check for registered task
    IGenericTaskInfo taskInfo = registeredTasks.findByKey( aTaskId );
    if( taskInfo == null ) {
      return ValidationResult.error( STR_UNKNOWN_TASK_ID, aTaskId );
    }
    // create and check input
    ITsContext input = createPreparedITaskInput( aTaskId, aWinContext );
    ValidationResult vr = GenericTaskUtils.validateInput( taskInfo, input );
    if( vr.isError() ) {
      return vr;
    }
    // check there is at least one capable unit
    IStridablesList<ISkideUnit> capableUnits = listCapableUnits( aTaskId );
    if( capableUnits.isEmpty() ) {
      return ValidationResult.error( STR_NO_TASK_CAPABLE_UNITS, taskInfo.nmName() );
    }
    // check units can run task
    for( ISkideUnit u : capableUnits ) {
      IGenericTask task = u.listSupportedTasks().getByKey( aTaskId );
      vr = ValidationResult.firstNonOk( vr, task.canRun( input ) );
      if( vr.isError() ) {
        return vr;
      }
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  public IStringMap<ITsContextRo> runSyncSequentially( String aTaskId, ITsGuiContext aWinContext,
      ILongOpProgressCallback aCallback ) {
    TsNullArgumentRtException.checkNull( aCallback );
    TsValidationFailedRtException.checkError( canRun( aTaskId, aWinContext ) );
    ITsContext input = createPreparedITaskInput( aTaskId, aWinContext );
    REFDEF_IN_PROGRESS_MONITOR.setRef( input, aCallback );
    IStringMapEdit<ITsContextRo> resultsMap = new StringMap<>();
    for( ISkideUnit skUnit : listCapableUnits( aTaskId ) ) {
      IGenericTask task = skUnit.listSupportedTasks().getByKey( aTaskId );
      ITsContextRo output = task.runSync( input );
      resultsMap.put( aTaskId, output );
    }
    return resultsMap;
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
  public void registerTask( IGenericTaskInfo aTaskInfo, ISkideTaskInputPreparator aInputPreparator ) {
    TsNullArgumentRtException.checkNulls( aTaskInfo, aInputPreparator );
    TsItemAlreadyExistsRtException.checkTrue( registeredTasks.hasKey( aTaskInfo.id() ) );
    registeredTasks.add( aTaskInfo );
    preparatorsMap.put( aTaskInfo.id(), aInputPreparator );
  }

  @Override
  public IOptionSet getTaskInputOptions( String aTaskId ) {
    IGenericTaskInfo taskInfo = registeredTasks.getByKey( aTaskId ); // also checks if aTaskId exists
    IKeepablesStorage ktorStorage = skideEnv.getSkideCoreStorage().ktorStorage();
    IStringMap<IOptionSet> insMap = ktorStorage.readStridMap( SECTID_TASK_INPUT_PARAMS_MAP, OptionSetKeeper.KEEPER );
    IOptionSet inOps = insMap.findByKey( aTaskId );
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
    ktorStorage.writeStridMap( SECTID_TASK_INPUT_PARAMS_MAP, newMap, OptionSetKeeper.KEEPER, true );
  }

}
