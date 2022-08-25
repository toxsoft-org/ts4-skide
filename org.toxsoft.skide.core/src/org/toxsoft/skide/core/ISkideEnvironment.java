package org.toxsoft.skide.core;

import org.toxsoft.skide.core.expoint.*;

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
   * Return the means to managere SkIDE project tree - the content and the visualizaion.
   *
   * @return {@link ISqTreeManager} - the project tree manager
   */
  ISqTreeManager projTreeManager();

}
