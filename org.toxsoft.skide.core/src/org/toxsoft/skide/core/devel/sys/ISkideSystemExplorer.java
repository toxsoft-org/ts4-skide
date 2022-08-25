package org.toxsoft.skide.core.devel.sys;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Data of USkat system (accessed witjh single connection) to be presented various views in SkIDE.
 *
 * @author hazard157
 */
public interface ISkideSystemExplorer
    extends IStridable {

  /**
   * Returns USkat connection to the system.
   *
   * @return {@link ISkConnection} - the connection
   */
  ISkConnection skConn();

}
