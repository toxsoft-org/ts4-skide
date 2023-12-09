package org.toxsoft.skide.core.api;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * {@link ISkideProjectPropertiesRo} extension with editing methods.
 * <p>
 * Project properties are implemented as {@link IOptionSet} and are stored in
 * {@link ITsWorkroom#getApplicationStorage()}.
 * <p>
 * Fires an {@link IGenericChangeListener#onGenericChangeEvent(Object)} every time when properties are changed.
 *
 * @author hazard157
 */
public interface ISkideProjectProperties
    extends ISkideProjectPropertiesRo {

  /**
   * Returns the editing validator.
   *
   * @return {@link ITsValidationSupport}&lt;{@link ISkObjectServiceValidator}&gt; - the service validator
   */
  ITsValidationSupport<ISkideProjectPropertiesValidator> svs();

  /**
   * Changes the project properties.
   * <p>
   * The argument may contains only part of the properties. Unknown properties will be ignored.
   *
   * @param aProperties {@link IOptionSet} - changed properties
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed call to {@link #svs()} validator
   */
  void setProperties( IOptionSet aProperties );

}
