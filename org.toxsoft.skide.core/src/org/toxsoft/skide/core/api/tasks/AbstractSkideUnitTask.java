package org.toxsoft.skide.core.api.tasks;

import java.util.concurrent.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * {@link IGenericTask} implementation for SkIDE units to be provided by {@link ISkideUnit#listSupportedTasks()}.
 *
 * @author hazard157
 */
public abstract class AbstractSkideUnitTask
    extends AbstractGenericTask
    implements ITsGuiContextable, ISkidePluginRelated, ISkConnected {

  private static final String FMT_KEEPABLES_STORAGE_ITEM_ID = "task_config__%s"; //$NON-NLS-1$

  private final AbstractSkideUnit ownerUnit;

  private final IStridablesListEdit<IDataDef> opDefs = new StridablesList<>();
  protected final IOptionSetEdit              opVals = new OptionSet();

  /**
   * Constructor for subclasses.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @param aInfo {@link IGenericTaskInfo} - the task info
   * @param aCfgOptionDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - the config options
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractSkideUnitTask( AbstractSkideUnit aOwnerUnit, IGenericTaskInfo aInfo,
      IStridablesList<IDataDef> aCfgOptionDefs ) {
    super( aInfo );
    TsNullArgumentRtException.checkNulls( aOwnerUnit, aCfgOptionDefs );
    ownerUnit = aOwnerUnit;
    opDefs.addAll( aCfgOptionDefs );
    String ksItemId = makeKeepablesStorageItemId( aInfo.id() );
    IKeepablesStorage unitStorage = plEnv().unitStorage( ownerUnit.id() );
    IOptionSet taskCfg = unitStorage.readItem( ksItemId, OptionSetKeeper.KEEPER, IOptionSet.NULL );
    // we'll guarantee that there will be only defined options with valid values
    OptionSetUtils.initOptionSet( opVals, opDefs );
    if( !OptionSetUtils.validateOptionSet( taskCfg, opDefs ).isError() ) {
      opVals.refreshSet( taskCfg );
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static String makeKeepablesStorageItemId( String aTaskId ) {
    return String.format( FMT_KEEPABLES_STORAGE_ITEM_ID, aTaskId );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  final public ITsGuiContext tsContext() {
    return ownerUnit.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // ISkidePluginRelated
  //

  @Override
  final public ISkideEnvironment skEnv() {
    return ownerUnit.skEnv();
  }

  @Override
  final public IPluginEnvironment plEnv() {
    return ownerUnit.plEnv();
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    ISkConnectionSupplier cs = tsContext().get( ISkConnectionSupplier.class );
    return cs.defConn();
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //
  /**
   * Returns the owner SkIDE unit.
   *
   * @return {@link ISkUser} - the owner unit
   */
  final public ISkideUnit ownerUnit() {
    return ownerUnit;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the task configuration options definitions.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - options definitions, may be an empty list
   */
  final public IStridablesList<IDataDef> getCfgOptionDefs() {
    return opDefs;
  }

  /**
   * Returns the configuration options values.
   * <p>
   * Options may contain only subset of the options defined by {@link #getCfgOptionDefs()}.
   *
   * @return {@link IOptionSet} - a configuration
   */
  final public IOptionSet getCfgOptionValues() {
    return opVals;
  }

  /**
   * Sets the configuration option values.
   * <p>
   * Argument may contain only subset of the options defined by {@link #getCfgOptionDefs()}.
   *
   * @param aValues {@link IOptionSet} - new configuration option values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link OptionSetUtils#checkOptionSet(IOptionSet, IStridablesList)}
   */
  final public void setCfgOptionValues( IOptionSet aValues ) {
    OptionSetUtils.checkOptionSet( aValues, opDefs );
    opVals.refreshSet( aValues );
    // save options to the permanent storage
    String ksItemId = makeKeepablesStorageItemId( taskInfo().id() );
    IKeepablesStorage unitStorage = plEnv().unitStorage( ownerUnit.id() );
    unitStorage.writeItem( ksItemId, getCfgOptionValues(), OptionSetKeeper.KEEPER );
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  @Override
  protected abstract void doRunSync( ITsContextRo aInput, ITsContext aOutput );

  @Override
  protected abstract Future<ITsContextRo> doRunAsync( ITsContextRo aInput, ITsContext aOutput );

}
