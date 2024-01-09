package org.toxsoft.skide.core.api.impl;

import java.util.concurrent.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.uskat.core.api.users.*;

/**
 * Base calss to implement SkIDE task in plugins.
 *
 * @author hazard157
 */
public class AbstracSkideUnitTask
    extends AbstractGenericTask
    implements ITsGuiContextable, ISkidePluginRelated {

  private final AbstractSkideUnit ownerUnit;

  protected AbstracSkideUnitTask( AbstractSkideUnit aOwnerUnit, IGenericTaskInfo aInfo ) {
    super( aInfo );
    TsNullArgumentRtException.checkNull( aOwnerUnit );
    ownerUnit = aOwnerUnit;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return ownerUnit.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // ISkidePluginRelated
  //

  @Override
  public ISkideEnvironment skEnv() {
    return ownerUnit.skEnv();
  }

  @Override
  public IPluginEnvironment plEnv() {
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
  public ISkideUnit ownerUnit() {
    return ownerUnit;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  // ------------------------------------------------------------------------------------
  // To implement
  //

  @Override
  protected Future<ITsContextRo> doRunAsync( ITsContextRo aInput, ITsContext aOutput ) {
    // TODO Auto-generated method stub
    return null;
  }

}
