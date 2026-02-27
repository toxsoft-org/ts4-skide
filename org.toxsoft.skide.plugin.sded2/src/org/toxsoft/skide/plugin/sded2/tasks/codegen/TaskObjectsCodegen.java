package org.toxsoft.skide.plugin.sded2.tasks.codegen;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.skide.plugin.sded2.l10n.ISkidePluginSdedSharedResources.*;
import static org.toxsoft.skide.plugin.sded2.tasks.codegen.IPackageConstants.*;
import static org.toxsoft.skide.task.codegen.gen.ICodegenConstants.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.plugin.sded2.main.*;
import org.toxsoft.skide.task.codegen.gen.*;
import org.toxsoft.skide.task.codegen.gen.impl.*;
import org.toxsoft.skide.task.codegen.main.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * SkIDE task {@link CodegenTaskProcessor} runner for {@link SkideUnitSdedObjects}.
 *
 * @author hazard157
 */
public class TaskObjectsCodegen
    extends AbstractSkideUnitTaskSync {

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskObjectsCodegen( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, CodegenTaskProcessor.INSTANCE.taskInfo(),
        new StridablesList<>( OPDEF_GW_OBJECTS_INTERFACE_NAME ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static void writeClassObjects( ISkConnection aConn, ISkClassInfo aCinf, IJavaConstantsInterfaceWriter aJw ) {
    aJw.addCommentLine( StridUtils.printf( StridUtils.FORMAT_ID_NAME, aCinf ) );
    ISkObjectService objServ = aConn.coreApi().objService();
    IList<ISkObject> llSkObjs = objServ.listObjs( aCinf.id(), false );
    for( ISkObject skObj : llSkObjs ) {
      CodegenUtils.jwAddObjectSkid( aJw, skObj );
    }
    aJw.addSeparatorLine();
  }

  private static void writeConstants( ISkConnection aConn, IJavaConstantsInterfaceWriter aJw ) {
    ISkSysdescr sysdescr = aConn.coreApi().sysdescr();
    IStridablesList<ISkClassInfo> llClasses = sysdescr.listClasses();
    for( ISkClassInfo classInfo : llClasses ) {
      String claimerId = sysdescr.getSkClassImplementationInfo( classInfo.id() ).claimingServiceId();
      // write objects of only SYSDESCR claimed classes
      if( claimerId.equals( ISkSysdescr.SERVICE_ID ) ) {
        writeClassObjects( aConn, classInfo, aJw );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkideUnitTaskSync
  //

  @Override
  protected void doRunSync( ITsContextRo aInput, ITsContext aOutput ) {
    ILongOpProgressCallback lop = REFDEF_IN_PROGRESS_MONITOR.getRef( aInput );
    ICodegenEnvironment codegenEnv = REFDEF_CODEGEN_ENV.getRef( aInput );
    String interfaceName = OPDEF_GW_OBJECTS_INTERFACE_NAME.getValue( getCfgOptionValues() ).asString();
    IJavaConstantsInterfaceWriter jw = codegenEnv.createJavaInterfaceWriter( interfaceName );
    jw.setImports( new StringArrayList( "org.toxsoft.core.tslib.gw.skid" ) ); //$NON-NLS-1$
    ISkConnectionSupplier cs = tsContext().get( ISkConnectionSupplier.class );
    writeConstants( cs.defConn(), jw );
    jw.writeFile();
    lop.finished( ValidationResult.info( FMT_INFO_JAVA_INTERFACE_WAS_GENERATED, interfaceName ) );
    REFDEF_OUT_TASK_RESULT.setRef( aOutput, ValidationResult.SUCCESS );

    REFDEF_OUT_TASK_RESULT.setRef( aOutput, ValidationResult.error( "KJHJKHKJH" ) );
  }

}
