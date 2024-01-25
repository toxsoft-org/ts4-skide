package org.toxsoft.skide.core.api.tasks;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;

/**
 * {@link IGenericTask} implementation for SkIDE to be provided by {@link ISkideTaskManager#listRegisteredSkideTasks()}.
 * <p>
 * To execute the task, this implementation runs SkIDE unit task executors as returned by
 * {@link ISkideTaskManager#listCapableUnits(String)}.
 *
 * @author hazard157
 */
public class AbstractSkideTask
    extends AbstractGenericTask {

  protected AbstractSkideTask( IGenericTaskInfo aInfo ) {
    super( aInfo );
    // TODO Auto-generated constructor stub
  }

  @Override
  protected void doRunSync( ITsContextRo aInput, ITsContext aOutput ) {
    // TODO Auto-generated method stub

  }

  @Override
  protected Future<ITsContextRo> doRunAsync( ITsContextRo aInput, ITsContext aOutput ) {
    // TODO Auto-generated method stub
    return null;
  }

}
