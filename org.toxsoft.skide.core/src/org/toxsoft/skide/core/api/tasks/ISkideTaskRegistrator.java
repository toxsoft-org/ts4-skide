package org.toxsoft.skide.core.api.tasks;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;

/**
 * Manages registered SkIDE task processors.
 *
 * @author hazard157
 */
public interface ISkideTaskRegistrator {

  /**
   * Returns all registered task processors.
   *
   * @return {@link IStringMap}&lt;{@link SkideTaskProcessor}&gt; - map "task ID" - "the processor"
   */
  IStringMap<SkideTaskProcessor> getRegisteredProcessors();

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
   * Registers the task processor.
   *
   * @param aProcessor {@link SkideTaskProcessor} - the processor to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException the task with the same ID is already registered
   */
  void registerTaskProcessor( SkideTaskProcessor aProcessor );

}
