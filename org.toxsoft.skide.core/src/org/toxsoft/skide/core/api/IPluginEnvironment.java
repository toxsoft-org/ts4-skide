package org.toxsoft.skide.core.api;

import org.toxsoft.core.txtproj.lib.workroom.*;

/**
 * SkIDE tools provided for the specific plugin.
 * <p>
 * Each SkIDE plugin has it's own instance of this interface.
 *
 * @author hazard157
 */
public interface IPluginEnvironment {

  /**
   * Returns the storage for the plugin.
   *
   * @return {@link ITsWorkroomStorage} - the plugin-specific storage
   */
  ITsWorkroomStorage wrStorage();

}
