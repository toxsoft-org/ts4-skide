package org.toxsoft.skide.core.gui.tasks;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.gui.tasks.ISkResources.*;

import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;

/**
 * Specified SkIDE task configuration panel.
 * <p>
 * Configures one of the tasks listed in {@link ISkideTaskRegistrator#getRegisteredProcessors()}. The configured task is
 * determined by {@link #getSkideTaskId()}.
 * <p>
 * Task configuration includes:
 * <ul>
 * <li>specifying the input parameters of the task as defined by {@link IGenericTaskInfo#inOps()};</li>
 * <li>setting configuration options {@link AbstractSkideUnitTask#cfgOptionValues()} for each task in
 * {@link ISkideTaskRegistrator#listCapableUnits(String)} of the specified task.</li>
 * </ul>
 * <p>
 * All changes in the panel are immediately saved to the SkIDE project.
 *
 * @author hazard157
 */
public class PanelSkideTaskConfig
    extends TsPanel {

  /**
   * When user edits in the panel calls {@link #whenOptionValueChanged(String)}.
   */
  private final IOpsetsKitChangeListener valuesChangeListener = new IOpsetsKitChangeListener() {

    @Override
    public void onOptionValueChanged( IOpsetsKitPanel aSource, String aKitItemId, String aOptionId,
        IAtomicValue aNewValue ) {
      whenOptionValueChanged( aKitItemId );
    }

    @Override
    public void onKitItemsSelected( IOpsetsKitPanel aSource, String aKitItemId ) {
      // nop
    }
  };

  private final ISkideEnvironment     skideEnv;
  private final ISkideTaskRegistrator taskReg;
  private final IOpsetsKitPanel       opKitPanel;

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
  public PanelSkideTaskConfig( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    skideEnv = tsContext().get( ISkideEnvironment.class );
    taskReg = skideEnv.taskRegistrator();
    this.setLayout( new BorderLayout() );
    opKitPanel = new OpsetsKitPanel( tsContext() );
    opKitPanel.createControl( this );
    opKitPanel.getControl().setLayoutData( BorderLayout.CENTER );
    opKitPanel.eventer().addListener( valuesChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void clearPanel() {
    opKitPanel.setKitItemDefs( IStridablesList.EMPTY );
  }

  private void refreshPanel() {
    clearPanel();
    if( taskProcessor == null ) {
      return;
    }
    IStridablesListEdit<IOpsetsKitItemDef> llKids = new StridablesList<>();
    IStringMapEdit<IOptionSet> mapVals = new StringMap<>();
    // options kit for task itself
    IGenericTaskInfo taskInfo = taskProcessor.taskInfo();
    OpsetsKitItemDef kidTaskSelf = new OpsetsKitItemDef( taskInfo.id(), taskInfo.inOps(), taskInfo.params() );
    DDEF_NAME.setValue( kidTaskSelf.params(), avStr( STR_TASK_INPUT_PARAMS ) );
    DDEF_DESCRIPTION.setValue( kidTaskSelf.params(), avStr( STR_TASK_INPUT_PARAMS_D ) );
    llKids.add( kidTaskSelf );
    SkideTaskProcessor p = taskReg.getRegisteredProcessors().getByKey( taskInfo.id() );
    mapVals.put( taskInfo.id(), p.getTaskInputOptions() );
    // options kits for capable units
    for( ISkideUnit unit : taskReg.listCapableUnits( taskInfo.id() ) ) {
      AbstractSkideUnitTask uTask = unit.listSupportedTasks().getByKey( taskInfo.id() );
      llKids.add( new OpsetsKitItemDef( unit.id(), uTask.cfgOptionDefs(), unit.params() ) );
    }
    // init panel
    opKitPanel.setKitItemDefs( llKids );
    opKitPanel.setAllKitOptionValues( mapVals );
  }

  void whenOptionValueChanged( String aKitItemId ) {
    TsInternalErrorRtException.checkNull( taskProcessor );
    IGenericTaskInfo taskInfo = taskProcessor.taskInfo();
    IOptionSet newVals = opKitPanel.getAllKitOptionValues().getByKey( aKitItemId );
    // task options changed
    if( aKitItemId.equals( taskInfo.id() ) ) {
      taskProcessor.setTaskInputOptions( newVals );
      return;
    }
    // unit options changed
    ISkideUnit unit = skideEnv.pluginsRegistrator().listUnits().getByKey( aKitItemId );
    AbstractSkideUnitTask task = unit.listSupportedTasks().getByKey( taskInfo.id() );
    task.setCfgOptionValues( newVals );
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
