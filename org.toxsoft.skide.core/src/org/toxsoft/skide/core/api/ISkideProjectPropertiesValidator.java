package org.toxsoft.skide.core.api;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;

/**
 * Validates changes to the SkIDE project properties.
 * <p>
 * SkIDE plugins may want to restrict project properties change by adding instances of the validator to the
 * {@link ISkideProjectProperties#svs()}.
 *
 * @author hazard157
 */
public interface ISkideProjectPropertiesValidator
    extends ITsValidator<IOptionSet> {

  /**
   * The argument may contains only part of the properties. Unknown properties shall be ignored.
   * <p>
   * {@inheritDoc}
   */
  @Override
  ValidationResult validate( IOptionSet aValue );

}
