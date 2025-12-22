package org.toxsoft.skide.core.api;

/**
 * Mix-in interface of the SKIDE plugin related entities.
 * <p>
 *
 * @author hazard157
 */
public interface ISkidePluginRelated {

  /**
   * Returns the same environment as found in the application context.
   *
   * @return {@link ISkideEnvironment} - common environment for all plugins
   */
  ISkideEnvironment skEnv();

  /**
   * Returns the environment instance specifically created for this plugin.
   *
   * @return {@link IPluginEnvironment} - plugin-specific environment
   */
  IPluginEnvironment plEnv();

}
