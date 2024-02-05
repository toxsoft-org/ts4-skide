package org.toxsoft.skide.plugin.exconn.service;

import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnSharedResources.*;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tslib.bricks.ctx.ITsContext;
import org.toxsoft.core.tslib.bricks.ctx.impl.TsContext;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.idgen.IStridGenerator;
import org.toxsoft.core.tslib.bricks.strid.idgen.SimpleStridGenerator;
import org.toxsoft.core.tslib.bricks.strid.more.IdChain;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.gw.IGwHardConstants;
import org.toxsoft.core.tslib.gw.skid.ISkidList;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.ILogger;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.api.sysdescr.ISkClassInfo;
import org.toxsoft.uskat.core.api.sysdescr.ISkSysdescr;
import org.toxsoft.uskat.core.api.sysdescr.dto.IDtoClassInfo;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.gui.conn.SkGuiThreadExecutor;
import org.toxsoft.uskat.core.gui.conn.cfg.*;
import org.toxsoft.uskat.core.impl.ISkCoreConfigConstants;
import org.toxsoft.uskat.core.impl.dto.DtoClassInfo;
import org.toxsoft.uskat.core.impl.dto.DtoFullObject;
import org.toxsoft.uskat.s5.client.IS5ConnectionParams;

/**
 * Экспорт системного описания из текстового backend в боевой сервер.
 *
 * @author Dima
 */
public class SysdescrExportRunner
    implements IRunnableWithProgress {

  private final IStridGenerator idGen = new SimpleStridGenerator( SimpleStridGenerator.DEFAULT_INITIAL_STATE );

  private final ITsGuiContext     tsContext;
  private final IConnectionConfig targetConnConfig;
  private boolean                 success = true;

  private static final ILogger logger = LoggerUtils.errorLogger();

  /**
   * @param aContext {@link ITsGuiContext} контекст приложения
   * @param aSelConfig {@link IConnectionConfig} параметры соединения с целевым сервером
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public SysdescrExportRunner( ITsGuiContext aContext, IConnectionConfig aSelConfig ) {
    TsNullArgumentRtException.checkNull( aContext );
    TsNullArgumentRtException.checkNull( aSelConfig );
    tsContext = aContext;
    targetConnConfig = aSelConfig;
  }

  // ------------------------------------------------------------------------------------
  // Открытое API
  //
  /**
   * Возвращает признак того, что экспорт успешно выполнен
   *
   * @return <b>true</b> экспорт успешно выполнен; <b>false</b> экспорт не выполнялся или выполнился с ошибкой.
   */
  public boolean success() {
    return success;
  }

  // ------------------------------------------------------------------------------------
  // Реализация IRunnableWithProgress
  //
  @SuppressWarnings( "null" )
  @Override
  public void run( IProgressMonitor aMonitor )
      throws InvocationTargetException,
      InterruptedException {
    TsIllegalArgumentRtException.checkTrue( aMonitor == null );
    // Начинаем процесс экспорта
    aMonitor.beginTask( STR_EXPORT_SYSDESCR, IProgressMonitor.UNKNOWN );
    // первый этап, подключаемся к целевой базе
    aMonitor.setTaskName( MSG_OPEN_TARGET_CONN );
    ISkConnection targetConn = targetConnection();

    ISkConnectionSupplier conSup = tsContext.get( ISkConnectionSupplier.class );
    ISkConnection sourceConn = conSup.defConn();

    Shell shell = tsContext.get( Shell.class );
    // работаем в GUI потоке чтобы не сломать сервер
    shell.getDisplay().syncExec( () -> {
      // Второй этап, заливаем сущности
      aMonitor.setTaskName( MSG_EXPORTING );
      // тут выкачиваем классы и объекты из источника
      IStridablesList<ISkClassInfo> classList = sourceConn.coreApi().sysdescr().listClasses();
      IListEdit<IDtoClassInfo> dtoClassList = new ElemArrayList<>();
      for( ISkClassInfo classInfo : classList ) {
        dtoClassList.add( DtoClassInfo.createFromSk( classInfo, true ) );
      }
      // записываем в целевую базу
      for( IDtoClassInfo dtoClassInfo : dtoClassList ) {
        // пропускаем классы которые "не твоего ума.." :)
        if( nonYourBusiness( dtoClassInfo, targetConn ) ) {
          continue;
        }
        try {
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
        catch( Exception ex ) {
          TsDialogUtils.info( shell, "Error in process: %s ", ex.getMessage() );
          success = false;
        }
      }
      // TODO перенести в свой раздел
      ISkRegRefInfoService sourceRriService = sourceConn.coreApi().getService( ISkRegRefInfoService.SERVICE_ID );
      ISkRegRefInfoService targetRriService = targetConn.coreApi().getService( ISkRegRefInfoService.SERVICE_ID );
      // получаем список секций в источнике
      for( ISkRriSection srcSection : sourceRriService.listSections() ) {
        ISkRriSection targetSection;
        if( targetRriService.findSection( srcSection.id() ) == null ) {
          // создаем секцию в целевом соединении
          targetSection = targetRriService.createSection( srcSection.id(), srcSection.nmName(),
              srcSection.description(), srcSection.params() );
        }
        else {
          targetSection = targetRriService.getSection( srcSection.id() );
          targetSection.setSectionProps( srcSection.nmName(), srcSection.description(), srcSection.params() );
        }
        // копируем в нее содержание исходной
        for( String srcClassId : srcSection.listClassIds() ) {
          // создаем класс в целевой секции
          for( IDtoRriParamInfo paramInfo : srcSection.listParamInfoes( srcClassId ) ) {
            targetSection.defineParam( srcClassId, paramInfo );
          }
        }
      }

    } );
    aMonitor.done();

  }

  private static boolean nonYourBusiness( IDtoClassInfo aDtoClassInfo, ISkConnection aConn ) {
    if( aDtoClassInfo.id().compareTo( IGwHardConstants.GW_ROOT_CLASS_ID ) == 0 ) {
      return true;
    }
    String serviceId = aConn.coreApi().sysdescr().determineClassClaimingServiceId( aDtoClassInfo.id() );
    if( ISkSysdescr.SERVICE_ID.compareTo( serviceId ) != 0 ) {
      return true;
    }
    return false;
  }

  private ISkConnection targetConnection() {
    ISkConnection retVal = null;
    // select the connection
    IConnectionConfigService ccService = tsContext.get( IConnectionConfigService.class );
    Shell shell = tsContext.get( Shell.class );

    // prepare arguments
    IConnectionConfigProvider ccProvider = ccService.listProviders().findByKey( targetConnConfig.providerId() );
    ITsContext args = new TsContext();
    ccProvider.fillArgs( args, targetConnConfig.opValues() );

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
    // necessary staff for RCP
    Display display = tsContext.get( Display.class );
    IS5ConnectionParams.REF_CLASSLOADER.setRef( args, getClass().getClassLoader() );
    ISkCoreConfigConstants.REFDEF_THREAD_EXECUTOR.setRef( args, new SkGuiThreadExecutor( display ) );

    // create connection
    ISkConnectionSupplier conSup = tsContext.get( ISkConnectionSupplier.class );
    IdChain connId = new IdChain( targetConnConfig.id(), idGen.nextId() );
    retVal = conSup.createConnection( connId, tsContext );
    // open connection
    try {
      retVal.open( args );
    }
    catch( Exception ex ) {
      TsDialogUtils.error( shell, ex );
      conSup.removeConnection( connId );
      LoggerUtils.errorLogger().error( ex );
      retVal = null;
    }
    return retVal;
  }
}
