package org.toxsoft.skide.core.project;

import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Information about the project.
 *
 * @author hazard157
 */
public interface ISkideProjectInfo
    extends IStridable {

  /**
   * Notes (may be large text) on project.
   *
   * @return String - the notes
   */
  String notes();

}
