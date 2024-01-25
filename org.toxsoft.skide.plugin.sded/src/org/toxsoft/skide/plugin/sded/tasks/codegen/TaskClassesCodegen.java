package org.toxsoft.skide.plugin.sded.tasks.codegen;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.core.tslib.gw.IGwHardConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;
import static org.toxsoft.skide.plugin.sded.ISkidePluginSdedSharedResources.*;
import static org.toxsoft.skide.plugin.sded.tasks.codegen.IPackageConstants.*;
import static org.toxsoft.skide.task.codegen.gen.ICodegenConstants.*;
import static org.toxsoft.skide.task.codegen.gen.impl.CodegenUtils.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.plugin.sded.main.*;
import org.toxsoft.skide.task.codegen.gen.*;
import org.toxsoft.skide.task.codegen.main.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * SkIDE task {@link SkideTaskCodegenInfo} runner for {@link SkideUnitClasses}.
 *
 * @author hazard157
 */
public class TaskClassesCodegen
    extends AbstractSkideUnitTaskSync {

  /**
   * TODO what to do with prefixes? Maybe as config options?<br>
   * TODO implement SkIDE v2 behavior - store Java constant names together with SYSDESCR entities<br>
   * TODO need to check that class generated constant IDs may be duplicated<br>
   */

  private static final String PREFIX_CLASS   = "CLSID"; //$NON-NLS-1$
  private static final String PREFIX_ATTR    = "ATRID"; //$NON-NLS-1$
  private static final String PREFIX_RTDATA  = "RTDID"; //$NON-NLS-1$
  private static final String PREFIX_RIVET   = "RIVID"; //$NON-NLS-1$
  private static final String PREFIX_LINK    = "LNKID"; //$NON-NLS-1$
  private static final String PREFIX_COMMAND = "CMDID"; //$NON-NLS-1$
  private static final String PREFIX_EVENT   = "EVNID"; //$NON-NLS-1$
  private static final String PREFIX_CLOB    = "CLBID"; //$NON-NLS-1$

  private static final IMap<ESkClassPropKind, String> PROP_PREFIX_MAP;

  static {
    IMapEdit<ESkClassPropKind, String> map = new ElemMap<>();
    map.put( ESkClassPropKind.ATTR, PREFIX_ATTR );
    map.put( ESkClassPropKind.RTDATA, PREFIX_RTDATA );
    map.put( ESkClassPropKind.RIVET, PREFIX_RIVET );
    map.put( ESkClassPropKind.LINK, PREFIX_LINK );
    map.put( ESkClassPropKind.CMD, PREFIX_COMMAND );
    map.put( ESkClassPropKind.EVENT, PREFIX_EVENT );
    map.put( ESkClassPropKind.CLOB, PREFIX_CLOB );
    PROP_PREFIX_MAP = map;
  }

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskClassesCodegen( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, SkideTaskCodegenInfo.INSTANCE, new StridablesList<>( OPDEF_GW_CLASSES_INTERFACE_NAME ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static void writeClassProps( ISkClassProps<?> aProps, IJavaConstantsInterfaceWriter aJw ) {
    String prefix = PROP_PREFIX_MAP.getByKey( aProps.kind() );
    for( IDtoClassPropInfoBase prop : aProps.listSelf() ) {
      String cn = makeJavaConstName( prefix, prop.id() );
      aJw.addConstString( cn, prop.id(), prop.nmName() );
    }
  }

  private static void writeClass( ISkClassInfo aCinf, IJavaConstantsInterfaceWriter aJw ) {
    aJw.addCommentLine( StridUtils.printf( StridUtils.FORMAT_ID_NAME, aCinf ) );
    String cn = makeJavaConstName( PREFIX_CLASS, aCinf.id() );
    aJw.addConstString( cn, aCinf.id(), EMPTY_STRING );
    for( ESkClassPropKind k : PROP_PREFIX_MAP.keys() ) {
      ISkClassProps<?> props = aCinf.props( k );
      writeClassProps( props, aJw );
    }
  }

  private static void writeConstants( ISkConnection aConn, IJavaConstantsInterfaceWriter aJw ) {
    ISkSysdescr sysdescr = aConn.coreApi().sysdescr();
    IStridablesList<ISkClassInfo> llClasses = sysdescr.listClasses();
    for( ISkClassInfo classInfo : llClasses ) {
      String claimerId = sysdescr.determineClassClaimingServiceId( classInfo.id() );
      // write only SYSDESCR claimed classes (and of course, we don't need a root class)
      if( claimerId.equals( ISkSysdescr.SERVICE_ID ) && !classInfo.id().equals( GW_ROOT_CLASS_ID ) ) {
        writeClass( classInfo, aJw );
        aJw.addSeparatorLine();
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
    String interfaceName = OPDEF_GW_CLASSES_INTERFACE_NAME.getValue( aInput.params() ).asString();
    IJavaConstantsInterfaceWriter jw = codegenEnv.createJavaInterfaceWriter( interfaceName );
    ISkConnectionSupplier cs = tsContext().get( ISkConnectionSupplier.class );
    writeConstants( cs.defConn(), jw );
    jw.writeFile();
    lop.finished( ValidationResult.info( FMT_INFO_JAVA_INTERFACE_WAS_GENERATED, interfaceName ) );
  }

}
