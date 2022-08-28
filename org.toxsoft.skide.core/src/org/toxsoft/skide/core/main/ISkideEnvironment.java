package org.toxsoft.skide.core.main;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.qtree.mgr.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Access point to the SkIDE API.
 * <p>
 * Reference to the implementation must be in the application context. Moreover, the fact that reference is in context
 * means that some module is running in SkIDE, not another application.
 *
 * @author hazard157
 */
public interface ISkideEnvironment {

  /**
   * Creates new instance of the SkIDE project tree root node.
   * <p>
   * Root node conains entity of type {@link ITsProject} - the currently open project in SkIDE.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link IQRootNode} - created root node
   */
  IQRootNode createRootNode( ITsGuiContext aContext );

  /**
   * Return the means to managere SkIDE project tree - the content and the visualizaion.
   *
   * @return {@link IQTreeContributorsManager} - the project tree contributions manager
   */
  IQTreeContributorsManager projTreeManager();

}
