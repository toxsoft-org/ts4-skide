package org.toxsoft.skide.core.e4.handlers;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.l10n.ISkideCoreSharedResources.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tsgui.dialogs.datarec.TsDialogInfo;
import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tsgui.m5.IM5Model;
import org.toxsoft.core.tsgui.m5.gui.M5GuiUtils;
import org.toxsoft.core.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.core.tsgui.m5.model.impl.M5LifecycleManager;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.skide.core.ISkideCoreConstants;
import org.toxsoft.skide.core.api.ISkideEnvironment;
import org.toxsoft.skide.core.api.ISkideUnit;
import org.toxsoft.skide.core.api.tasks.ISkideTaskRegistrator;
import org.toxsoft.skide.core.gui.SkideGuiUtils;
import org.toxsoft.skide.core.gui.m5.SkideUnitM5Model;

import jakarta.inject.Named;

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
    // determine which task to run: either specified in argument or selected from the list
    String taskId = aTaskId;
    if( taskId == null ) {
      taskId = SkideGuiUtils.selectTask( aEclipseContext );
      if( taskId == null ) {
        return;
      }
    }
    // select units to run
    IStridablesList<ISkideUnit> capableUnits = aSkideEnv.taskRegistrator().listCapableUnits( taskId );
    if( capableUnits.isEmpty() ) {
      TsDialogUtils.warn( shell, FMT_WARN_NO_UNITS_TO_RUN_TASK, taskId );
      return;
    }
    IM5Domain m5 = aEclipseContext.get( IM5Domain.class );
    IM5Model<ISkideUnit> unitModel = m5.getModel( SkideUnitM5Model.MODEL_ID, ISkideUnit.class );
    IM5LifecycleManager<ISkideUnit> lm =
        new SkideUnitOfTaskM5LifecycleManager( unitModel, aSkideEnv.taskRegistrator(), taskId );
    ITsGuiContext ctx = new TsGuiContext( aEclipseContext );
    TsDialogInfo di = new TsDialogInfo( ctx, DLG_SELECT_TASK_UNITS, DLG_SELECT_TASK_UNITS_D );
    IList<ISkideUnit> unitsList =
        M5GuiUtils.askSelectItemsList( di, unitModel, lm.itemsProvider().listItems(), lm.itemsProvider() );
    if( unitsList == null ) {
      return;
    }
    // TODO run the units

    // TODO CmdSkideRunTask.exec()
    TsDialogUtils.underDevelopment( shell );
  }

  @CanExecute
  boolean canExec( ISkideEnvironment aSkideEnv ) {
    return !aSkideEnv.taskRegistrator().getRegisteredProcessors().isEmpty();
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
      extends M5LifecycleManager<ISkideUnit, ISkideTaskRegistrator> {

    private final String taskId;

    public SkideUnitOfTaskM5LifecycleManager( IM5Model<ISkideUnit> aModel, ISkideTaskRegistrator aMaster,
        String aTaskId ) {
      super( aModel, false, false, false, true, aMaster );
      taskId = StridUtils.checkValidIdPath( aTaskId );
    }

    @Override
    protected IList<ISkideUnit> doListEntities() {
      return master().listCapableUnits( taskId );
    }

  }
}
