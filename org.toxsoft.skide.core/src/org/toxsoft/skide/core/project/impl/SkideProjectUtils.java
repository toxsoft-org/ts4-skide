package org.toxsoft.skide.core.project.impl;

import org.toxsoft.skide.core.project.*;

/**
 * SkIDE project management utilisy methods.
 *
 * @author hazard157
 */
public class SkideProjectUtils {

  /**
   * Creates an empty project.
   *
   * @return {@link ISkideProject} - empty SkIDE project with
   */
  public static ISkideProject createEmptyProject() {
    return new SkideProject();
  }

  /**
   * No subclassing.
   */
  private SkideProjectUtils() {
    // nop
  }

}
