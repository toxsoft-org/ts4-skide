package org.toxsoft.skide.plugin.exconn.service;

import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnSharedResources.*;

import java.lang.reflect.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.idgen.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.conn.cfg.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.core.impl.dto.*;
import org.toxsoft.uskat.s5.client.*;
import org.toxsoft.uskat.s5.utils.threads.impl.*;

/**
 * Экспорт системного описания из текстового backend в боевой сервер.
 *
 * @author Dima
 */
public class SysdescrExportRunner
    implements IRunnableWithProgress {

  private final IStridGenerator idGen = new SimpleStridGenaretor( SimpleStridGenaretor.DEFAULT_INITIAL_STATE );

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
    success = false;
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
    IS5ConnectionParams.REF_CONNECTION_LOCK.setRef( args, new S5Lockable() );
    // necessary staff for RCP
    IS5ConnectionParams.REF_CLASSLOADER.setRef( args, getClass().getClassLoader() );
    ISkCoreConfigConstants.REFDEF_THREAD_SEPARATOR.setRef( args, SwtThreadSeparatorService.CREATOR );
    Display display = tsContext.get( Display.class );
    SwtThreadSeparatorService.REF_DISPLAY.setRef( args, display );

    // ---

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
