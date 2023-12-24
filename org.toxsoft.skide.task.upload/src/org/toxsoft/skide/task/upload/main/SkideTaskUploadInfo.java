package org.toxsoft.skide.task.upload.main;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.task.upload.main.ISkResources.*;
import static org.toxsoft.skide.task.upload.main.ISkideTaskUploadConstants.*;

import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.gentask.*;

/**
 * Code generation task meta-information.
 *
 * @author hazard157
 */
public final class SkideTaskUploadInfo
    extends GenericTaskInfo {

  /**
   * The singleton instance
   */
  public static final IGenericTaskInfo INSTANCE = new SkideTaskUploadInfo();

  private SkideTaskUploadInfo() {
    super( SKIDE_TASK_UPLOAD_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_TASK_UPLOAD, //
        TSID_DESCRIPTION, STR_TASK_UPLOAD_D//
    ) );
  }

}
