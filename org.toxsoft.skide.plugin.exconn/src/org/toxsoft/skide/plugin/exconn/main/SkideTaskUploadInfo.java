package org.toxsoft.skide.plugin.exconn.main;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnConstants.*;
import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnSharedResources.*;

import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.skide.core.api.*;

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

  public static final ISkideTaskInputPreparator INPUT_PREPARATOR = ( aInput, aSkideEnv, aWinContext ) -> {

    // TODO Auto-generated method stub

  };

  private SkideTaskUploadInfo() {
    super( SKIDE_TASK_UPLOAD_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_TASK_UPLOAD, //
        TSID_DESCRIPTION, STR_TASK_UPLOAD_D, //
        TSID_ICON_ID, ICONID_TASK_UPLOAD //
    ) );
  }

}
