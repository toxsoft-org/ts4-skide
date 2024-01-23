package org.toxsoft.skide.plugin.sded.tasks.codegen;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.core.tslib.bricks.strid.impl.StridUtils.*;
import static org.toxsoft.skide.plugin.sded.ISkidePluginSdedSharedResources.*;
import static org.toxsoft.skide.plugin.sded.tasks.codegen.IPackageConstants.*;
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
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.plugin.sded.main.*;
import org.toxsoft.skide.task.codegen.gen.*;
import org.toxsoft.skide.task.codegen.main.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * SkIDE task {@link SkideTaskCodegenInfo} runner for {@link SkideUnitObjects}.
 *
 * @author hazard157
 */
public class TaskObjectsCodegen
    extends AbstractSkideUnitTaskSync {

  private static final String PREFIX_OBJECT = "SKID"; //$NON-NLS-1$

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskObjectsCodegen( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, SkideTaskCodegenInfo.INSTANCE, new StridablesList<>( OPDEF_GW_OBJECTS_INTERFACE_NAME ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static void writeClassObjects( ISkConnection aConn, ISkClassInfo aCinf, IJavaConstantsInterfaceWriter aJw ) {
    aJw.addCommentLine( StridUtils.printf( StridUtils.FORMAT_ID_NAME, aCinf ) );
    ISkObjectService objServ = aConn.coreApi().objService();
    IList<ISkObject> llSkObjs = objServ.listObjs( aCinf.id(), false );
    for( ISkObject skObj : llSkObjs ) {
      // value of the constant is "new Skid( classId, strid )";
      String classId = skObj.skid().classId();
      String strid = skObj.skid().strid();
      String rawConstValue = String.format( "new Skid( \"%s\", \"%s\" )", classId, strid ); //$NON-NLS-1$
      // name of the constant is "SKID_CLASS_ID___OBJ_STRID"
      String nClassId = classId.replace( CHAR_ID_PATH_DELIMITER, '_' ).toUpperCase();
      String nStrid = strid.replace( CHAR_ID_PATH_DELIMITER, '_' ).toUpperCase();
      String cnObj = String.format( "%s_%s___%s", PREFIX_OBJECT, nClassId, nStrid ); //$NON-NLS-1$
      // write line "Skid SKID_CLASS_ID___OBJ_STRID = new Skid( classId, strid )".
      aJw.addConstOther( "Skid", cnObj, rawConstValue, skObj.nmName() ); //$NON-NLS-1$
    }
    aJw.addSeparatorLine();
  }

  private static void writeConstants( ISkConnection aConn, IJavaConstantsInterfaceWriter aJw ) {
    ISkSysdescr sysdescr = aConn.coreApi().sysdescr();
    IStridablesList<ISkClassInfo> llClasses = sysdescr.listClasses();
    for( ISkClassInfo classInfo : llClasses ) {
      String claimerId = sysdescr.determineClassClaimingServiceId( classInfo.id() );
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
    String interfaceName = OPDEF_GW_OBJECTS_INTERFACE_NAME.getValue( aInput.params() ).asString();
    IJavaConstantsInterfaceWriter jw = codegenEnv.createJavaInterfaceWriter( interfaceName );
    jw.setImports( new StringArrayList( "org.toxsoft.core.tslib.gw.skid" ) ); //$NON-NLS-1$
    ISkConnectionSupplier cs = tsContext().get( ISkConnectionSupplier.class );
    writeConstants( cs.defConn(), jw );
    jw.writeFile();
    lop.finished( ValidationResult.info( FMT_INFO_JAVA_INTERFACE_WAS_GENERATED, interfaceName ) );
    // TODO Auto-generated method stub
    lop.finished( ValidationResult.warn( "Sk-object code generation is under deveopment" ) );
  }

}
