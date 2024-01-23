package org.toxsoft.skide.task.codegen.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.task.codegen.*;
import org.toxsoft.skide.task.codegen.gen.*;
import org.toxsoft.skide.task.codegen.main.*;
import org.toxsoft.skide.task.codegen.valed.*;

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
    TsValobjUtils.registerKeeper( ECodegenJavaType.KEEPER_ID, ECodegenJavaType.KEEPER );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ISkideEnvironment skideEnv = aAppContext.get( ISkideEnvironment.class );
    TsInternalErrorRtException.checkNull( skideEnv );
    skideEnv.pluginsRegistrator().registerPlugin( SkidePluginTaskCodegen.INSTANCE );
    skideEnv.taskManager().registerTask( SkideTaskCodegenInfo.INSTANCE, SkideTaskCodegenInfo.INPUT_PREPARATOR );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkideTaskCodegenConstants.init( aWinContext );
    //
    IValedControlFactoriesRegistry vcfRegistry = aWinContext.get( IValedControlFactoriesRegistry.class );
    vcfRegistry.registerFactory( ValedStringJavaTypeName.FACTORY );
    vcfRegistry.registerFactory( ValedAvStringJavaTypeName.FACTORY );
  }

}
