package org.toxsoft.skide.task.codegen.main;

import static org.toxsoft.core.tsgui.rcp.valed.IValedFileConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.task.codegen.ISkideTaskCodegenSharedResources.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.gentask.*;

/**
 * Code generation task meta-information.
 *
 * @author hazard157
 */
public final class SkideTaskCodegenInfo
    extends GenericTaskInfo {

  /**
   * The task ID.
   */
  public static final String TASK_ID = SKIDE_FULL_ID + ".task.CodeGenerator"; //$NON-NLS-1$

  public static final String INID_DIRECTORY    = TASK_ID + ".inOp.Directory";   //$NON-NLS-1$
  public static final String INID_JAVA_PACKAGE = TASK_ID + ".inOp.JavaPackage"; //$NON-NLS-1$

  public static final IDataDef INOP_DERECTORY = DataDef.create3( INID_DIRECTORY, DT_DIRECTORY_FILE, //
      TSID_NAME, "directory ", //
      TSID_DESCRIPTION, "" //
  );

  public static final IDataDef INOP_JAVA_PACKAGE = null;

  /**
   * The singleton instance
   */
  public static final IGenericTaskInfo INSTANCE = new SkideTaskCodegenInfo();

  private SkideTaskCodegenInfo() {
    super( SkideTaskCodegenInfo.TASK_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_TASK_CODEGEN, //
        TSID_DESCRIPTION, STR_TASK_CODEGEN_D//
    ) );
    inOps().add( INOP_DERECTORY );
    // inOps().add( INOP_JAVA_PACKAGE );
  }

}
