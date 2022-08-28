package org.toxsoft.skide.core.main.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.qtree.mgr.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.skide.core.main.*;

/**
 * {@link ISkideEnvironment} implementation.
 *
 * @author hazard157
 */
public class SkideEnvironment
    implements ISkideEnvironment {

  private final IQTreeContributorsManager contributorsManager = new QTreeContributorsManager();

  /**
   * Constructor.
   */
  public SkideEnvironment() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ISkideEnvironment
  //

  @Override
  public IQRootNode createRootNode( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    ITsProject proj = aContext.get( ITsProject.class );
    TsInternalErrorRtException.checkNull( proj );
    ITsGuiContext ctx = new TsGuiContext( aContext );
    ctx.put( ISkideEnvironment.class, this );
    return new SkideRootNode( ctx, proj, IOptionSet.NULL );
  }

  @Override
  public IQTreeContributorsManager projTreeManager() {
    return contributorsManager;
  }

}
