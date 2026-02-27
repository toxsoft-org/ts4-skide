package org.toxsoft.skide.plugin.sded2.tasks.upload;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.core.tslib.bricks.strid.impl.StridUtils.*;
import static org.toxsoft.core.tslib.gw.IGwHardConstants.*;
import static org.toxsoft.skide.plugin.exconn.main.UploadToServerTaskProcessor.*;
import static org.toxsoft.skide.plugin.sded2.l10n.ISkidePluginSdedSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.gw.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.plugin.exconn.main.*;
import org.toxsoft.skide.plugin.sded2.main.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * SkIDE task {@link UploadToServerTaskProcessor} runner for {@link SkideUnitSdedClasses}.
 * <p>
 * Classes upload algorithm:
 * <ul>
 * <li>llToUpload = apply user-defined selection (what to upload) to the all source classes;</li>
 * <li>llToUpload = remove non-GW and source code defined classes;</li>
 * <li>llToUpload = add parent classes so list contains non-broken hierarchy;</li>
 * <li>llDest = all classes of destination sysdescr;</li>
 * <li>compare source code classes from llToUpload to the llDest, if there is SIGNIFICANT diffs, return error;</li>
 * <li>llDest = remove non-GW classes, retain only parents for a valid hierarchy;</li>
 * <li>compare llToUpload to the llDest and invoke comparison dialog for user to select what to do;</li>
 * <li>???.</li>
 * </ul>
 * Notes: GW (Green World) classes are classes claimed by {@link ISkSysdescr} service, so for <b><i>non-GW</i></b>
 * classes method {@link ISkSysdescr#getSkClassImplementationInfo(String)} returns any ID except
 * {@link ISkSysdescr#SERVICE_ID}. Source code defined classes are hard-coded in Java source source code. The option
 * {@link ISkHardConstants#OPDEF_SK_IS_SOURCE_CODE_DEFINED_CLASS} determines if class is a source code defined one.
 * <p>
 * Task may invokes several dialog windows that require the user to make decisions if any ambiguity or warning is
 * detected during upload.
 *
 * @author hazard157
 */
public class TaskClassesUpload
    extends AbstractSkideUnitTaskSync {

  private final ClassesUploadSelectionRules uploadSelectionRules = new ClassesUploadSelectionRules();

  private ISkCoreApi srcCoreApi  = null;
  private ISkCoreApi destCoreApi = null;

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskClassesUpload( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, UploadToServerTaskProcessor.INSTANCE.taskInfo(), //
        new StridablesList<>( /* No cfg ops */ ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static boolean isSourceCodeDefinedClass( ISkClassInfo aCinf ) {
    return OPDEF_SK_IS_SOURCE_CODE_DEFINED_CLASS.getValue( aCinf.params() ).asBool();
  }

  private static boolean isGwClass( ISkClassInfo aCinf, ISkCoreApi aCoreApi ) {
    if( isSourceCodeDefinedClass( aCinf ) ) {
      return false;
    }
    // check class is Green World class
    String claimServiceId = aCoreApi.sysdescr().getSkClassImplementationInfo( aCinf.id() ).claimingServiceId();
    return claimServiceId.equals( ISkSysdescr.SERVICE_ID );
  }

  /**
   * Filters out classes to upload according to the user-specified rules.
   *
   * @param aList {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - list to filter out
   * @return {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - filtered list
   */
  private IStridablesList<ISkClassInfo> applyUserDefinedUploadSelectionRules( IStridablesList<ISkClassInfo> aList ) {
    IStridablesListEdit<ISkClassInfo> ll = new StridablesList<>();
    for( ISkClassInfo cinf : aList ) {
      if( uploadSelectionRules.accept( cinf ) || GW_ROOT_CLASS_ID.equals( cinf.id() ) ) {
        ll.add( cinf );
      }
    }
    return ll;
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

  /**
   * Remove non-GW and source defined code classes from argument but retains hierarchy validity.
   * <p>
   * Root class is retained too.
   *
   * @param aList {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - list to filter out
   * @return {@link IStridablesListEdit}&lt;{@link ISkClassInfo}&gt; - filtered list
   */
  private IStridablesListEdit<ISkClassInfo> removeNonGwSrcCodeClasses( IStridablesList<ISkClassInfo> aList ) {
    IStridablesListEdit<ISkClassInfo> ll = new StridablesList<>();
    for( ISkClassInfo cinf : aList ) {
      if( isGwClass( cinf, srcCoreApi ) || GW_ROOT_CLASS_ID.equals( cinf.id() ) ) {
        ll.add( cinf );
      }
    }
    return ensureHierarchyValidity( ll );
  }

  /**
   * Checks if source code defined classes from a list have a significant difference.
   * <p>
   * All source code defined classes from <code>aSrc</code> list is checked against destination classes list:
   * <ul>
   * <li>check for error: destination must contain the counterpart class with the same ID;</li>
   * <li>check for error: the counterpart class must be a source code defined class too;</li>
   * <li>check for error: the counterpart class must be claimed by the same service;</li>
   * <li>check for error: the counterpart class ancestors list must match the source class ancestors;</li>
   * <li>all other differences between two {@link ISkClassInfo} are treated as a warning.</li>
   * </ul>
   *
   * @param aSrc {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - list to be checked
   * @param aDest {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - all classes of destination
   * @return {@link ValidationResult} - the check result
   */
  private ValidationResult checkSourceCodeDefinedClassesAreDifferent( IStridablesList<ISkClassInfo> aSrc,
      IStridablesList<ISkClassInfo> aDest ) {
    for( ISkClassInfo ciSrc : aSrc ) {
      // check only source code classes
      if( isSourceCodeDefinedClass( ciSrc ) ) {

        // FIXME TaskClassesUpload.checkSourceCodeDefinedClassesAreDifferent()

      }
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Check that non-source code classes from <code>aSrc</code> have valid destination.
   * <p>
   * All non-source code defined classes from <code>aSrc</code> list is checked against destination classes list:
   * <ul>
   * <li>check for error: destination has the class with the same ID that is defined by source code;</li>
   * <li>check for error: destination has the class with the same ID that is claimed not by {@link ISkSysdescr}.</li>
   * </ul>
   *
   * @param aSrcList {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - list to be checked
   * @param aDestList {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - all classes of destination
   * @return {@link ValidationResult} - the check result
   */
  private ValidationResult checkUploadableClassesHaveValidDestination( IStridablesList<ISkClassInfo> aSrcList,
      IStridablesList<ISkClassInfo> aDestList ) {
    for( ISkClassInfo ciSrc : aSrcList ) {
      // check only non-source code classes (note: here we have only GW classes)
      if( !isSourceCodeDefinedClass( ciSrc ) ) {
        ISkClassInfo ciDest = aDestList.findByKey( ciSrc.id() );
        if( ciDest != null ) {
          // can't overwrite source code defined destination
          if( isSourceCodeDefinedClass( ciDest ) ) {
            String s = printf( FORMAT_ID_NAME, ciSrc );
            return ValidationResult.error( FMT_ERR_DEST_CLASS_ID_SOURCE_CODE_DEFINED, s );
          }
          // can't overwrite non-GW destination
          String claimServiceId = destCoreApi.sysdescr().determineClassClaimingServiceId( ciDest.id() );
          if( !claimServiceId.equals( ISkSysdescr.SERVICE_ID ) ) {
            String s = printf( FORMAT_ID_NAME, ciSrc );
            return ValidationResult.error( FMT_ERR_DEST_CLASS_ID_NON_GW, s );
          }
        }
      }
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Invokes source vs destination classes comparison dialog and returns classes to be uploaded.
   *
   * @param aSrcList {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - classes, planned to upload
   * @param aDestList {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - counterpart classes of destination and more
   * @return {@link IStridablesListEdit}&lt;{@link ISkClassInfo}&gt; - classes selected to upload
   */
  private IStridablesListEdit<ISkClassInfo> invokeCompareDialogAndSelect( IStridablesList<ISkClassInfo> aSrcList,
      IStridablesList<ISkClassInfo> aDestList ) {

    // TODO TaskClassesUpload.invokeCompareDialogAndSlecet()

    // TODO ensure selected classes does not violate destination integrity
    return new StridablesList<>( aSrcList );
  }

  /**
   * Returns the argument list sorted so that for any class all of its ancestors appear before it in the list.
   *
   * @param aList {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - classes list to sort
   * @return {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - the sorted list
   */
  private IStridablesList<ISkClassInfo> sortAncestorsFirst( IStridablesList<ISkClassInfo> aList ) {
    IStridablesListEdit<ISkClassInfo> src = new StridablesList<>( aList );
    // new list must start with the root class
    IStridablesListEdit<ISkClassInfo> ll = new StridablesList<>();
    ll.add( src.removeById( GW_ROOT_CLASS_ID ) );
    for( ISkClassInfo cinf : aList ) {
      for( ISkClassInfo pinf : cinf.listSuperclasses( true ) ) {
        if( !ll.hasKey( pinf.id() ) ) {
          ll.add( src.removeById( pinf.id() ) );
        }
      }
    }
    return ll;
  }

  /**
   * Actually uploads classes - defines specified classes in the destination {@link ISkSysdescr} service.
   *
   * @param aList {@link IStridablesList}&lt;{@link ISkClassInfo}&gt; - classes to upload
   */
  private int actuallyUpload( IStridablesList<ISkClassInfo> aList ) {
    IStridablesList<ISkClassInfo> src = sortAncestorsFirst( aList );
    int count = 0;
    for( ISkClassInfo skCinf : src ) {
      if( skCinf.id().equals( GW_ROOT_CLASS_ID ) || !isGwClass( skCinf, srcCoreApi ) ) {
        continue;
      }
      IDtoClassInfo dtoCinf = DtoClassInfo.createFromSk( skCinf, true );
      destCoreApi.sysdescr().defineClass( dtoCinf );
      ++count;
    }
    return count;
  }

  private int uploadClasses() {
    IStridablesList<ISkClassInfo> llToUpload = srcCoreApi.sysdescr().listClasses();
    // prepare list of classes to upload
    llToUpload = applyUserDefinedUploadSelectionRules( llToUpload );
    llToUpload = removeNonGwSrcCodeClasses( llToUpload );

    // check upload can be started
    IStridablesList<ISkClassInfo> llDestList = destCoreApi.sysdescr().listClasses();
    ValidationResult vr = checkSourceCodeDefinedClassesAreDifferent( llToUpload, llDestList );
    if( TsDialogUtils.askContinueOnValidation( getShell(), vr,
        MSG_ASK_CONTINUE_CLASSES_UPLOAD_ON_WARN ) != ETsDialogCode.YES ) {
      return 0;
    }
    vr = checkUploadableClassesHaveValidDestination( llToUpload, llDestList );
    if( TsDialogUtils.askContinueOnValidation( getShell(), vr,
        MSG_ASK_CONTINUE_CLASSES_UPLOAD_ON_WARN ) != ETsDialogCode.YES ) {
      return 0;
    }
    llDestList = removeNonGwSrcCodeClasses( llDestList );
    llToUpload = invokeCompareDialogAndSelect( llToUpload, llDestList );
    // actually upload classes
    return actuallyUpload( llToUpload );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkideUnitTaskSync
  //

  @SuppressWarnings( "boxing" )
  @Override
  protected void doRunSync( ITsContextRo aInput, ITsContext aOutput ) {
    ILongOpProgressCallback lop = REFDEF_IN_PROGRESS_MONITOR.getRef( aInput );
    uploadSelectionRules.loadFromOptions( getCfgOptionValues() );
    ISkConnection srcConn = tsContext().get( ISkConnectionSupplier.class ).defConn();
    srcCoreApi = srcConn.coreApi();
    ISkConnection destConn = REFDEF_IN_OPEN_SK_CONN.getRef( aInput );
    destCoreApi = destConn.coreApi();
    int uploadedClassesCount = uploadClasses();
    lop.finished( ValidationResult.info( FMT_INFO_CLASSES_UPLOADED, uploadedClassesCount ) );
    REFDEF_OUT_TASK_RESULT.setRef( aOutput, ValidationResult.SUCCESS );
  }

}
