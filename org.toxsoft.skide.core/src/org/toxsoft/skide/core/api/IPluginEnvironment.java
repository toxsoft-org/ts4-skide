package org.toxsoft.skide.core.api;

import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;
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

  /**
   * Returns the storage for the unit of this plugin.
   * <p>
   * Current implementation stores each units {@link IKeepablesStorage} in the separate file of the plugin directory
   * {@link ITsWorkroomStorage#rootDir()}.
   *
   * @param aUnitId String - the unit ID
   * @return {@link IKeepablesStorage} - the unit storage
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an ID path
   */
  IKeepablesStorage unitStorage( String aUnitId );

}
