package org.toxsoft.skide.core.api.impl;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.validator.*;

public class AbstractUnitTask
    extends AbstractGenericTask {

  protected AbstractUnitTask( IGenericTaskInfo aInfo ) {
    super( aInfo );
    // TODO Auto-generated constructor stub
  }

  @Override
  protected ValidationResult doCanRun( ITsContextRo aInput ) {
    // TODO Auto-generated method stub
    return super.doCanRun( aInput );
  }

  @Override
  protected ITsContextRo doRunSync( ITsContextRo aInput ) {
    // TODO Auto-generated method stub
    return super.doRunSync( aInput );
  }

  @Override
  protected Future<ITsContextRo> doRunAsync( ITsContextRo aInput, ITsContext aOutput ) {
    // TODO Auto-generated method stub
    return null;
  }

}
