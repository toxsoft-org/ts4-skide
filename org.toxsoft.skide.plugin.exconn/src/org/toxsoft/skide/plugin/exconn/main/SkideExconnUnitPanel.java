package org.toxsoft.skide.plugin.exconn.main;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import java.lang.reflect.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.operation.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.strid.idgen.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.plugin.exconn.*;
import org.toxsoft.skide.plugin.exconn.service.*;
import org.toxsoft.uskat.core.gui.conn.cfg.*;
import org.toxsoft.uskat.core.gui.conn.cfg.m5.*;

/**
 * {@link AbstractSkideUnitPanel} implementation.
 *
 * @author hazard157
 * @author dima
 */
class SkideExconnUnitPanel
    extends AbstractSkideUnitPanel {

  private final IStridGenerator idGen = new SimpleStridGenaretor( SimpleStridGenaretor.DEFAULT_INITIAL_STATE );
  private TsComposite           backplane;

  final static String ACTID_EXPORT_SYSDESCR = SK_ID + ".org.toxsoft.skide.plugin.exconn.ExportSysdescr"; //$NON-NLS-1$

  final static TsActionDef ACDEF_EXPORT_SYSDESCR = TsActionDef.ofPush2( ACTID_EXPORT_SYSDESCR, STR_N_EXPORT_SYSDESCR,
      STR_D_EXPORT_SYSDESCR, ISkidePluginExconnConstants.ICONID_SYSDESCR_EXPORT );

  public SkideExconnUnitPanel( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    backplane = new TsComposite( aParent );
    backplane.setLayout( new BorderLayout() );
    IConnectionConfigService ccService = tsContext().get( IConnectionConfigService.class );
    IM5Model<IConnectionConfig> model =
        m5().getModel( IConnectionConfigM5Constants.MID_SK_CONN_CFG, IConnectionConfig.class );
    IM5LifecycleManager<IConnectionConfig> lm = model.getLifecycleManager( ccService );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.params().addAll( tsContext().params() );
    IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(),
        avValobj( EBorderLayoutPlacement.SOUTH ) );
    // IMultiPaneComponentConstants.OPDEF_IS_SUPPORTS_TREE.setValue( ctx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AvUtils.AV_TRUE );
    // добавляем в панель фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AvUtils.AV_FALSE );

    MultiPaneComponentModown<IConnectionConfig> cfgsListPanel =
        new MultiPaneComponentModown<>( ctx, model, lm.itemsProvider(), lm ) {

          @Override
          protected ITsToolbar doCreateToolbar( @SuppressWarnings( "hiding" ) ITsGuiContext aContext, String aName,
              EIconSize aIconSize, IListEdit<ITsActionDef> aActs ) {
            aActs.add( ITsStdActionDefs.ACDEF_SEPARATOR );
            aActs.add( ACDEF_EXPORT_SYSDESCR );

            ITsToolbar toolbar =

                super.doCreateToolbar( aContext, aName, aIconSize, aActs );

            toolbar.addListener( aActionId -> {
              // TODO Auto-generated method stub

            } );

            return toolbar;
          }

          @Override
          protected void doProcessAction( String aActionId ) {
            super.doProcessAction( aActionId );
            IConnectionConfig selConfig = selectedItem();

            switch( aActionId ) {
              case ACTID_EXPORT_SYSDESCR:
                // exportSysdescr( selConfig );
                // Запускаем процесс с индикатором выполнения
                SysdescrExportRunner importer = new SysdescrExportRunner( tsContext(), selConfig );
                runInWaitingDialog( getShell(), STR_EXPORT_SYSDESCR, importer );
                if( importer.success() ) {
                  TsDialogUtils.info( getShell(), MSG_EXPORT_PROCESS_COMPLETED_ERROR_FREE, selConfig.nmName() );
                }
                else {
                  TsDialogUtils.info( getShell(), MSG_EXPORT_PROCESS_FAILED, selConfig.nmName() );
                }
                break;
              default:
                throw new TsNotAllEnumsUsedRtException( aActionId );
            }
          }

        };

    cfgsListPanel.createControl( backplane );
    cfgsListPanel.getControl().setLayoutData( BorderLayout.WEST );

    // пока не выбрано ни одно соединение, отключаем
    cfgsListPanel.toolbar().getAction( ACDEF_EXPORT_SYSDESCR.id() ).setEnabled( false );
    cfgsListPanel.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
      // просто активируем кнопку подключения/просмотра
      boolean enableRunBttn = (aSelectedItem != null);
      cfgsListPanel.toolbar().getAction( ACDEF_EXPORT_SYSDESCR.id() ).setEnabled( enableRunBttn );
    } );

    return backplane;
  }

  /**
   * Выполняет работы в отдельном потоке при открытом диалоге ожидания.
   *
   * @param aShell Shell родительское окно
   * @param aDialogName String - имя диалога ожидания.
   * @param aRunnable IRunnableWithProgress - реализация потока выполнения работы.
   */
  public static void runInWaitingDialog( Shell aShell, String aDialogName, IRunnableWithProgress aRunnable ) {

    final ProgressMonitorDialog dialog = new ProgressMonitorDialog( aShell ) {

      @Override
      protected Control createDialogArea( Composite aParent ) {
        Control c = super.createDialogArea( aParent );
        c.getShell().setText( aDialogName );
        return c;
      }
    };

    try {
      dialog.run( true, false, aRunnable );
    }
    catch( InvocationTargetException | InterruptedException e ) {
      LoggerUtils.errorLogger().error( e );
      Display.getDefault().asyncExec( () -> TsDialogUtils.error( aShell, e.getCause() != null ? e.getCause() : e ) );
    }
  }

}
