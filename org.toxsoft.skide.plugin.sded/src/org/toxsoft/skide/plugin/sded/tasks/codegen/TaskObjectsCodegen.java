package org.toxsoft.skide.plugin.sded.tasks.codegen;

import static org.toxsoft.skide.plugin.sded.tasks.codegen.IPackageConstants.*;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.plugin.sded.main.*;
import org.toxsoft.skide.task.codegen.main.*;

/**
 * SkIDE task {@link SkideTaskCodegenInfo} runner for {@link SkideUnitObjects}.
 *
 * @author hazard157
 */
public class TaskObjectsCodegen
    extends AbstractSkideUnitTask {

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskObjectsCodegen( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, SkideTaskCodegenInfo.INSTANCE, new StridablesList<>( OPDEF_GW_OBJECTS_INTERFACE_NAME ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractGenericTaskRunner
  //

  @Override
  protected ITsContextRo doRunSync( ITsContextRo aInput ) {
    // TODO Auto-generated method stub
    return super.doRunSync( aInput );
  }

  @Override
  protected Future<ITsContextRo> doRunAsync( ITsContextRo aInput, ITsContext aOutput ) {
    // TODO реализовать TaskObjectsCodegen.doRunAsync()
    throw new TsUnderDevelopmentRtException( "TaskObjectsCodegen.doRunAsync()" );
  }

  @Override
  protected ValidationResult doCanRun( ITsContextRo aInput ) {
    // TODO Auto-generated method stub
    return ValidationResult.error( "Under development" );
  }

}
