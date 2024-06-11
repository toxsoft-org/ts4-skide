package org.toxsoft.skide.core.gui.tasks;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.gui.tasks.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.core.api.tasks.*;

/**
 * Right panel for {@link ISkideUnit} to configure and run the specified task.
 * <p>
 * Current implementation contains tab folder with tabs:
 * <ul>
 * <li>Run - runs the task and displays execution log using {@link PanelSkideTaskRunner};</li>
 * <li>Configure - allows to set up the task using {@link PanelSkideTaskConfig}.</li>
 * <li>Results - displays task result using {@link PanelSkideTaskResults};</li>
 * </ul>
 *
 * @author hazard157
 */
public class SkideUnitTaskPanel
    extends AbstractSkideUnitPanel {

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
      String s = String.format( "\n%s", aName ); //$NON-NLS-1$
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

  private static final String ACTID_RUN_SKIDE_TASK       = SKIDE_FULL_ID + ".act.runTask";       //$NON-NLS-1$
  private static final String ACTID_CONFIGURE_SKIDE_TASK = SKIDE_FULL_ID + ".act.configureTask"; //$NON-NLS-1$

  private static final ITsActionDef ACDEF_RUN_SKIDE_TASK = TsActionDef.ofPush2( ACTID_RUN_SKIDE_TASK,
      STR_RUN_SKIDE_TASK, STR_RUN_SKIDE_TASK_D, ITsStdIconIds.ICONID_GO_NEXT_VIEW_PAGE );

  private static final ITsActionDef ACDEF_CONFIGURE_SKIDE_TASK = TsActionDef.ofPush2( ACTID_CONFIGURE_SKIDE_TASK,
      STR_CONFIGURE_SKIDE_TASK, STR_CONFIGURE_SKIDE_TASK_D, ITsStdIconIds.ICONID_DOCUMENT_PROPERTIES );

  /**
   * Toolbar actions.
   *
   * @author hazard157
   */
  class AspLocalActions
      extends MethodPerActionTsActionSetProvider {

    public AspLocalActions() {
      defineAction( ACDEF_RUN_SKIDE_TASK, this::doRun, this::doCanRun );
      defineAction( ACDEF_CONFIGURE_SKIDE_TASK, this::doConfigure, this::canConfigure );
      defineSeparator();
      defineAction( ACDEF_CLEAR, this::doClear, this::canClear );
    }

    void doRun() {
      TsIllegalStateRtException.checkFalse( doCanRun() );
      logText.append( "\n" ); //$NON-NLS-1$
      logText.append( String.format( FMT_TASK_STARTED, taskProcessor.taskInfo().nmName() ) );
      try {
        IStringMap<ITsContextRo> result = taskProcessor.runSyncSequentially( new TaskCallback() );
        logText.append( "\n" ); //$NON-NLS-1$
        if( result == null ) {
          logText.append( String.format( MSG_TASK_CANCELED ) );
        }
        else {
          logText.append( String.format( MSG_TASK_FINISHED ) );
        }
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

    void doConfigure() {
      DialogTaskRunConfiguration.edit( tsContext(), taskProcessor, true );
    }

    boolean canConfigure() {
      return taskProcessor != null;
    }

    void doClear() {
      logText.setText( EMPTY_STRING );
    }

    boolean canClear() {
      return !logText.getText().isEmpty();
    }

  }

  private final ITsActionSetProvider aspLocal = new AspLocalActions();

  private final ISkideEnvironment     skideEnv;
  private final ISkideTaskRegistrator taskReg;
  private final SkideTaskProcessor    taskProcessor;

  private TsToolbar toolbar;
  private Text      logText;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aUnit {@link ISkideUnit} - the project unit, creator of the panel
   * @param aTaskId String - the ID of the task to configure and run
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException there is no such task in {@link ISkideTaskRegistrator#getRegisteredProcessors()}
   */
  public SkideUnitTaskPanel( ITsGuiContext aContext, ISkideUnit aUnit, String aTaskId ) {
    super( aContext, aUnit );
    skideEnv = tsContext().get( ISkideEnvironment.class );
    taskReg = skideEnv.taskRegistrator();
    taskProcessor = skEnv().taskRegistrator().getRegisteredProcessors().getByKey( aTaskId );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkideUnitPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    TsComposite board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    // toolbar
    toolbar = TsToolbar.create( board, tsContext(), aspLocal.listAllActionDefs() );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( aspLocal );
    // logText
    logText = new Text( board, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY );
    logText.setLayoutData( BorderLayout.CENTER );
    //
    aspLocal.actionsStateEventer().addListener( src -> updateActionsState() );
    updateActionsState();
    return board;
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
    IStringList unitIds = taskProcessor.getTaskUnitIds();
    IOptionSet inOps = taskProcessor.getTaskInputOptions();
    return taskProcessor.canRun( unitIds, inOps );
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

}
