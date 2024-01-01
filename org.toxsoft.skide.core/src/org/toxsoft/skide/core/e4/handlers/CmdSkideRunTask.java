package org.toxsoft.skide.core.e4.handlers;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.l10n.ISkideCoreSharedResources.*;

import javax.inject.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.models.misc.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skide.core.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.gui.m5.*;
import org.toxsoft.uskat.core.api.evserv.*;

/**
 * Command: Run the specified task or select and run task if none specified.
 * <p>
 * Command ID: {@link ISkideCoreConstants#CMDID_SKIDE_RUN_TASK}.<br>
 * Arg ID (optional): {@link ISkideCoreConstants#CMDARGID_SKIDE_RUN_TASK_ID} the ID of the task to run
 *
 * @author hazard157
 */
public class CmdSkideRunTask {

  @Execute
  void exec( @Named( CMDARGID_SKIDE_RUN_TASK_ID ) @Optional String aTaskId, ISkideEnvironment aSkideEnv,
      IEclipseContext aEclipseContext ) {
    Shell shell = aEclipseContext.get( Shell.class );
    // task run: either specified in argument or selected from the list
    String taskId = aTaskId;
    if( taskId == null ) {
      taskId = selectTask( aSkideEnv, aEclipseContext );
      if( taskId == null ) {
        return;
      }
    }
    // select units to run
    IStridablesList<ISkideUnit> capableUnits = aSkideEnv.taskManager().listCapableUnits( taskId );
    if( capableUnits.isEmpty() ) {
      TsDialogUtils.warn( shell, FMT_WARN_NO_UNITS_TO_RUN_TASK, taskId );
      return;
    }
    IM5Domain m5 = aEclipseContext.get( IM5Domain.class );
    IM5Model<ISkideUnit> unitModel = m5.getModel( SkideUnitM5Model.MODEL_ID, ISkideUnit.class );
    IM5LifecycleManager<ISkideUnit> lm =
        new SkideUnitOfTaskM5LifecycleManager( unitModel, aSkideEnv.taskManager(), taskId );
    ITsGuiContext ctx = new TsGuiContext( aEclipseContext );
    TsDialogInfo di = new TsDialogInfo( ctx, DLG_SELECT_TASK_UNITS, DLG_SELECT_TASK_UNITS_D );
    IList<ISkideUnit> unitsList = M5GuiUtils.askSelectItemsList( di, unitModel, null, lm.itemsProvider() );
    if( unitsList == null ) {
      return;
    }
    // TODO run the units

    // TODO CmdSkideRunTask.exec()
    TsDialogUtils.underDevelopment( shell );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  /**
   * LM for {@link SkideUnitM5Model} to list units {@link ISkideUnit} capable to run the specified task.
   *
   * @author hazard157
   */
  static class SkideUnitOfTaskM5LifecycleManager
      extends M5LifecycleManager<ISkideUnit, ISkideTaskManager> {

    private final String taskId;

    public SkideUnitOfTaskM5LifecycleManager( IM5Model<ISkideUnit> aModel, ISkideTaskManager aMaster, String aTaskId ) {
      super( aModel, false, false, false, true, aMaster );
      taskId = StridUtils.checkValidIdPath( aTaskId );
    }

    @Override
    protected IList<ISkideUnit> doListEntities() {
      return master().listCapableUnits( taskId );
    }

  }

  /**
   * Invokes dialog to select the task to run from the {@link ISkideTaskManager#listTasks()}.
   * <p>
   * If no task is registered displays the warning message and returns <code>null</code>.
   *
   * @param aSkideEnv {@link ISkEventHandler} - SkIDE environment
   * @param aEclipseContext {@link IEclipseContext} - the context
   * @return String - selected task ID or <code>null</code>
   */
  private static String selectTask( ISkideEnvironment aSkideEnv, IEclipseContext aEclipseContext ) {
    if( aSkideEnv.taskManager().listTasks().isEmpty() ) {
      TsDialogUtils.warn( aEclipseContext.get( Shell.class ), MSG_NO_REGISTERED_SKIDE_TASK );
      return null;
    }
    ITsGuiContext ctx = new TsGuiContext( aEclipseContext );
    OPDEF_IS_TOOLBAR.setValue( ctx.params(), AV_FALSE );
    TsDialogInfo di = new TsDialogInfo( ctx, DLG_SELECT_TASK, DLG_SELECT_TASK_D );
    di.setMinSizeShellRelative( 30, 50 );
    IM5Domain m5 = aEclipseContext.get( IM5Domain.class );
    IM5Model<IGenericTaskInfo> model = m5.getModel( GenericTaskInfoM5Model.MODEL_ID, IGenericTaskInfo.class );
    IM5LifecycleManager<IGenericTaskInfo> lm =
        model.getLifecycleManager( (ITsItemsProvider<IGenericTaskInfo>)() -> aSkideEnv.taskManager().listTasks() );
    IGenericTaskInfo taskInfo = M5GuiUtils.askSelectItem( di, model, null, lm.itemsProvider(), null );
    if( taskInfo != null ) {
      return taskInfo.id();
    }
    return null;
  }

}
