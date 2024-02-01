package org.toxsoft.skide.core.gui.tasks;

import static org.toxsoft.skide.core.gui.tasks.ISkResources.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.core.gui.tasks.PanelTaskRunConfiguration.*;

/**
 * Dialog to invoke {@link PanelTaskRunConfiguration}.
 *
 * @author hazard157
 */
public class DialogTaskRunConfiguration {

  /**
   * Dialog panel wrapping over {@link PanelTaskRunConfiguration}.
   *
   * @author hazard157
   */
  static class DialogPanel
      extends AbstractTsDialogPanel<Config, SkideTaskProcessor> {

    private final PanelTaskRunConfiguration panel;

    DialogPanel( Composite aParent, TsDialog<Config, SkideTaskProcessor> aOwnerDialog ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      panel = new PanelTaskRunConfiguration( this, tsContext() );
      panel.setLayoutData( BorderLayout.CENTER );
      panel.setTaskProcessor( environ() );
      panel.genericChangeEventer().addListener( notificationGenericChangeListener );
    }

    @Override
    protected void doSetDataRecord( Config aData ) {
      // do nothing - initial configuration is read by PanelTaskRunConfiguration directly from storage
    }

    @Override
    protected ValidationResult validateData() {
      Config cfg = panel.getConfig();
      ValidationResult vr = environ().canRun( cfg.unitIds(), cfg.inOps() );
      if( vr.isError() ) {
        return vr;
      }
      ISkideEnvironment skideEnv = tsContext().get( ISkideEnvironment.class );
      for( String uid : cfg.unitIds() ) {
        ISkideUnit unit = skideEnv.pluginsRegistrator().listUnits().getByKey( uid );
        AbstractSkideUnitTask uTask = unit.listSupportedTasks().getByKey( environ().taskInfo().id() );
        IOptionSet uTaskCfg = cfg.unitCfgs().getByKey( uid );
        vr = ValidationResult.firstNonOk( vr, OptionSetUtils.validateOptionSet( uTaskCfg, uTask.getCfgOptionDefs() ) );
        if( vr.isError() ) {
          break;
        }
      }
      return vr;
    }

    @Override
    protected Config doGetDataRecord() {
      return panel.getConfig();
    }

  }

  /**
   * Invokes task run configuration dialog.
   * <p>
   * During the edit configuration is checked by {@link SkideTaskProcessor#canRun(IStringList, IOptionSet)} and against
   * individual unit tasks {@link AbstractSkideUnitTask#getCfgOptionDefs()}. OK is enabled only if all checks succeed.
   * <p>
   * After pressing OK, if argument <code>aUpdateConfig</code> is <code>true</code> changed configuration is stored back
   * to {@link SkideTaskProcessor} and individual unit tasks. If <code>aUpdateConfig</code> is <code>false</code>,
   * existing configuration remains unchanged, edited values are only returned by this method.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aTask {@link SkideTaskProcessor} - task to run
   * @param aUpdateConfig boolean - <code>true</code> to update task configuration
   * @return {@link Config} - task run configuration or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static final Config edit( ITsGuiContext aContext, SkideTaskProcessor aTask, boolean aUpdateConfig ) {
    TsNullArgumentRtException.checkNulls( aContext, aTask );
    TsDialogInfo di = new TsDialogInfo( aContext, aTask.taskInfo().nmName(), STR_DLG_T_TASK_RUN_CFG );
    di.setMinSizeShellRelative( 50, 70 );
    TsDialog<Config, SkideTaskProcessor> d = new TsDialog<>( di, null, aTask, DialogPanel::new );
    Config cfg = d.execData();
    if( aUpdateConfig && cfg != null ) {
      // update task processors setting
      aTask.setTaskInputOptions( cfg.inOps() );
      aTask.setTaskUnitIdsToRun( cfg.unitIds() );
      // update individual unit task runners configurations
      ISkideEnvironment skideEnv = aContext.get( ISkideEnvironment.class );
      for( String uid : cfg.unitIds() ) {
        ISkideUnit unit = skideEnv.pluginsRegistrator().listUnits().getByKey( uid );
        AbstractSkideUnitTask uTask = unit.listSupportedTasks().getByKey( aTask.taskInfo().id() );
        IOptionSet uTaskCfg = cfg.unitCfgs().getByKey( uid );
        uTask.setCfgOptionValues( uTaskCfg );
      }
    }
    return cfg;
  }

}
