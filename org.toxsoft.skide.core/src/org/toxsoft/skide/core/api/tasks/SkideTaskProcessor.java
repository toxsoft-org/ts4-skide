package org.toxsoft.skide.core.api.tasks;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.api.tasks.ISkResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * The SkIDE task processor provided by {@link ISkideTaskRegistrator#getRegisteredProcessors()}.
 * <p>
 * TODO comment about SkIDE tasks
 * <p>
 * TODO comment about SkIDE tasks
 *
 * @author hazard157
 */
public class SkideTaskProcessor
    implements ITsGuiContextable, ICloseable {

  /**
   * The ID of the section storing task input option values.
   * <p>
   * The map "task ID" - "input options set" is stored in {@link SkideEnvironment#papiGetSkideCoreStorage()} using
   * {@link IKeepablesStorage#writeStridMap(String, IStringMap, IEntityKeeper)} in
   * {@link ITsWorkroomStorage#ktorStorage()}.
   */
  private static final String SECTID_TASK_INPUT_PARAMS_MAP = SKIDE_ID + ".Enivironment.TaskManager.TaskInputs"; //$NON-NLS-1$

  private final IGenericTaskInfo taskInfo;

  private SkideEnvironment skideEnv  = null;
  private ITsGuiContext    tsContext = null;

  /**
   * Constructor for subclasses.
   *
   * @param aInfo {@link IGenericTaskInfo} - task info
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected SkideTaskProcessor( IGenericTaskInfo aInfo ) {
    TsNullArgumentRtException.checkNull( aInfo );
    taskInfo = aInfo;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    TsIllegalStateRtException.checkNull( tsContext );
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  /**
   * Internal method, must not be called by the clients.
   */
  @SuppressWarnings( "javadoc" )
  final public void papiInitialize( ITsGuiContext aContext, SkideEnvironment aSkEnv ) {
    TsInternalErrorRtException.checkNull( aSkEnv );
    TsInternalErrorRtException.checkNull( aContext );
    TsInternalErrorRtException.checkNoNull( skideEnv );
    TsInternalErrorRtException.checkNoNull( tsContext );
    skideEnv = aSkEnv;
    tsContext = aContext;
    doInititlize();
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  final public void close() {
    doClose();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the same environment as found in the application context.
   *
   * @return {@link ISkideEnvironment} - common environment for all plugins
   */
  final public ISkideEnvironment skEnv() {
    TsIllegalStateRtException.checkNull( skideEnv );
    return skideEnv;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the meta-information about task to run.
   *
   * @return {@link IGenericTaskInfo} - the task information
   */
  final public IGenericTaskInfo taskInfo() {
    return taskInfo;
  }

  /**
   * Runs (starts) the task for the specified SkIDE units.
   * <p>
   * Runs task for all units sequentially one after another, starting next unit only when previous is finished
   *
   * @param aUnitIds {@link IStringList} - list of SkIDE unit IDs to run task for
   * @param aCallback {@link ILongOpProgressCallback} - execution process visualizer
   * @return {@link IStringMap}&lt;{@link ITsContextRo}&gt; - map "unit ID" - "finished task result"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #canRun(IStringList)}
   */
  final public IStringMap<ITsContextRo> runSyncSequentially( IStringList aUnitIds, ILongOpProgressCallback aCallback ) {
    TsNullArgumentRtException.checkNulls( aUnitIds, aCallback );
    TsValidationFailedRtException.checkError( canRun( aUnitIds ) );
    ITsContext input = new TsContext();
    input.params().setAll( getTaskInputOptions() );
    REFDEF_IN_PROGRESS_MONITOR.setRef( input, aCallback );
    doBeforeRunTask( input );
    IStringMapEdit<ITsContextRo> resultsMap;
    try {
      resultsMap = new StringMap<>();
      for( ISkideUnit skUnit : skEnv().taskRegistrator().listCapableUnits( taskInfo.id() ) ) {
        IGenericTask task = skUnit.listSupportedTasks().getByKey( taskInfo.id() );
        ITsContextRo output = task.runSync( input );
        resultsMap.put( skUnit.id(), output );
      }
    }
    finally {
      doAfterRunTask( input );
    }
    return resultsMap;
  }

  /**
   * Checks if task can be started.
   * <p>
   * Checks that task ID is registered, there is at least one unit in
   * {@link ISkideTaskRegistrator#listCapableUnits(String)} and inout options (not references!) are valid.
   *
   * @param aUnitIds {@link IStringList} - list of SkIDE unit IDs to run task for
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  final public ValidationResult canRun( IStringList aUnitIds ) {
    TsNullArgumentRtException.checkNull( aUnitIds );
    // create and check input
    ITsContext input = new TsContext();
    input.params().setAll( getTaskInputOptions() );
    ValidationResult vr = OptionSetUtils.validateOptionSet( input.params(), taskInfo.inOps() );
    if( vr.isError() ) {
      return vr;
    }
    // check all specified units exists and supports this task
    IListEdit<IGenericTask> runTasks = new ElemArrayList<>();
    for( String unitId : aUnitIds ) {
      ISkideUnit unit = skEnv().pluginsRegistrator().listUnits().findByKey( unitId );
      if( unit != null ) {
        AbstractSkideUnitTask task = unit.listSupportedTasks().findByKey( taskInfo.id() );
        if( task == null ) {
          return ValidationResult.error( FMT_ERR_UNIT_NOT_SUPPRTS_TASK, unit.nmName(), taskInfo.nmName() );
        }
        runTasks.add( task );
      }
      else {
        return ValidationResult.error( FMT_ERR_NON_REGISTERED_UNIT_ID, unitId );
      }
    }
    // check there is at least one runnable unit
    if( runTasks.isEmpty() ) {
      return ValidationResult.error( STR_NO_TASK_CAPABLE_UNITS, taskInfo.nmName() );
    }
    return doCanRun( input );
  }

  /**
   * Returns saved input parameters of the task.
   * <p>
   * If task input was not saved yet then returns the default values as defined by {@link IGenericTaskInfo#inOps()}.
   *
   * @return {@link IOptionSet} - the task input parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public IOptionSet getTaskInputOptions() {
    IKeepablesStorage ktorStorage = skideEnv.papiGetSkideCoreStorage().ktorStorage();
    IStringMap<IOptionSet> insMap = ktorStorage.readStridMap( SECTID_TASK_INPUT_PARAMS_MAP, OptionSetKeeper.KEEPER );
    IOptionSet inOps = insMap.findByKey( taskInfo.id() );
    if( inOps != null ) {
      return inOps;
    }
    return OptionSetUtils.initOptionSet( new OptionSet(), taskInfo.inOps() );
  }

  /**
   * Saves to the permanent storage the task input parameters.
   *
   * @param aTaskInputOps {@link IOptionSet} - the task input parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed check of input options vs {@link IGenericTaskInfo#inOps()}
   */
  public void setTaskInputOptions( IOptionSet aTaskInputOps ) {
    OptionSetUtils.checkOptionSet( aTaskInputOps, taskInfo.inOps() );
    IKeepablesStorage ktorStorage = skideEnv.papiGetSkideCoreStorage().ktorStorage();
    IStringMap<IOptionSet> oldMap = ktorStorage.readStridMap( SECTID_TASK_INPUT_PARAMS_MAP, OptionSetKeeper.KEEPER );
    IStringMapEdit<IOptionSet> newMap = new StringMap<>( oldMap );
    newMap.put( taskInfo.id(), aTaskInputOps );
    ktorStorage.writeStridMap( SECTID_TASK_INPUT_PARAMS_MAP, newMap, OptionSetKeeper.KEEPER, true );
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Implementation may prepare input and perform other activities before run the task.
   * <p>
   * For example, implementation may open file or connection and put references to the task input.
   * <p>
   * When method is called <code>aInput</code> already contains:
   * <ul>
   * <li>mandatory Generic Task options and references as declared in {@link IGenericTaskConstants};</li>
   * <li>user-configured options as declared by {@link IGenericTaskInfo#inOps()};</li>
   * <li>all SkIDE specific options and references.</li>
   * </ul>
   * In the base class does nothing, no need to call parent method when overriding.
   *
   * @param aInput {@link ITsContext} - the task input to edit
   */
  protected void doBeforeRunTask( ITsContext aInput ) {
    // nop
  }

  /**
   * Implementation may perform clean-up after task finished.
   * <p>
   * Argument is the same task input as passed to {@link #doBeforeRunTask(ITsContext)} In the base class does nothing,
   * no need to call parent method when overriding.
   * <p>
   * In the base class does nothing, no need to call parent method when overriding.
   *
   * @param aInput {@link ITsContext} - the task input
   */
  protected void doAfterRunTask( ITsContextRo aInput ) {
    // nop
  }

  /**
   * Subclass may perform additional initialization.
   * <p>
   * The environments {@link #skEnv()} and {@link #tsContext} are initialized.
   * <p>
   * Does nothing in the base class so there is no need to call superclass method when overriding.
   */
  protected void doInititlize() {
    // nop
  }

  /**
   * Subclass may perform addition cluen-up when SkIDE application finishes.
   * <p>
   * Called from the method {@link #close()} the plugin closes.
   * <p>
   * Note: this method is called only for successfully initialized tasks.
   * <p>
   * Does nothing in the base class so there is no need to call superclass method when overriding.
   */
  protected void doClose() {
    // nop
  }

  /**
   * Subclass may perform additional checks for method {@link #canRun(IStringList)}.
   * <p>
   * Note: input is prepared with input options, but it does <b>not</b> contains the options and references added before
   * actual task run in {@link #doBeforeRunTask(ITsContext)}.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aInput {@link ITsContextRo} - input prepared with options
   * @return {@link ValidationResult} - the check result
   */
  protected ValidationResult doCanRun( ITsContextRo aInput ) {
    return ValidationResult.SUCCESS;
  }

}
