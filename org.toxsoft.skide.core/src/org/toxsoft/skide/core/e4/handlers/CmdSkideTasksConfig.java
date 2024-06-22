package org.toxsoft.skide.core.e4.handlers;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.skide.core.ISkideCoreConstants;
import org.toxsoft.skide.core.api.ISkideEnvironment;
import org.toxsoft.skide.core.gui.SkideGuiUtils;

import jakarta.inject.Named;

/**
 * Command: invoke the specified task configuration dialog or select and configure task if none specified.
 * <p>
 * Command ID: {@link ISkideCoreConstants#CMDID_SKIDE_CONFIGURE_TASK}.<br>
 * Arg ID (optional): {@link ISkideCoreConstants#CMDARGID_SKIDE_CFG_TASK_ID} the ID of the task to configure
 *
 * @author hazard157
 */
public class CmdSkideTasksConfig {

  @Execute
  void exec( @Named( CMDARGID_SKIDE_CFG_TASK_ID ) @Optional String aTaskId, ISkideEnvironment aSkideEnv,
      IEclipseContext aEclipseContext ) {
    Shell shell = aEclipseContext.get( Shell.class );
    // determine which task to configure: either specified in argument or selected from the list
    String taskId = aTaskId;
    if( taskId == null ) {
      taskId = SkideGuiUtils.selectTask( aEclipseContext );
      if( taskId == null ) {
        return;
      }
    }
    // select units to run
    // IStridablesList<ISkideUnit> capableUnits = aSkideEnv.taskManager().listCapableUnits( taskId );
    // if( capableUnits.isEmpty() ) {
    // TsDialogUtils.warn( shell, FMT_WARN_NO_UNITS_TO_RUN_TASK, taskId );
    // return;
    // }
    // IM5Domain m5 = aEclipseContext.get( IM5Domain.class );
    // IM5Model<ISkideUnit> unitModel = m5.getModel( SkideUnitM5Model.MODEL_ID, ISkideUnit.class );
    // IM5LifecycleManager<ISkideUnit> lm =
    // new SkideUnitOfTaskM5LifecycleManager( unitModel, aSkideEnv.taskManager(), taskId );
    // ITsGuiContext ctx = new TsGuiContext( aEclipseContext );
    // TsDialogInfo di = new TsDialogInfo( ctx, DLG_SELECT_TASK_UNITS, DLG_SELECT_TASK_UNITS_D );
    // IList<ISkideUnit> unitsList =
    // M5GuiUtils.askSelectItemsList( di, unitModel, lm.itemsProvider().listItems(), lm.itemsProvider() );
    // if( unitsList == null ) {
    // return;
    // }
    // TODO run the units

    // TODO CmdSkideRunTask.exec()
    TsDialogUtils.underDevelopment( shell );
  }

  @CanExecute
  boolean canExec( ISkideEnvironment aSkideEnv ) {
    return !aSkideEnv.taskRegistrator().getRegisteredProcessors().isEmpty();
  }

}
