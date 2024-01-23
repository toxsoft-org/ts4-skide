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
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skide.core.api.*;

/**
 * Panel to run and monitor the execution of the specified task.
 *
 * @author hazard157
 */
public class PanelSkideTaskRunner
    extends TsPanel {

  private class TaskCallback
      implements ILongOpProgressCallback {

    private boolean undefined = false;

    public TaskCallback() {
      // nop
    }

    @Override
    public boolean startWork( String aName, boolean aUndefined ) {
      String s = String.format( "\n%s" );
      logText.append( s );
      undefined = aUndefined;
      return false;
    }

    @Override
    public boolean updateWorkProgress( String aName, double aWorkedPercents ) {
      String s;
      if( undefined ) {
        s = String.format( "\n%s", aName, aWorkedPercents );
      }
      else {
        s = String.format( "\n%s - %.2f", aName, aWorkedPercents );
      }
      logText.append( s );
      return false;
    }

    @Override
    public void finished( ValidationResult aStatus ) {
      String s = String.format( "\n%s %s", aStatus.type().nmName(), aStatus.message() );
      logText.append( s );
    }

  }

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
      TsIllegalStateRtException.checkFalse( doCanRun() );
      ITsContextRo input = createTaskInput();
      logText.append( String.format( "\nSTARTED TASK: %s", taskInfo.nmName() ) );
      try {
        taskMan.runSyncSequentially( taskInfo.id(), input );
        logText.append( String.format( "\nTASK FINISHED.\n\n" ) );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
        logText.append( String.format( "\nTASK FAILED: %s.\n\n", ex.getMessage() ) );
      }
      updateActionsState();
    }

    boolean doCanRun() {
      return !validateCanRun().isError();
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
    //
    aspLocal.actionsStateEventer().addListener( src -> updateActionsState() );
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void updateActionsState() {
    for( String aid : aspLocal.listHandledActionIds() ) {
      toolbar.setActionEnabled( aid, aspLocal.isActionEnabled( aid ) );
      toolbar.setActionChecked( aid, aspLocal.isActionChecked( aid ) );
    }
    // TODO maybe place this code comwhere else?
    ValidationResult vr = validateCanRun();
    if( !vr.isOk() ) {
      logText.append( vr.message() );
    }
  }

  private ValidationResult validateCanRun() {
    if( taskInfo == null ) {
      return ValidationResult.error( MSG_ERR_NO_TASK_TO_RUN );
    }
    IStridablesList<ISkideUnit> units = taskMan.listCapableUnits( taskInfo.id() );
    if( units.isEmpty() ) {
      return ValidationResult.error( MSG_ERR_NO_CAPABLE_UNITS );
    }
    ITsContextRo input = createTaskInput();
    ValidationResult vr = GenericTaskUtils.validateInput( taskInfo, input );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.SUCCESS;
  }

  private ITsContextRo createTaskInput() {
    ITsGuiContext input = new TsGuiContext( tsContext() );
    input.params().setAll( taskMan.getTaskInputOptions( taskInfo.id() ) );
    IGenericTaskConstants.REFDEF_IN_PROGRESS_MONITOR.setRef( input, new TaskCallback() );
    ISkideTaskInputPreparator inputPreparator = taskMan.getInputPreparator( taskInfo.id() );
    inputPreparator.prepareTaskInput( input, skideEnv, tsContext() );
    return input;
  }

  private void refreshPanel() {
    aspLocal.handleAction( ACTID_CLEAR );
    updateActionsState();
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
