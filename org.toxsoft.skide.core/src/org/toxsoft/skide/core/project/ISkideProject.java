package org.toxsoft.skide.core.project;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * The SkIDE project main and the only access point.
 *
 * @author hazard157
 */
public interface ISkideProject {

  /**
   * Returns the information about project.
   *
   * @return {@link ISkideProjectInfo} - the project info
   */
  ISkideProjectInfo projectInfo();

  /**
   * Changes project info? any option except project ID.
   *
   * @param aParams {@link IOptionSet} - options to be changed
   */
  void setProjectInfo( IOptionSet aParams );

  // TODO API

}
