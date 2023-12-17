package org.toxsoft.skide.task.codegen.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.task.codegen.*;
import org.toxsoft.skide.task.codegen.main.*;

/**
 * The plugin main addon.
 *
 * @author hazard157
 */
public class AddonSkideTaskCodegen
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkideTaskCodegen() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkideEnvironment skideEnv = aWinContext.get( ISkideEnvironment.class );
    TsInternalErrorRtException.checkNull( skideEnv );
    skideEnv.taskManager().registerTask( SkideCodegenTaskInfo.INSTANCE );
  }

}