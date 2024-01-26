package org.toxsoft.skide.core.api.tasks;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * {@link ISkideTaskRegistrator} implementation.
 *
 * @author hazard157
 */
public class SkideTaskRegistrator
    implements ISkideTaskRegistrator, ICloseable {

  // FIXME add tasks closing

  private final SkideEnvironment skideEnv;

  private final IStringMapEdit<SkideTaskProcessor> tpMap = new StringMap<>();

  /**
   * Constructor.
   *
   * @param aEnv {@link SkideEnvironment} - the owner environment
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkideTaskRegistrator( SkideEnvironment aEnv ) {
    TsNullArgumentRtException.checkNull( aEnv );
    skideEnv = aEnv;
  }

  // ------------------------------------------------------------------------------------
  // internal API
  //

  @SuppressWarnings( "javadoc" )
  public void papiInitialize( ITsGuiContext aContext, ISkideEnvironment aSkEnv ) {
    TsNullArgumentRtException.checkNulls( aContext, aSkEnv );
    for( SkideTaskProcessor p : tpMap ) {
      p.papiInitialize( aContext, skideEnv );
    }
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    for( SkideTaskProcessor p : tpMap ) {
      p.close();
    }
  }

  // ------------------------------------------------------------------------------------
  // ISkideGenericTaskManager
  //

  @Override
  public IStringMap<SkideTaskProcessor> getRegisteredProcessors() {
    return tpMap;
  }

  @Override
  public IStridablesList<ISkideUnit> listCapableUnits( String aTaskId ) {
    TsNullArgumentRtException.checkNull( aTaskId );
    IStridablesListEdit<ISkideUnit> ll = new StridablesList<>();
    for( ISkideUnit u : skideEnv.pluginsRegistrator().listUnits() ) {
      if( u.listSupportedTasks().hasKey( aTaskId ) ) {
        ll.add( u );
      }
    }
    return ll;
  }

  @Override
  public void registerTaskProcessor( SkideTaskProcessor aProcessor ) {
    TsNullArgumentRtException.checkNull( aProcessor );
    TsItemAlreadyExistsRtException.checkTrue( tpMap.hasKey( aProcessor.taskInfo().id() ) );
    tpMap.put( aProcessor.taskInfo().id(), aProcessor );
  }

}
