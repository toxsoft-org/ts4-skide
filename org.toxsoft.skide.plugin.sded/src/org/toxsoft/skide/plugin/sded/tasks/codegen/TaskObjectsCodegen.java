package org.toxsoft.skide.plugin.sded.tasks.codegen;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.skide.plugin.sded.tasks.codegen.IPackageConstants.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.plugin.sded.main.*;
import org.toxsoft.skide.task.codegen.main.*;

/**
 * SkIDE task {@link SkideTaskCodegenInfo} runner for {@link SkideUnitObjects}.
 *
 * @author hazard157
 */
public class TaskObjectsCodegen
    extends AbstractSkideUnitTaskSync {

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
  // AbstractSkideUnitTaskSync
  //

  @Override
  protected void doRunSync( ITsContextRo aInput, ITsContext aOutput ) {
    ILongOpProgressCallback lop = REFDEF_IN_PROGRESS_MONITOR.getRef( aInput );

    // TODO Auto-generated method stub
    lop.finished( ValidationResult.warn( "Sk-object code generation is under deveopment" ) );
  }

}
