package org.toxsoft.skide.core.api;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;

/**
 * The means to prepare task input.
 * <p>
 * Each registered need own means to prepare task specific options and references of the input context. This interface
 * is provided by the task declarer when registering the task by the method
 * {@link ISkideTaskManager#registerTask(IGenericTaskInfo, ISkideTaskInputPreparator)}.
 *
 * @author hazard157
 */
public interface ISkideTaskInputPreparator {

  /**
   * The singleton doing nothing.
   */
  ISkideTaskInputPreparator NONE = ( aInput, aSkideEnv, aWinContext ) -> { /* nop */ };

  /**
   * Implementation may add necessary options and references to the task input.
   * <p>
   * When method is called <code>aInput</code> already contains:
   * <ul>
   * <li>common Generic Task options and references as declared in {@link IGenericTaskConstants};</li>
   * <li>user-configured options as declared by {@link IGenericTaskInfo#inOps()};</li>
   * <li>all SkIDE specific options and references.</li>
   * </ul>
   * Main purpose of theis method is to fill input with references because options are already filled.
   *
   * @param aInput {@link ITsContext} - the task input to edit
   * @param aSkideEnv {@link ISkideEnvironment} - the SkIDE environment
   * @param aWinContext {@link ITsGuiContext} - the windows level application context
   */
  void prepareTaskInput( ITsContext aInput, ISkideEnvironment aSkideEnv, ITsGuiContext aWinContext );

}
