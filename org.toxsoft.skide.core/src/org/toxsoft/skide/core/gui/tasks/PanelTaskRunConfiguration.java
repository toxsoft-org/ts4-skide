package org.toxsoft.skide.core.gui.tasks;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;

/**
 * Panels to configure the task including task input, included unit IDs and per-unit configuration options.
 * <p>
 * Contains:
 * <ul>
 * <li>top panel - the task input options {@link SkideTaskProcessor#getTaskInputOptions()} edit panel
 * {@link IOptionSetPanel};</li>
 * <li>left panel of units checkable list {@link PanelTaskUnitIdsEdit};</li>
 * <li>right panel {@link IOptionSetPanel} to edit config {@link AbstractSkideUnitTask#getCfgOptionValues()}.</li>
 * </ul>
 * Panel does not changes configuration options, instead it returns edited values {@link #getUnitTasksCfgOpsMap()}.
 *
 * @author hazard157
 */
public class PanelTaskRunConfiguration
    extends TsStdEventsProducerPanel<ISkideUnit> {

  /**
   * All values edited by this panel in a one package.
   *
   * @author hazard157
   * @param inOps {@link IOptionSet} - task input options (as {@link SkideTaskProcessor#getTaskInputOptions()})
   * @param unitIds {@link IStringList} - included unit IDs (as {@link SkideTaskProcessor#getTaskUnitIds()})
   * @param unitCfgs {@link IStringMap}&lt;{@link IOptionSet}&gt; - map "unitID" - "unit task configuration"
   */
  public static record Config ( IOptionSet inOps, IStringList unitIds, IStringMap<IOptionSet> unitCfgs ) {

    /**
     * Constructor.
     *
     * @param inOps {@link IOptionSet} - task input options (as {@link SkideTaskProcessor#getTaskInputOptions()})
     * @param unitIds {@link IStringList} - included unit IDs (as {@link SkideTaskProcessor#getTaskUnitIds()})
     * @param unitCfgs {@link IStringMap}&lt;{@link IOptionSet}&gt; - map "unitID" - "unit task configuration"
     */
    public Config( IOptionSet inOps, IStringList unitIds, IStringMap<IOptionSet> unitCfgs ) {
      this.inOps = new OptionSet( inOps );
      this.unitIds = new StringArrayList( unitIds );
      IStringMapEdit<IOptionSet> map = new StringMap<>();
      for( String s : unitCfgs.keys() ) {
        map.put( s, new OptionSet( unitCfgs.getByKey( s ) ) );
      }
      this.unitCfgs = map;
    }

  }

  private final IOptionSetPanel      panelTop;
  private final PanelTaskUnitIdsEdit panelLeft;
  private final IOptionSetPanel      panelRight;

  /**
   * Current vales edited in the right panel.
   * <p>
   * This map "unit ID from left panel" - "task config options" are updated every time when user edits config options in
   * the right panel.
   */
  private final IStringMapEdit<IOptionSet> cfgOpsMap = new StringMap<>();

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelTaskRunConfiguration( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    // main sash form
    SashForm sfMain = new SashForm( this, SWT.VERTICAL );
    // panelTop
    panelTop = new OptionSetPanel( tsContext(), false, true );
    panelTop.createControl( sfMain );
    // bottom sash form
    SashForm sfBottom = new SashForm( sfMain, SWT.HORIZONTAL );
    sfBottom.setLayoutData( BorderLayout.CENTER );
    // panelLeft
    panelLeft = new PanelTaskUnitIdsEdit( sfBottom, tsContext() );
    // panelRight
    panelRight = new OptionSetPanel( tsContext(), false, true );
    panelRight.createControl( sfBottom );
    // setup
    sfMain.setWeights( 4000, 6000 );
    sfBottom.setWeights( 3000, 7000 );
    panelTop.genericChangeEventer().addListener( genericChangeEventer );
    panelLeft.addTsDoubleClickListener( doubleClickEventHelper );
    panelLeft.addTsSelectionListener( ( src, sel ) -> whenUnitSelectionChanges( sel ) );
    panelLeft.addTsSelectionListener( selectionChangeEventHelper );
    panelLeft.genericChangeEventer().addListener( genericChangeEventer );
    panelRight.genericChangeEventer().addListener( s -> whenUserChangesConfigOptions() );
    panelRight.genericChangeEventer().addListener( genericChangeEventer );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenUnitSelectionChanges( ISkideUnit aSelUnit ) {
    panelRight.setOptionDefs( IStridablesList.EMPTY );
    panelRight.setEntity( IOptionSet.NULL );
    if( aSelUnit != null ) {
      AbstractSkideUnitTask unitTask = aSelUnit.listSupportedTasks().findByKey( getTaskProcessor().taskInfo().id() );
      TsInternalErrorRtException.checkNull( unitTask );
      panelRight.setOptionDefs( unitTask.getCfgOptionDefs() );
      panelRight.setEntity( cfgOpsMap.getByKey( aSelUnit.id() ) );
    }
  }

  private void whenUserChangesConfigOptions() {
    ISkideUnit selUnit = panelLeft.selectedItem();
    TsInternalErrorRtException.checkNull( selUnit );
    cfgOpsMap.put( selUnit.id(), panelRight.getEntity() );
  }

  // ------------------------------------------------------------------------------------
  // TsStdEventsProducerPanel
  //

  @Override
  public ISkideUnit selectedItem() {
    return panelLeft.selectedItem();
  }

  @Override
  public void setSelectedItem( ISkideUnit aItem ) {
    panelLeft.setSelectedItem( aItem );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns all edited values at once.
   *
   * @return {@link Config} - edited values
   */
  public Config getConfig() {
    return new Config( getTaskInputOptions(), getCheckedUnitIds(), getUnitTasksCfgOpsMap() );
  }

  /**
   * Sets all edited values at once.
   *
   * @param aCfg {@link Config} - the values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setConfig( Config aCfg ) {
    TsNullArgumentRtException.checkNull( aCfg );
    genericChangeEventer().pauseFiring();
    try {
      panelTop.setEntity( aCfg.inOps() );
      panelLeft.setCheckedUnitIds( aCfg.unitIds() );
      panelLeft.setSelectedItem( null );
      panelRight.setOptionDefs( IStridablesList.EMPTY );
      panelRight.setEntity( IOptionSet.NULL );
      cfgOpsMap.setAll( aCfg.unitCfgs() );
    }
    finally {
      genericChangeEventer().fireChangeEvent();
      genericChangeEventer().resumeFiring( true );
    }
  }

  /**
   * Returns for which task the unitIds will be edited.
   *
   * @return {@link SkideTaskProcessor} - the task or <code>null</code>
   */
  public SkideTaskProcessor getTaskProcessor() {
    return panelLeft.getTaskProcessor();
  }

  /**
   * Sets for which task the unitIds will be edited.
   *
   * @param aTaskProcessor {@link SkideTaskProcessor} - the task or <code>null</code>
   */
  public void setTaskProcessor( SkideTaskProcessor aTaskProcessor ) {
    panelLeft.setTaskProcessor( aTaskProcessor );
    // init panelTop
    panelTop.setOptionDefs( IStridablesList.EMPTY );
    panelTop.setEntity( IOptionSet.NULL );
    panelLeft.setCheckedUnitIds( IStringList.EMPTY );
    if( getTaskProcessor() != null ) {
      panelTop.setOptionDefs( getTaskProcessor().taskInfo().inOps() );
      panelTop.setEntity( getTaskProcessor().getTaskInputOptions() );
      panelLeft.setCheckedUnitIds( aTaskProcessor.getTaskUnitIds() );
    }
    // init cfgOpsMap
    cfgOpsMap.clear();
    for( ISkideUnit unit : panelLeft.listCapableUnits() ) {
      AbstractSkideUnitTask task = unit.listSupportedTasks().getByKey( getTaskProcessor().taskInfo().id() );
      cfgOpsMap.put( unit.id(), task.getCfgOptionValues() );
    }
    // setup
    panelLeft.setSelectedItem( panelLeft.items().first() );
  }

  /**
   * Returns task input option values from the widgets.
   *
   * @return {@link IOptionSet} - edited task input option values
   */
  public IOptionSet getTaskInputOptions() {
    return panelTop.getEntity();
  }

  // /**
  // * Sets the task input options to be edited.
  // *
  // * @param aInputOps {@link IOptionSet} - option values
  // */
  // public void setTaskInputOptions( IOptionSet aInputOps ) {
  // TsNullArgumentRtException.checkNull( aInputOps );
  // panelTop.setEntity( aInputOps );
  // }

  /**
   * Returns the units checked to be included in task run.
   *
   * @return {@link IStringList} - list of unit IDs, may be empty
   */
  public IStringList getCheckedUnitIds() {
    return panelLeft.listCheckedUnitIds();
  }

  // /**
  // * Checks the units to be included in task run.
  // * <p>
  // * Unknown units or units not capable to run the task are ignored.
  // *
  // * @param aUnitIds {@link IStringList} - the unit IDs to include in task run
  // */
  // public void setCheckedUnitIds( IStringList aUnitIds ) {
  // panelLeft.setCheckedUnitIds( aUnitIds );
  // }

  /**
   * Returns the edited values of the unit task configuration options.
   *
   * @return {@link IStringMap}&lt;{@link IOptionSet}&gt; -
   */
  public IStringMap<IOptionSet> getUnitTasksCfgOpsMap() {
    return cfgOpsMap;
  }

}
