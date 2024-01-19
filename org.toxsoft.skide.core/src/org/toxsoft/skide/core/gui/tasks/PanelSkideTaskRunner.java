package org.toxsoft.skide.core.gui.tasks;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.gui.tasks.ISkResources.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;

/**
 * Panel to run and monitor the execution of the specified task.
 *
 * @author hazard157
 */
public class PanelSkideTaskRunner
    extends TsPanel {

  private static final String ACTID_RUN_SKIDE_TASK = SKIDE_FULL_ID + ".act.runTask"; //$NON-NLS-1$

  private static final ITsActionDef ACDEF_RUN_SKIDE_TASK =
      TsActionDef.ofPush2( ACTID_RUN_SKIDE_TASK, STR_RUN_SKIDE_TASK, STR_RUN_SKIDE_TASK_D, ICONID_TASK_RUN );

  class AspLocalActions
      extends MethodPerActionTsActionSetProvider {

    public AspLocalActions() {
      defineAction( ACDEF_RUN_SKIDE_TASK, this::doRun, this::doCanRun );
      defineAction( ACDEF_CLEAR, this::doClear, this::doCanClear );
    }

    void doRun() {
      TsDialogUtils.underDevelopment( getShell() );
    }

    boolean doCanRun() {
      TsDialogUtils.underDevelopment( getShell() );
      return true;
    }

    void doClear() {
      logText.setText( EMPTY_STRING );
    }

    boolean doCanClear() {
      return !logText.getText().isEmpty();
    }

  }

  private final ITsActionSetProvider aspLocal = new AspLocalActions();

  private final TsToolbar toolbar;
  private final Text      logText;

  private final ISkideEnvironment skideEnv;
  private final ISkideTaskManager taskMan;
  private IGenericTaskInfo        taskInfo = null;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelSkideTaskRunner( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    skideEnv = tsContext().get( ISkideEnvironment.class );
    taskMan = skideEnv.taskManager();
    this.setLayout( new BorderLayout() );
    // toolbar
    toolbar = TsToolbar.create( this, tsContext(), aspLocal.listAllActionDefs() );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( aspLocal );
    // logText
    logText = new Text( this, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY );
    logText.setLayoutData( BorderLayout.CENTER );

    // TODO PanelSkideTaskRunner.PanelSkideTaskRunner()
    updateAcxtionsState();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void updateAcxtionsState() {
    // TODO PanelSkideTaskRunner.updateAcxtionsState()
  }

  private void refreshPanel() {
    // TODO PanelSkideTaskRunner.refreshPanel()
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns currently edited task ID
   *
   * @return String - the task ID or <code>null</code>
   */
  public String getSkideTaskId() {
    return taskInfo != null ? taskInfo.id() : null;
  }

  /**
   * Sets the task to edit configuration.
   *
   * @param aTaskId String - the task ID or <code>null</code>
   * @throws TsItemNotFoundRtException no task with specified ID found
   */
  public void setSkideTaskId( String aTaskId ) {
    if( Objects.equals( aTaskId, getSkideTaskId() ) ) {
      return;
    }
    taskInfo = taskMan.listTasks().getByKey( aTaskId );
    refreshPanel();
  }

}
