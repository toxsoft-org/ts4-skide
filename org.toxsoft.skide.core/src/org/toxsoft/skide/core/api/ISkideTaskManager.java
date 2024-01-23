package org.toxsoft.skide.core.api;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * Manages the tasks to be executed by SkIDE units.
 *
 * @author hazard157
 */
public interface ISkideTaskManager {

  /**
   * Returns all registered tasks.
   *
   * @return {@link IStridablesList}&lt;{@link IGenericTaskInfo}&gt; - list of registered tasks
   */
  IStridablesList<IGenericTaskInfo> listTasks();

  /**
   * Checks if task can be started.
   * <p>
   * Checks that task ID is registered, there is at least on unit in {@link #listCapableUnits(String)}, and no capable
   * unit method {@link AbstractSkideUnitTask#canRun(ITsContextRo)} returns error.
   *
   * @param aTaskId String - the ID of the task to run
   * @param aWinContext {@link ITsGuiContext} - windows level application context
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canRun( String aTaskId, ITsGuiContext aWinContext );

  /**
   * Runs (starts) the task for all SkIDE units.
   * <p>
   * Runs task for all units sequentially one after another, starting next unit only when previous is finished
   *
   * @param aTaskId String - the ID of the task to run
   * @param aWinContext {@link ITsGuiContext} - windows level application context
   * @param aCallback {@link ILongOpProgressCallback} - execution process visualizer
   * @return {@link IStringMap}&lt;{@link ITsContextRo}&gt; - map "unit ID" - "finished task result"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #canRun(String, ITsGuiContext)}
   */
  IStringMap<ITsContextRo> runSyncSequentially( String aTaskId, ITsGuiContext aWinContext,
      ILongOpProgressCallback aCallback );

  // TODO IStringMap<ITsContextRo> runAsyncSequentially( String aTaskId, ITsContextRo aInput );

  /**
   * Returns the SkIDE plugins capable to run specified task.
   * <p>
   * Returns the plugins having task ID declared in the keys of the map {@link ISkideUnit#listSupportedTasks()}.
   *
   * @param aTaskId String - the task ID
   * @return {@link IStridablesList}&lt;{@link ISkideUnit}&gt; - list of units declaring task as runnable
   */
  IStridablesList<ISkideUnit> listCapableUnits( String aTaskId );

  /**
   * Registers the task.
   *
   * @param aTaskInfo {@link IGenericTaskInfo} - the task to register
   * @param aInputPreparator {@link ISkideTaskInputPreparator} - the means to prepare task input
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException the task with the same ID is already registered
   */
  void registerTask( IGenericTaskInfo aTaskInfo, ISkideTaskInputPreparator aInputPreparator );

  /**
   * Returns saved input parameters of the specified task.
   * <p>
   * If task input was not saved yet then returns the default values as defined by {@link IGenericTaskInfo#inOps()}.
   *
   * @param aTaskId String - the task ID
   * @return {@link IOptionSet} - the task input parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such task is registered in {@link #listTasks()}
   */
  IOptionSet getTaskInputOptions( String aTaskId );

  /**
   * Saves to the permanent storage the task input parameters.
   *
   * @param aTaskId String - the task ID
   * @param aTaskInputOps {@link IOptionSet} - the task input parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such task is registered in {@link #listTasks()}
   * @throws TsValidationFailedRtException failed check of input options vs {@link IGenericTaskInfo#inOps()}
   */
  void setTaskInputOptions( String aTaskId, IOptionSet aTaskInputOps );

}
