package org.toxsoft.skide.task.upload.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.plugin.exconn.main.*;
import org.toxsoft.skide.task.upload.*;
import org.toxsoft.skide.task.upload.main.*;

/**
 * The plugin main addon.
 *
 * @author hazard157
 */
public class AddonSkideTaskUpload
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkideTaskUpload() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ISkideEnvironment skideEnv = aAppContext.get( ISkideEnvironment.class );
    TsInternalErrorRtException.checkNull( skideEnv );
    skideEnv.taskManager().registerTask( SkideTaskUploadInfo.INSTANCE );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    // nop
  }

}
