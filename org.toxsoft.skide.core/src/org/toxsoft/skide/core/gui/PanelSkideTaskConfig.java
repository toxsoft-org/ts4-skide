package org.toxsoft.skide.core.gui;

import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;

/**
 * Specified SkIDE task configuration panel.
 * <p>
 * Configures one of the tasks listed in {@link ISkideTaskManager#listTasks()}. The configured task is determined by
 * {@link #getSkideTaskId()}.
 * <p>
 * Task configuration includes:
 * <ul>
 * <li>specifying the input parameters of the task as defined by {@link IGenericTaskInfo#inOps()};</li>
 * <li>setting configuration options {@link IGenericTask#cfgOptionValues()} for each task in
 * {@link ISkideTaskManager#listCapableUnits(String)} of the specified task.</li>
 * </ul>
 * <p>
 * All changes in the panel are immediately saved to the SkIDE project.
 *
 * @author hazard157
 */
public class PanelSkideTaskConfig
    extends TsPanel {

  private final ISkideEnvironment skideEnv;
  private final IOpsetsKitPanel   opKitPanel;

  private IGenericTaskInfo taskInfo = null;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelSkideTaskConfig( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    skideEnv = tsContext().get( ISkideEnvironment.class );
    this.setLayout( new BorderLayout() );
    opKitPanel = new OpsetsKitPanel( tsContext() );
    opKitPanel.createControl( this );
    opKitPanel.getControl().setLayoutData( BorderLayout.CENTER );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void refreshPanel() {

    // TODO реализовать PanelSkideTaskConfig.refreshPanel()
    throw new TsUnderDevelopmentRtException( "PanelSkideTaskConfig.refreshPanel()" );

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
    ISkideTaskManager taskMan = skideEnv.taskManager();
    taskInfo = taskMan.listTasks().getByKey( aTaskId );
    refreshPanel();
  }

}
