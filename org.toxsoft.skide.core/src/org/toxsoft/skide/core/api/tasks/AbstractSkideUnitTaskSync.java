package org.toxsoft.skide.core.api.tasks;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;

/**
 * {@link AbstractSkideUnitTask} implementing only the task synchronous running.
 * <p>
 * {@link #doRunAsync(ITsContextRo, ITsContext)} is implemented by calling {@link #doRunSync(ITsContextRo, ITsContext)}.
 * <p>
 * Important: this class could be used only when task execution is guaranteed to take a very short time (no more than a
 * few seconds).
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
  }

  @Override
  protected abstract void doRunSync( ITsContextRo aInput, ITsContext aOutput );

  @Override
  final protected Future<ITsContextRo> doRunAsync( ITsContextRo aInput, ITsContext aOutput ) {
    doRunSync( aInput, aOutput );
    return new CompletedFuture<>( aOutput );
  }

}
