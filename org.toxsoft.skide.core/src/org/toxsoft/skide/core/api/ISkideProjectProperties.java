package org.toxsoft.skide.core.api;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.api.ISkideProjectPropertiesConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.txtproj.lib.workroom.*;

/**
 * The SkIDE workroom-specific properties are called "Project properties" for user convenience.
 * <p>
 * Project properties are implemented as {@link IOptionSet} and are stored in
 * {@link ITsWorkroom#getApplicationStorage()}.
 * <p>
 * Fires an {@link IGenericChangeListener#onGenericChangeEvent(Object)} every time when properties are changed.
 *
 * @author hazard157
 */
public interface ISkideProjectProperties
    extends IParameterized, IGenericChangeEventCapable {

  /**
   * The section ID of the {@link ISkideProjectProperties} storage in {@link ITsWorkroom#getApplicationStorage()}.
   */
  String STORAGE_OPID_PROPECT_PROPS = SKIDE_FULL_ID + ".ProjectProperties"; //$NON-NLS-1$

  /**
   * Returns the project single line name.
   *
   * @return String - SkIDE project name
   */
  default String name() {
    return OPDEF_SPP_NAME.getValue( params() ).asString();
  }

  /**
   * Returns the project description.
   *
   * @return String - SkIDE project description
   */
  default String description() {
    return OPDEF_SPP_DESCRIPTION.getValue( params() ).asString();
  }

}
