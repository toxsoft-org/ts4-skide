package org.toxsoft.skide.core.api;

import org.toxsoft.skide.core.api.tasks.*;

/**
 * The SkIDE environment is the SkIDE-specific API entry point.
 * <p>
 * Reference to the instance is in the application context. Moreover, the fact that reference to the
 * {@link ISkideEnvironment} is in context tells the plug-in that it is running inside SkIDE, not another application.
 *
 * @author hazard157
 */
public interface ISkideEnvironment {

  /**
   * Returns the plugins registrator.
   *
   * @return {@link ISkidePluginsRegistrator} - the plugins registrator
   */
  ISkidePluginsRegistrator pluginsRegistrator();

  /**
   * Returns the common SkiDE project properties.
   *
   * @return {@link ISkideProjectProperties} - project properties
   */
  ISkideProjectProperties projectProperties();

  /**
   * Returns SkIDE task manager to run {@link ISkideUnit#listSupportedTasks()} tasks.
   *
   * @return {@link ISkideTaskRegistrator} - the SkIDE task manager
   */
  ISkideTaskRegistrator taskRegistrator();

}
