package org.toxsoft.skide.core.api.impl;

import java.util.concurrent.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.uskat.core.api.users.*;

/**
 * Base class to implement SkIDE task in plugins.
 *
 * @author hazard157
 */
public abstract class AbstractSkideUnitTask
    extends AbstractGenericTask
    implements ITsGuiContextable, ISkidePluginRelated {

  private static final String FMT_KEEPABLES_STORAGE_ITEM_ID = "task_config__%s"; //$NON-NLS-1$

  private final AbstractSkideUnit ownerUnit;

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
    addConfigOptionDefs( aCfgOptionDefs );
    String ksItemId = makeKeepablesStorageItemId( aInfo.id() );
    IKeepablesStorage unitStorage = plEnv().unitStorage( ownerUnit.id() );
    IOptionSet taskCfg = unitStorage.readItem( ksItemId, OptionSetKeeper.KEEPER, IOptionSet.NULL );
    // we'll guarantee that there will be only defined options with valid values
    OptionSetUtils.initOptionSet( opVals, aCfgOptionDefs );
    if( !OptionSetUtils.validateOptionSet( taskCfg, aCfgOptionDefs ).isError() ) {
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
  // To implement
  //

  @Override
  protected void afterOptionValuesUpdated() {
    // save options to the permanent storage
    String ksItemId = makeKeepablesStorageItemId( taskInfo().id() );
    IKeepablesStorage unitStorage = plEnv().unitStorage( ownerUnit.id() );
    unitStorage.writeItem( ksItemId, cfgOptionValues(), OptionSetKeeper.KEEPER );
  }

  @Override
  protected abstract void doRunSync( ITsContextRo aInput, ITsContext aOutput );

  @Override
  protected abstract Future<ITsContextRo> doRunAsync( ITsContextRo aInput, ITsContext aOutput );

}