package org.toxsoft.skide.core.api.tasks;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.api.tasks.ISkResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
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
import org.toxsoft.skide.core.gui.tasks.*;

/**
 * The SkIDE task processor implementation base class.
 * <p>
 * Registered task processor implementations are provided by {@link ISkideTaskRegistrator#getRegisteredProcessors()}.
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
   * <p>
   * {@link OptionSetKeeper#KEEPER} is used to store parameters.
   */
  private static final String SECTID_TASK_INPUT_PARAMS_MAP = SKIDE_ID + ".Enivironment.TaskManager.TaskInputs"; //$NON-NLS-1$

  /**
   * The ID of the section storing unit IDs of the tasks to run..
   * <p>
   * The map "task ID" - "unit IDs" is stored in {@link SkideEnvironment#papiGetSkideCoreStorage()} using
   * {@link IKeepablesStorage#writeStridMap(String, IStringMap, IEntityKeeper)} in
   * {@link ITsWorkroomStorage#ktorStorage()}.
   * <p>
   * {@link StringListKeeper#KEEPER} is used to store unit IDs.
   */
  private static final String SECTID_TASK_UNIT_IDS_MAP = SKIDE_ID + ".Enivironment.TaskManager.UnitIds"; //$NON-NLS-1$

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
   * Before task will started the confirmation dialog will appear. Dialog allows to edit list of units to run and to
   * change task input options. Edited values will be saved. Confirmation dialog may be bypassed if argument
   * <code>aDontAskConfig</code> is set to <code>true</code>.
   * <p>
   * If {@link #canRun(IStringList, IOptionSet)} fails, this method invokes message dialog and returns
   * <code>null</code>.
   * <p>
   * Runs task for all units sequentially one after another, starting next unit only when previous is finished
   * <p>
   * If user does not starts the task (cancel confirmation dialog) then method returns <code>null</code>.
   *
   * @param aCallback {@link ILongOpProgressCallback} - execution process visualizer
   * @param aDontAskConfig boolean - <code>true</code> to bypass unit IDs and input options editing
   * @return {@link IStringMap}&lt;{@link ITsContextRo}&gt; - map "unit ID" - "finished task result" or
   *         <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  final public IStringMap<ITsContextRo> runSyncSequentially( ILongOpProgressCallback aCallback,
      boolean aDontAskConfig ) {
    TsNullArgumentRtException.checkNull( aCallback );
    // ask run confirmation and edit units and input if needed
    if( !aDontAskConfig ) {
      PanelTaskRunConfiguration.Config cfg = DialogTaskRunConfiguration.edit( tsContext, this, true );
      if( cfg == null ) {
        return null;
      }
    }
    // check task can be run
    IStringList unitIds = getTaskUnitIds();
    IOptionSet inOps = getTaskInputOptions();
    ValidationResult vr = canRun( unitIds, inOps );
    if( TsDialogUtils.askContinueOnValidation( getShell(), vr, MSG_ASK_RUN_TASK_ON_WARNING ) != ETsDialogCode.YES ) {
      return null;
    }
    // prepare task input
    ITsContext input = new TsContext();
    input.params().setAll( inOps );
    REFDEF_IN_PROGRESS_MONITOR.setRef( input, aCallback );
    doBeforeRunTask( input );
    IStringMapEdit<ITsContextRo> resultsMap;
    // run the task units sequentially
    try {
      resultsMap = new StringMap<>();
      for( String unitId : unitIds ) {
        ISkideUnit skUnit = skEnv().pluginsRegistrator().listUnits().getByKey( unitId );
        IGenericTask task = skUnit.listSupportedTasks().getByKey( taskInfo.id() );
        ITsContextRo output = task.runSync( input );
        resultsMap.put( skUnit.id(), output );
      }
    }
    finally { // clean-up after task
      doAfterRunTask( input );
    }
    return resultsMap;
  }

  /**
   * Checks if task can be started.
   * <p>
   * Checks that task ID is registered, there is at least one unit in
   * {@link ISkideTaskRegistrator#listCapableUnits(String)} and input options <code>aInputParams</code> (not
   * references!) are valid.
   *
   * @param aUnitIds {@link IStringList} - list of SkIDE unit IDs to run task for
   * @param aInputParams {@link IOptionSet} - task input options
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  final public ValidationResult canRun( IStringList aUnitIds, IOptionSet aInputParams ) {
    TsNullArgumentRtException.checkNull( aUnitIds );
    // create and check input
    ITsContext input = new TsContext();
    input.params().setAll( aInputParams );
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
   * Returns saved list of the units to run.
   * <p>
   * If units list was not saved yet then returns all capable units
   * {@link ISkideTaskRegistrator#listCapableUnits(String)}.
   *
   * @return {@link IOptionSet} - the task input parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public IStringList getTaskUnitIds() {
    IKeepablesStorage ktorStorage = skideEnv.papiGetSkideCoreStorage().ktorStorage();
    IStringMap<IStringList> unitIdsMap = ktorStorage.readStridMap( SECTID_TASK_UNIT_IDS_MAP, StringListKeeper.KEEPER );
    IStringList unitIds = unitIdsMap.findByKey( taskInfo.id() );
    if( unitIds != null ) {
      return unitIds;
    }
    return skEnv().taskRegistrator().listCapableUnits( taskInfo.id() ).ids();
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

  /**
   * Saves to the permanent storage the list of units to run for task.
   *
   * @param aUnitIds {@link IStringList} - list of SkIDE unit IDs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public void setTaskUnitIdsToRun( IStringList aUnitIds ) {
    TsNullArgumentRtException.checkNull( aUnitIds );
    for( String s : aUnitIds ) {
      StridUtils.checkValidIdPath( s );
    }
    IKeepablesStorage ktorStorage = skideEnv.papiGetSkideCoreStorage().ktorStorage();
    IStringMap<IStringList> oldMap = ktorStorage.readStridMap( SECTID_TASK_UNIT_IDS_MAP, StringListKeeper.KEEPER );
    IStringMapEdit<IStringList> newMap = new StringMap<>( oldMap );
    newMap.put( taskInfo.id(), aUnitIds );
    ktorStorage.writeStridMap( SECTID_TASK_UNIT_IDS_MAP, newMap, StringListKeeper.KEEPER, true );
  }

  // TODO results panel to be provided by the task processor
  // TODO public IGenericEntityPanel<IStringMap<ITsContextRo>> createResultsPanel() {
  //
  // }

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
   * Subclass may perform additional checks for method {@link #canRun(IStringList, IOptionSet)}.
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
