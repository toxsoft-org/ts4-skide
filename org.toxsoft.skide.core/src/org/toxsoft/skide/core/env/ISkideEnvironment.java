package org.toxsoft.skide.core.env;

/**
 * Exposes SkIDE internal for SkIDE plugins and other components.
 * <p>
 * Reference to the implementation must be in the application context. Moreover, the fact that reference is in context
 * tells the plug-in that it is running inside SkIDE, not an other application.
 *
 * @author hazard157
 */
public sealed interface ISkideEnvironment permits SkideEnvironment {

  /**
   * Returns the SkIDE project properties.
   *
   * @return {@link ISkideProjectProperties} - project properties
   */
  ISkideProjectProperties projectProperties();

  /**
   * Retruns the plugins registrator.
   *
   * @return {@link ISkidePluginsRegistrator} - the plugins registrator
   */
  ISkidePluginsRegistrator pluginsRegistrator();

}
