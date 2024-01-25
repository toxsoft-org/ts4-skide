package org.toxsoft.skide.plugin.exconn.main;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnConstants.*;
import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnSharedResources.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.core.api.tasks.*;

/**
 * Code generation task meta-information.
 *
 * @author hazard157
 */
public final class UploadToServerTaskInfo
    extends GenericTaskInfo {

  /**
   * The task ID.
   */
  public static final String TASK_ID = SKIDE_FULL_ID + ".task.UploadToServer"; //$NON-NLS-1$

  public static final String OPID_IN_EXCONN_IDCHAIN = TASK_ID + ".inOp.ExconnIdchain"; //$NON-NLS-1$

  public static final IDataDef OPDEF_IN_EXCONN_IDCHAIN = DataDef.create( OPID_IN_EXCONN_IDCHAIN, VALOBJ, //
      TSID_NAME, "directory ", //
      TSID_DESCRIPTION, "", //
      TSID_KEEPER_ID, IdChain.KEEPER_ID
  // , //
  // FIXME OPDEF_EDITOR_FACTORY_NAME, ValedAvValedExconnIdchain.EDITOR //
  );

  /**
   * The singleton instance
   */
  public static final IGenericTaskInfo INSTANCE = new UploadToServerTaskInfo();

  /**
   * {@link ISkideTaskInputPreparator} implementation for this task.
   */
  public static final ISkideTaskInputPreparator INPUT_PREPARATOR = ( aInput, aSkideEnv, aWinContext ) -> {

    // TODO Auto-generated method stub

  };

  private UploadToServerTaskInfo() {
    super( TASK_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_TASK_UPLOAD, //
        TSID_DESCRIPTION, STR_TASK_UPLOAD_D, //
        TSID_ICON_ID, ICONID_TASK_UPLOAD //
    ) );
  }

}
