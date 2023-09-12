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
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.idgen.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.plugin.exconn.*;
import org.toxsoft.skide.plugin.exconn.service.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.conn.cfg.*;
import org.toxsoft.uskat.core.gui.conn.cfg.m5.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.core.impl.dto.*;
import org.toxsoft.uskat.s5.client.*;
import org.toxsoft.uskat.s5.utils.threads.impl.*;

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

  private void exportSysdescr( IConnectionConfig aSelConfig ) {
    // select the connection
    IConnectionConfigService ccService = tsContext().get( IConnectionConfigService.class );
    Shell shell = tsContext().get( Shell.class );

    // prepare arguments
    IConnectionConfigProvider ccProvider = ccService.listProviders().findByKey( aSelConfig.providerId() );
    ITsContext args = new TsContext();
    ccProvider.fillArgs( args, aSelConfig.opValues() );

    /**
     * TODO вниманию dima и MVK: выделенный внизу код неправильный! во-первых, он предназначен ТОЛЬКО для S5 соединения,
     * а здесь код SkIDE, который может соединятся любым бекендм. Во-вторых, бекенд-специфичный код настроки аргументов
     * дожен находится в классах, наследниках ConnectionConfigProvider. То есть, в реализации провайдера
     * IConnectionConfigProvider.<br>
     * При таком подходе, достаточно одной строки выше:<br>
     * ccProvider.fillArgs( args, conConf.opValues() );<br>
     * <p>
     * По-хорошему, указанный код должен находится в теле метода S5ConnectionConfigProvider.doProcessArgs().
     */
    // TODO ---

    String login = "root";
    String password = "1";
    args.params().setStr( IS5ConnectionParams.OP_USERNAME, login );
    args.params().setStr( IS5ConnectionParams.OP_PASSWORD, password );
    IS5ConnectionParams.REF_CONNECTION_LOCK.setRef( args, new S5Lockable() );
    // necessary staff for RCP
    IS5ConnectionParams.REF_CLASSLOADER.setRef( args, getClass().getClassLoader() );
    ISkCoreConfigConstants.REFDEF_THREAD_SEPARATOR.setRef( args, SwtThreadSeparatorService.CREATOR );
    Display display = tsContext().get( Display.class );
    SwtThreadSeparatorService.REF_DISPLAY.setRef( args, display );

    // ---

    // create connection
    ISkConnectionSupplier conSup = tsContext().get( ISkConnectionSupplier.class );
    ISkConnection sourceConn = conSup.defConn();
    IdChain connId = new IdChain( aSelConfig.id(), idGen.nextId() );
    ISkConnection targetConn = conSup.createConnection( connId, tsContext() );
    // open connection
    try {
      // TODO invoke connection progress dialog
      targetConn.open( args );
    }
    catch( Exception ex ) {
      TsDialogUtils.error( shell, ex );
      conSup.removeConnection( connId );
      LoggerUtils.errorLogger().error( ex );
    }
    // тут выкачиваем классы и объекты из источника
    IStridablesList<ISkClassInfo> classList = sourceConn.coreApi().sysdescr().listClasses();
    IListEdit<IDtoClassInfo> dtoClassList = new ElemArrayList<>();
    for( ISkClassInfo classInfo : classList ) {
      dtoClassList.add( DtoClassInfo.createFromSk( classInfo, false ) );
    }

    // запрашиваем у пользователя что записывать
    // TODO

    // записываем в целевую базу
    for( IDtoClassInfo dtoClassInfo : dtoClassList ) {
      // создаем класс
      targetConn.coreApi().sysdescr().defineClass( dtoClassInfo );
      // создаем его объекты
      ISkidList skidList = sourceConn.coreApi().objService().listSkids( dtoClassInfo.id(), false );
      for( Skid skid : skidList ) {
        // создаем DtoFullObject
        DtoFullObject dto = DtoFullObject.createDtoFullObject( skid, sourceConn.coreApi() );
        DtoFullObject.defineFullObject( targetConn.coreApi(), dto );
      }
    }
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
