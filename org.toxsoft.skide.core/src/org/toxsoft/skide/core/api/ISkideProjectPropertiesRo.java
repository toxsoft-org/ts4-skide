package org.toxsoft.skide.core.api;

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
public interface ISkideProjectPropertiesRo
    extends IParameterized, IGenericChangeEventCapable {

  /**
   * Returns the globally unique project ID.
   *
   * @return String - SkIDE project ID (an IDpath)
   */
  default String projId() {
    return OPDEF_SPP_PROJ_ID.getValue( params() ).asString();
  }

  /**
   * Returns the short project alias.
   *
   * @return String - SkIDE project alias (an IDname)
   */
  default String projAlias() {
    return OPDEF_SPP_ALIAS.getValue( params() ).asString();
  }

  /**
   * Returns the project short, human readable name.
   *
   * @return String - SkIDE project name
   */
  default String name() {
    return OPDEF_SPP_NAME.getValue( params() ).asString();
  }

  /**
   * Returns the project copyright like "Acme company, 2000-2023".
   *
   * @return String - project copyright string
   */
  default String projCopyright() {
    return OPDEF_SPP_COPYRIGHT.getValue( params() ).asString();
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
