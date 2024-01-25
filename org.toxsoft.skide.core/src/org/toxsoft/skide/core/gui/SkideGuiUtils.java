package org.toxsoft.skide.core.gui;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skide.core.l10n.ISkideCoreSharedResources.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.std.models.misc.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.core.api.tasks.*;

/**
 * Statis helper methods for SkIDE GUI.
 *
 * @author hazard157
 */
public class SkideGuiUtils {

  /**
   * Invokes dialog to select the task to run from the {@link ISkideTaskManager#listRegisteredSkideTasks()}.
   * <p>
   * If no task is registered displays the warning message and returns <code>null</code>.
   *
   * @param aEclipseContext {@link IEclipseContext} - the context
   * @return String - selected task ID or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String selectTask( IEclipseContext aEclipseContext ) {
    TsNullArgumentRtException.checkNull( aEclipseContext );
    ISkideEnvironment skideEnv = aEclipseContext.get( ISkideEnvironment.class );
    if( skideEnv.taskManager().listRegisteredSkideTasks().isEmpty() ) {
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
        model.getLifecycleManager( (ITsItemsProvider<IGenericTaskInfo>)() -> skideEnv.taskManager().listRegisteredSkideTasks() );
    IGenericTaskInfo taskInfo = M5GuiUtils.askSelectItem( di, model, null, lm.itemsProvider(), null );
    if( taskInfo != null ) {
      return taskInfo.id();
    }
    return null;
  }

  /**
   * No subclasses.
   */
  private SkideGuiUtils() {
    // nop
  }

}
