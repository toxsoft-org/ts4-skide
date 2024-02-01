package org.toxsoft.skide.core.gui.tasks;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.core.gui.m5.*;

/**
 * Panel to edit the list of units to be included in the specified task run.
 * <p>
 * Contains checkable list of the units returned by the method {@link ISkideTaskRegistrator#listCapableUnits(String)}
 * for the specified task {@link #getTaskProcessor()}. It is assumed that checked units {@link #listCheckedUnits()} will
 * be included in the task run.
 *
 * @author hazard157
 */
public class PanelTaskUnitIdsEdit
    extends TsStdEventsProducerPanel<ISkideUnit> {

  /**
   * LM for {@link SkideUnitM5Model} enumerating units capable of task specified as {@link #master()}.
   *
   * @author hazard157
   */
  class LifecycleManager
      extends M5LifecycleManager<ISkideUnit, SkideTaskProcessor> {

    public LifecycleManager( IM5Model<ISkideUnit> aModel, SkideTaskProcessor aMaster ) {
      super( aModel, false, false, false, true, aMaster );
      TsNullArgumentRtException.checkNull( aMaster );
    }

    @Override
    protected IList<ISkideUnit> doListEntities() {
      return listCapableUnits();
    }

  }

  private final IM5CollectionPanel<ISkideUnit> panel;

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
  public PanelTaskUnitIdsEdit( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    IM5Model<ISkideUnit> model = m5().getModel( SkideUnitM5Model.MODEL_ID, ISkideUnit.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AV_FALSE );
    OPDEF_IS_TOOLBAR.setValue( ctx.params(), AV_FALSE );
    panel = model.panelCreator().createCollChecksPanel( ctx, null );
    panel.createControl( this );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.addTsSelectionListener( selectionChangeEventHelper );
    panel.addTsDoubleClickListener( doubleClickEventHelper );
    panel.checkSupport().checksChangeEventer().addListener( genericChangeEventer );
  }

  // ------------------------------------------------------------------------------------
  // TsStdEventsProducerPanel
  //

  @Override
  public ISkideUnit selectedItem() {
    return panel.selectedItem();
  }

  @Override
  public void setSelectedItem( ISkideUnit aItem ) {
    panel.setSelectedItem( aItem );
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  IStridablesList<ISkideUnit> listCapableUnits() {
    if( taskProcessor == null ) {
      return IStridablesList.EMPTY;
    }
    String taskId = taskProcessor.taskInfo().id();
    ISkideEnvironment skideEnv = tsContext().get( ISkideEnvironment.class );
    ISkideTaskRegistrator taskRegistrator = skideEnv.taskRegistrator();
    return taskRegistrator.listCapableUnits( taskId );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the list of displayed items.
   * <p>
   * Not all items may be displayed in panel, for example, if panel contains filter pane to select subset of items.
   *
   * @return {@link IList}&lt;{@link ISkideUnit}&gt; - items in panel
   */
  public IList<ISkideUnit> items() {
    return panel.items();
  }

  /**
   * Returns the units checked to be included in task run.
   *
   * @return {@link IStridablesList}&lt;{@link ISkideUnit}&gt; - list of units, may be empty
   */
  public IStridablesList<ISkideUnit> listCheckedUnits() {
    return new StridablesList<>( panel.checkSupport().listCheckedItems( true ) );
  }

  /**
   * Returns the units checked to be included in task run.
   *
   * @return {@link IStringList} - list of unit IDs, may be empty
   */
  public IStringList listCheckedUnitIds() {
    return listCheckedUnits().ids();
  }

  /**
   * Checks the units to be included in task run.
   * <p>
   * Unknown units or units not capable to run the task are ignored.
   *
   * @param aUnitIds {@link IStringList} - the unit IDs to include in task run
   */
  public void setCheckedUnitIds( IStringList aUnitIds ) {
    TsNullArgumentRtException.checkNull( aUnitIds );
    try {
      panel.checkSupport().checksChangeEventer().pauseFiring();
      panel.checkSupport().setAllItemsCheckState( false );
      for( String s : aUnitIds ) {
        ISkideUnit unit = listCapableUnits().findByKey( s );
        if( unit != null ) {
          panel.checkSupport().setItemCheckState( unit, true );
        }
      }
    }
    finally {
      panel.checkSupport().checksChangeEventer().resumeFiring( true );
    }
  }

  /**
   * Returns for which task the unitIds will be edited.
   *
   * @return {@link SkideTaskProcessor} - the task or <code>null</code>
   */
  public SkideTaskProcessor getTaskProcessor() {
    return taskProcessor;
  }

  /**
   * Sets for which task the unitIds will be edited.
   *
   * @param aTaskProcessor {@link SkideTaskProcessor} - the task or <code>null</code>
   */
  public void setTaskProcessor( SkideTaskProcessor aTaskProcessor ) {
    taskProcessor = aTaskProcessor;
    if( taskProcessor != null ) {
      IM5Model<ISkideUnit> model = m5().getModel( SkideUnitM5Model.MODEL_ID, ISkideUnit.class );
      LifecycleManager lm = new LifecycleManager( model, taskProcessor );
      panel.setItemsProvider( lm.itemsProvider() );
    }
    else {
      panel.setItemsProvider( null );
    }
    panel.refresh();
  }

}
