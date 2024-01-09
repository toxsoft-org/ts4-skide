package org.toxsoft.skide.task.codegen.main;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.gentask.*;

public class Task
    extends AbstractGenericTask {

  public Task( IGenericTaskInfo aTaskInfo ) {
    super( aTaskInfo );
    // TODO Auto-generated constructor stub
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
