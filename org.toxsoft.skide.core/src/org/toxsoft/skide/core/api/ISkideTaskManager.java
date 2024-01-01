package org.toxsoft.skide.core.api;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages the tasks to be executed by SkIDE units {@link ISkideUnit#listTaskRunners()}.
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
   * Runs (starts) the task for all SkIDE units.
   * <p>
   * Runs task for all units sequentially one after another, starting next unit only when previous is finished. Units
   * with {@link IGenericTaskRunner#canRun(ITsContextRo)} returning error are omitted. Method may return an empty map if
   * no unit declares specified task or if all declaring units can not run the task.
   *
   * @param aTaskId String - the ID of the task to run
   * @param aInput {@link ITsContextRo} - the task input (options and references)
   * @return {@link IStringMap}&lt;{@link IGenericTask}&gt; - map "unit ID" - "finished task"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException the task with the specified ID is not registered
   * @throws TsValidationFailedRtException failed {@link GenericTaskUtils#validateInput(IGenericTaskInfo, ITsContextRo)}
   */
  IStringMap<IGenericTask> runSyncSequentially( String aTaskId, ITsContextRo aInput );

  // TODO IStringMap<ITsContextRo> runAsyncSequentially( String aTaskId, ITsContextRo aInput );

  /**
   * Returns the SkIDE plugins capable to run specified task.
   * <p>
   * Returns the plugins having task ID declared in the keys of the map {@link ISkideUnit#listTaskRunners()}.
   *
   * @param aTaskId String - the task ID
   * @return {@link IStridablesList}&lt;{@link ISkideUnit}&gt; - list of units declaring task as runnable
   */
  IStridablesList<ISkideUnit> listCapableUnits( String aTaskId );

  /**
   * Registers the task.
   *
   * @param aTaskInfo {@link IGenericTaskInfo} - the task to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException the task with the same ID is already registered
   */
  void registerTask( IGenericTaskInfo aTaskInfo );

}
