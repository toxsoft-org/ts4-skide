package org.toxsoft.skide.core.api.impl;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;

/**
 * {@link AbstractSkideUnitTask} to implement task run only synchronously.
 * <p>
 * {@link #doRunAsync(ITsContextRo, ITsContext)} is implemented by calling {@link #doRunSync(ITsContextRo, ITsContext)}.
 * <p>
 * Important: this class could be used only when task execution is guaranteed to take a very small time (not more than
 * 0.1-0.3 second.
 *
 * @author hazard157
 */
public abstract class AbstractSkideUnitTaskSync
    extends AbstractSkideUnitTask {

  /**
   * Constructor for subclasses.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @param aInfo {@link IGenericTaskInfo} - the task info
   * @param aCfgOptionDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - the config options
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractSkideUnitTaskSync( AbstractSkideUnit aOwnerUnit, IGenericTaskInfo aInfo,
      IStridablesList<IDataDef> aCfgOptionDefs ) {
    super( aOwnerUnit, aInfo, aCfgOptionDefs );
    // TODO Auto-generated constructor stub
  }

  @Override
  protected abstract void doRunSync( ITsContextRo aInput, ITsContext aOutput );

  @Override
  final protected Future<ITsContextRo> doRunAsync( ITsContextRo aInput, ITsContext aOutput ) {
    doRunSync( aInput, aOutput );
    return new CompletedFuture<>( aOutput );
  }

}
