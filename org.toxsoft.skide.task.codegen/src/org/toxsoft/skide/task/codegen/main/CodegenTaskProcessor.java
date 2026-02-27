package org.toxsoft.skide.task.codegen.main;

import static org.toxsoft.core.tsgui.rcp.valed.IValedFileConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.task.codegen.ISkideTaskCodegenConstants.*;
import static org.toxsoft.skide.task.codegen.ISkideTaskCodegenSharedResources.*;
import static org.toxsoft.skide.task.codegen.gen.ICodegenConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.task.codegen.gen.*;
import org.toxsoft.skide.task.codegen.gen.impl.*;

/**
 * Class prepares for code generation and run units supporting this task.
 *
 * @author hazard157
 */
public final class CodegenTaskProcessor
    extends SkideTaskProcessor {

  /**
   * The task ID.
   */
  public static final String TASK_ID = SKIDE_FULL_ID + ".task.CodeGenerator"; //$NON-NLS-1$

  /**
   * ID of the option {@link #OPDEF_IN_DERECTORY}.
   */
  public static final String OPID_IN_DIRECTORY = TASK_ID + ".inOp.Directory"; //$NON-NLS-1$

  /**
   * ID of the option {@link #OPDEF_IN_JAVA_PACKAGE}.
   */
  public static final String OPID_IN_JAVA_PACKAGE = TASK_ID + ".inOp.JavaPackage"; //$NON-NLS-1$

  /**
   * Input option: Generated files destination directory
   */
  public static final IDataDef OPDEF_IN_DERECTORY = DataDef.create3( OPID_IN_DIRECTORY, DT_DIRECTORY_FILE, //
      TSID_NAME, STR_IN_DERECTORY, //
      TSID_DESCRIPTION, STR_IN_DERECTORY_D, //
      TSID_IS_MANDATORY, AV_TRUE //
  );

  /**
   * Input option: The name of the Java package (value of the 'package' clause)
   */
  public static final IDataDef OPDEF_IN_JAVA_PACKAGE = DataDef.create3( OPID_IN_JAVA_PACKAGE, DDEF_IDPATH, //
      TSID_NAME, STR_IN_JAVA_PACKAGE, //
      TSID_DESCRIPTION, STR_IN_JAVA_PACKAGE_D, //
      TSID_IS_MANDATORY, AV_TRUE //
  );

  private static final IGenericTaskInfo TASK_INFO = new GenericTaskInfo( TASK_ID, OptionSetUtils.createOpSet( //
      TSID_NAME, STR_TASK_CODEGEN, //
      TSID_DESCRIPTION, STR_TASK_CODEGEN_D, //
      TSID_ICON_ID, ICONID_TASK_CODEGEN //
  ) ) {

    {
      inOps().add( OPDEF_IN_DERECTORY );
      inOps().add( OPDEF_IN_JAVA_PACKAGE );
      inRefs().put( REFDEF_CODEGEN_ENV.refKey(), REFDEF_CODEGEN_ENV );
    }

  };

  /**
   * The singleton instance
   */
  public static final SkideTaskProcessor INSTANCE = new CodegenTaskProcessor();

  private CodegenTaskProcessor() {
    super( TASK_INFO );
  }

  @Override
  protected void doBeforeRunTask( ITsContext aInput ) {
    File outDir = OPDEF_IN_DERECTORY.getValue( aInput.params() ).asValobj();
    String packageName = OPDEF_IN_JAVA_PACKAGE.getValue( aInput.params() ).asString();
    ICodegenEnvironment codegenEnv = new CodegenEnvironment( outDir, packageName );
    ICodegenConstants.REFDEF_CODEGEN_ENV.setRef( aInput, codegenEnv );
  }

}
