package org.toxsoft.skide.core.env;

import static org.toxsoft.skide.core.env.ISkideProjectPropertiesConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.workroom.*;

/**
 * The SkIDE workroom-specific properties are called "Project properties" for user convinience.
 * <p>
 * Porject properties are implemented as {@link IOptionSet} and are stored in
 * {@link ITsWorkroom#getApplicationStorage()}.
 * <p>
 * Fires an {@link IGenericChangeListener#onGenericChangeEvent(Object)} every time when properties are changed by
 * {@link #setProperties(IOptionSet)}.
 *
 * @author hazard157
 */
public interface ISkideProjectProperties
    extends IParameterized, IGenericChangeEventCapable {

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

  /**
   * Changes the project properties.
   * <p>
   * The argument may conain oply part of the properties. Unknown properties will be ignored.
   *
   * @param aProperties {@link IOptionSet} - changed properties
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setProperties( IOptionSet aProperties );

}
