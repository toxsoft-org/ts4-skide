package org.toxsoft.skide.core.gui.tasks;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.gui.tasks.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.utils.errors.*;
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

  // TODO use settings from ITsHdpiService
  private static final EIconSize TAB_ICON_SIZE = EIconSize.IS_24X24;

  private final SkideTaskProcessor taskProcessor;

  private PanelSkideTaskRunner  runnerPanel;
  private PanelSkideTaskConfig  configPanel;
  private PanelSkideTaskResults resultsPanel;

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
    taskProcessor = skEnv().taskRegistrator().getRegisteredProcessors().getByKey( aTaskId );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkideUnitPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    TabFolder tfMain = new TabFolder( aParent, SWT.TOP );
    // add runner item
    runnerPanel = new PanelSkideTaskRunner( tfMain, tsContext() );
    TabItem tiRunner = new TabItem( tfMain, SWT.NONE );
    tiRunner.setText( STR_TABL_TASK_RUNNER );
    tiRunner.setToolTipText( STR_TABL_TASK_RUNNER_D );
    tiRunner.setImage( iconManager().loadStdIcon( ICONID_TASK_RUN, TAB_ICON_SIZE ) );
    tiRunner.setControl( runnerPanel );
    // add configuration item
    configPanel = new PanelSkideTaskConfig( tfMain, tsContext() );
    TabItem tiConfig = new TabItem( tfMain, SWT.NONE );
    tiConfig.setText( STR_TABL_TASK_CONFIG );
    tiConfig.setToolTipText( STR_TABL_TASK_CONFIG_D );
    tiConfig.setImage( iconManager().loadStdIcon( ICONID_TASK_CONFIG, TAB_ICON_SIZE ) );
    tiConfig.setControl( configPanel );
    // add results item
    resultsPanel = new PanelSkideTaskResults( tfMain, tsContext() );
    TabItem tiResults = new TabItem( tfMain, SWT.NONE );
    tiResults.setText( STR_TABL_TASK_RESULTS );
    tiResults.setToolTipText( STR_TABL_TASK_RESULTS_D );
    tiResults.setImage( iconManager().loadStdIcon( ICONID_TASK_RESULTS, TAB_ICON_SIZE ) );
    tiResults.setControl( resultsPanel );
    // setup
    runnerPanel.setSkideTaskId( taskProcessor.taskInfo().id() );
    configPanel.setSkideTaskId( taskProcessor.taskInfo().id() );
    resultsPanel.setSkideTaskId( taskProcessor.taskInfo().id() );
    return tfMain;
  }

}
