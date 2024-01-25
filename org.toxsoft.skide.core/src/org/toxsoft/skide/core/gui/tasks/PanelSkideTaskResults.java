package org.toxsoft.skide.core.gui.tasks;

import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.core.api.tasks.*;

/**
 * Panel to display the input parameters and results of the last task execution.
 *
 * @author hazard157
 */
public class PanelSkideTaskResults
    extends TsPanel {

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
  public PanelSkideTaskResults( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    skideEnv = tsContext().get( ISkideEnvironment.class );
    taskMan = skideEnv.taskManager();
    this.setLayout( new BorderLayout() );

    // TODO PanelSkideTaskResults.PanelSkideTaskResults()

  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void refreshPanel() {
    // TODO PanelSkideTaskResults.refreshPanel()
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
    taskInfo = taskMan.listRegisteredSkideTasks().getByKey( aTaskId );
    refreshPanel();
  }

}
