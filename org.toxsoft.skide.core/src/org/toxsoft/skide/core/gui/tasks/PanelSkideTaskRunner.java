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
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;

/**
 * Panel to run and monitor the execution of the specified task.
 *
 * @author hazard157
 */
public class PanelSkideTaskRunner
    extends TsPanel {

  /**
   * {@link ILongOpProgressCallback} to show task execution process in {@link PanelSkideTaskRunner#logText}.
   *
   * @author hazard157
   */
  private class TaskCallback
      implements ILongOpProgressCallback {

    private boolean undefined = false;

    public TaskCallback() {
      // nop
    }

    @Override
    public boolean startWork( String aName, boolean aUndefined ) {
      String s = String.format( "\n%s" ); //$NON-NLS-1$
      logText.append( s );
      undefined = aUndefined;
      return false;
    }

    @Override
    public boolean updateWorkProgress( String aName, double aWorkedPercents ) {
      String s;
      if( undefined ) {
        s = String.format( "\n%s", aName, Double.valueOf( aWorkedPercents ) ); //$NON-NLS-1$
      }
      else {
        s = String.format( "\n%s - %.2f", aName, Double.valueOf( aWorkedPercents ) ); //$NON-NLS-1$
      }
      logText.append( s );
      return false;
    }

    @Override
    public void finished( ValidationResult aStatus ) {
      String s;
      if( aStatus.isOk() ) {
        s = "\n" + aStatus.message(); //$NON-NLS-1$
      }
      else {
        s = String.format( "\n%s: %s", aStatus.type().nmName(), aStatus.message() ); //$NON-NLS-1$
      }
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
      logText.append( "\n" ); //$NON-NLS-1$
      logText.append( String.format( FMT_TASK_STARTED, taskProcessor.taskInfo().nmName() ) );
      try {
        IStringList unitIds = taskReg.listCapableUnits( taskProcessor.taskInfo().id() ).ids();
        taskProcessor.runSyncSequentially( tsContext(), unitIds, new TaskCallback() );
        logText.append( "\n" ); //$NON-NLS-1$
        logText.append( String.format( MSG_TASK_FINISHED ) );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
        logText.append( "\n" ); //$NON-NLS-1$
        logText.append( String.format( FMT_TASK_FAILED, ex.getMessage() ) );
      }
      logText.append( "\n" ); //$NON-NLS-1$
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

  private final ISkideEnvironment     skideEnv;
  private final ISkideTaskRegistrator taskReg;

  private SkideTaskProcessor taskProcessor = null;

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
    taskReg = skideEnv.taskRegistrator();
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
    ValidationResult vr = validateCanRun();
    if( !vr.isOk() ) {
      logText.append( vr.message() );
    }
  }

  private ValidationResult validateCanRun() {
    if( taskProcessor == null ) {
      return ValidationResult.error( MSG_ERR_NO_TASK_TO_RUN );
    }
    IStringList unitIds = taskReg.listCapableUnits( taskProcessor.taskInfo().id() ).ids();
    return taskProcessor.canRun( tsContext(), unitIds );
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
    return taskProcessor != null ? taskProcessor.taskInfo().id() : null;
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
    taskProcessor = taskReg.getRegisteredProcessors().getByKey( aTaskId );
    refreshPanel();
  }

}
