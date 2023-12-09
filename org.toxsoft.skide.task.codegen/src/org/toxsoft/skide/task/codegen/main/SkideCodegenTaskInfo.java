package org.toxsoft.skide.task.codegen.main;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.task.codegen.main.ISkResources.*;
import static org.toxsoft.skide.task.codegen.main.ISkideCodegenTaskConstants.*;

import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.gentask.*;

/**
 * Code generation task meta-information.
 *
 * @author hazard157
 */
public final class SkideCodegenTaskInfo
    extends GenericTaskInfo {

  /**
   * The singleton instance
   */
  public static final IGenericTaskInfo INSTANCE = new SkideCodegenTaskInfo();

  private SkideCodegenTaskInfo() {
    super( SKIDE_CODEGEN_TASK_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_TASK_CODEGEN, //
        TSID_DESCRIPTION, STR_TASK_CODEGEN_D//
    ) );
  }

}
