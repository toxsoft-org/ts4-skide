package org.toxsoft.skide.plugin.sded.tasks.upload;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.core.tslib.gw.IGwHardConstants.*;
import static org.toxsoft.skide.plugin.exconn.main.UploadToServerTaskProcessor.*;
import static org.toxsoft.skide.plugin.sded.ISkidePluginSdedSharedResources.*;

import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.plugin.exconn.main.*;
import org.toxsoft.skide.plugin.sded.main.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.devapi.transactions.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * SkIDE task {@link UploadToServerTaskProcessor} runner for {@link SkideUnitObjects}.
 *
 * @author max
 */
public class TaskObjectsUpload
    extends AbstractSkideUnitTaskSync {

  private final ObjectsUploadSelectionRules uploadSelectionRules = new ObjectsUploadSelectionRules();

  private ISkCoreApi srcCoreApi  = null;
  private ISkCoreApi destCoreApi = null;

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskObjectsUpload( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, UploadToServerTaskProcessor.INSTANCE.taskInfo(), //
        new StridablesList<>( /* No cfg ops */ ) );
  }

  @Override
  protected void doRunSync( ITsContextRo aInput, ITsContext aOutput ) {
    ILongOpProgressCallback lop = REFDEF_IN_PROGRESS_MONITOR.getRef( aInput );
    // Начинаем процесс экспорта
    lop.startWork( MSG_OBJECTS_UPLOAD, true );
    uploadSelectionRules.loadFromOptions( getCfgOptionValues() );
    ISkConnection srcConn = tsContext().get( ISkConnectionSupplier.class ).defConn();
    srcCoreApi = srcConn.coreApi();
    ISkConnection destConn = REFDEF_IN_OPEN_SK_CONN.getRef( aInput );
    destCoreApi = destConn.coreApi();
    int uploadedObjectsCount = uploadObjects();
    lop.finished( ValidationResult.info( FMT_OBJECTS_UPLOADED, Integer.valueOf( uploadedObjectsCount ) ) );
  }

  private int uploadObjects() {
    // load all src classes
    IStridablesList<ISkClassInfo> llToUpload = srcCoreApi.sysdescr().listClasses();

    // reduce list of src classes - leave only GW classes
    llToUpload = getGwClasses( llToUpload );

    // prepare list of objects to upload - first load all objects of GW classes
    IListEdit<Skid> srcObjSkidList = new ElemArrayList<>();

    for( ISkClassInfo skCinf : llToUpload ) {
      if( skCinf.id().equals( GW_ROOT_CLASS_ID ) ) {
        continue;
      }

      // all objects of class
      srcObjSkidList.addAll( srcCoreApi.objService().listSkids( skCinf.id(), false ) );
    }

    // filtr by user rules
    srcObjSkidList = applyUserDefinedUploadSelectionRules( srcObjSkidList );

    // check upload can be started

    IStridablesList<ISkClassInfo> llDestList = destCoreApi.sysdescr().listClasses();

    // check existance of linked objects in src objects list (or existance of that linked objects in dst)
    ValidationResult vr = checkExistanceOfLinkedObjectsInSrcOrDst( llToUpload, llDestList, srcObjSkidList );
    if( TsDialogUtils.askContinueOnValidation( getShell(), vr,
        MSG_ASK_CONT_LINKED_OBJS_UPLOAD_ON_WARN ) != ETsDialogCode.YES ) {
      return 0;
    }

    // add linked objects to src list of objects if need (not existed in dst)
    srcObjSkidList = addLinkedObjectsIfNeed( srcObjSkidList );

    // reduce list of src classes - leave classes of objects to upload
    llToUpload = removeUnneccessaryClasses( llToUpload, srcObjSkidList );

    // check existance and sameness src and dest classes of objects to upload
    vr = checkExistanceSamenessSrcDstClassesOfUploadObjects( llToUpload, llDestList );
    if( vr.isError() ) {
      TsDialogUtils.error( getShell(), MSG_ERR_CLASSES_SAMENESS_OBJS_UPLOAD );
      return 0;
    }

    // actually upload objects
    return actuallyUpload( srcObjSkidList );
  }

  private IListEdit<Skid> addLinkedObjectsIfNeed( IListEdit<Skid> aSrcObjSkidList ) {
    // TODO Auto-generated method stub
    return aSrcObjSkidList;
  }

  private static ValidationResult checkExistanceOfLinkedObjectsInSrcOrDst( IStridablesList<ISkClassInfo> aLlToUpload,
      IStridablesList<ISkClassInfo> aLlDestList, IListEdit<Skid> aSrcObjSkidList ) {
    // TODO Auto-generated method stub
    return ValidationResult.SUCCESS;
  }

  private static ValidationResult checkExistanceSamenessSrcDstClassesOfUploadObjects(
      IStridablesList<ISkClassInfo> aLlToUpload, IStridablesList<ISkClassInfo> aLlDestList ) {
    // TODO Auto-generated method stub
    return ValidationResult.SUCCESS;
  }

  private static IStridablesList<ISkClassInfo> removeUnneccessaryClasses( IStridablesList<ISkClassInfo> aLlToUpload,
      IListEdit<Skid> aSrcObjSkidList ) {
    // TODO Auto-generated method stub
    return aLlToUpload;
  }

  private static IListEdit<Skid> applyUserDefinedUploadSelectionRules( IListEdit<Skid> aSrcObjSkidList ) {
    // TODO Auto-generated method stub
    return aSrcObjSkidList;
  }

  /**
   * Actually uploads objects - defines specified objects in the destination {@link ISkCoreApi} service.
   *
   * @param aSrcObjSkidList {@link IStridablesList} - objects ids to upload
   * @return int - uploaded objects count
   */
  private int actuallyUpload( IListEdit<Skid> aSrcObjSkidList ) {

    int count = 0;
    // Разбиваем на два этапа, сначала создаем все объекты без инициализации связей
    ISkTransactionService dstTransactionService = ((IDevCoreApi)destCoreApi).transactionService();
    // Первый этап, создание объектов и склепок между ними в одной транзакции
    dstTransactionService.start();

    for( Skid objId : aSrcObjSkidList ) {
      if( objId.equals( Skid.NONE ) ) {
        continue;
      }
      ISkObject obj = srcCoreApi.objService().get( objId );
      destCoreApi.objService().defineObject( new DtoObject( obj.skid(), obj.attrs(), obj.rivets().map() ) );
      LoggerUtils.defaultLogger().info( "UploadObjsTask.actuallyUpload(...): define object %s", obj.skid() ); //$NON-NLS-1$
      count++;
    }

    // Завершение транзакции создания объектов
    dstTransactionService.commit();

    // Второй этап, создание связей между объектами
    for( Skid objId : aSrcObjSkidList ) {
      ISkClassInfo ci = srcCoreApi.sysdescr().findClassInfo( objId.classId() );
      for( IDtoLinkInfo li : ci.links().list() ) {
        IDtoLinkFwd lf = srcCoreApi.linkService().getLinkFwd( objId, li.id() );
        if( lf.rightSkids().size() != 0 ) {
          destCoreApi.linkService().setLink( lf );
        }
        else {
          if( li.linkConstraint().isExactCount() ) {
            LoggerUtils.errorLogger().warning( "Link %s has empty list", lf.gwid() ); //$NON-NLS-1$
          }
        }
      }
    }

    return count;
  }

  /**
   * Returns GW from argument but retains hierarchy validity.
   * <p>
   * Root class is retained too.
   *
   * @param aList {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - list to filter out
   * @return {@link IStridablesListEdit}&lt;{@link ISkClassInfo}&gt; - filtered list
   */
  private static IStridablesListEdit<ISkClassInfo> getGwClasses( IStridablesList<ISkClassInfo> aList ) {
    IStridablesListEdit<ISkClassInfo> ll = new StridablesList<>();
    for( ISkClassInfo cinf : aList ) {
      if( isGwClass( cinf ) || GW_ROOT_CLASS_ID.equals( cinf.id() ) ) {
        ll.add( cinf );
      }
    }
    return ensureHierarchyValidity( ll );
  }

  /**
   * Ensures that list of class has a valid hierarchy.
   * <p>
   * Hierarchy validity means that all classes in the list has all ancestor classes also included in the same list. Root
   * class with ID {@link IGwHardConstants#GW_ROOT_CLASS_ID} must also be included in the list.
   * <p>
   * Method adds to the list missing ancestor classes, if needed.
   *
   * @param aList {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - list to be checked
   * @return {@link IStridablesListEdit}&lt;{@link ISkClassInfo}&gt; - valid hierarchy list
   */
  private static IStridablesListEdit<ISkClassInfo> ensureHierarchyValidity( IStridablesList<ISkClassInfo> aList ) {
    IStridablesListEdit<ISkClassInfo> ll = new StridablesList<>();
    for( ISkClassInfo cinf : aList ) {
      ll.add( cinf );
      for( ISkClassInfo pinf : cinf.listSuperclasses( false ) ) {
        if( !aList.hasKey( pinf.id() ) ) {
          ll.add( pinf );
        }
      }
    }
    return ll;
  }

  private static boolean isGwClass( ISkClassInfo aCinf ) {
    if( aCinf.id().startsWith( ISkHardConstants.SK_ID + '.' ) ) {
      return false;
    }
    return true;
  }

  // private static boolean isSourceCodeDefinedClass( ISkClassInfo aCinf ) {
  // return OPDEF_SK_IS_SOURCE_CODE_DEFINED_CLASS.getValue( aCinf.params() ).asBool();
  // }

}
