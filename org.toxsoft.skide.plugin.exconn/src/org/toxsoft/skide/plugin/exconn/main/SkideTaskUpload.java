package org.toxsoft.skide.plugin.exconn.main;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericTask} implementation of the upload task.
 *
 * @author hazard157
 */
class SkideTaskUpload
    extends AbstractGenericTask {

  public SkideTaskUpload() {
    super( SkideTaskUploadInfo.INSTANCE );
  }

  @Override
  protected ITsContextRo doRunSync( ITsContextRo aInput ) {
    // TODO реализовать TaskUpload.doRunSync()
    throw new TsUnderDevelopmentRtException( "TaskUpload.doRunSync()" );
  }

  @Override
  protected Future<ITsContextRo> doRunAsync( ITsContextRo aInput, ITsContext aOutput ) {
    // TODO реализовать TaskUpload.doRunAsync()
    throw new TsUnderDevelopmentRtException( "TaskUpload.doRunAsync()" );
  }

}
