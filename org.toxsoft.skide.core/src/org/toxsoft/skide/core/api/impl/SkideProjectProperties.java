package org.toxsoft.skide.core.api.impl;

import static org.toxsoft.skide.core.api.ISkideProjectPropertiesConstants.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.skide.core.api.*;

/**
 * {@link ISkideEnvironment#projectProperties()} implementation.
 *
 * @author hazard157
 */
public final class SkideProjectProperties
    implements ISkideProjectProperties {

  /**
   * {@link ISkideProjectProperties#svs()} implementation.
   *
   * @author hazard157
   */
  static class Svs
      extends AbstractTsValidationSupport<ISkideProjectPropertiesValidator>
      implements ISkideProjectPropertiesValidator {

    @Override
    public ValidationResult validate( IOptionSet aValue ) {
      TsNotAllEnumsUsedRtException.checkNull( aValue );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkideProjectPropertiesValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.validate( aValue ) );
        if( vr.isError() ) {
          break;
        }
      }
      return vr;
    }

    @Override
    public ISkideProjectPropertiesValidator validator() {
      return this;
    }

  }

  private ISkideProjectPropertiesValidator builtinValidator = v -> OptionSetUtils.validateOptionSet( v, ALL_SPP_OPS );

  private final GenericChangeEventer eventer;
  private final Svs                  svs    = new Svs();
  private final IOptionSetEdit       params = new OptionSet();

  private final ITsWorkroomStorage storage;

  /**
   * Constructor.
   *
   * @param aStorage {@link ITsWorkroomStorage} - the properties storage
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkideProjectProperties( ITsWorkroomStorage aStorage ) {
    storage = TsNullArgumentRtException.checkNull( aStorage );
    eventer = new GenericChangeEventer( this );
    IKeepablesStorage ks = storage.ktorStorage();
    params.setAll( ks.readItem( ISkideProjectPropertiesConstants.STORAGE_OPID_PROPECT_PROPS,
        OptionSetKeeper.KEEPER_INDENTED, IOptionSet.NULL ) );
    for( IDataDef dd : ALL_SPP_OPS ) {
      if( !params.hasKey( dd.id() ) ) {
        params.setValue( dd, dd.defaultValue() );
      }
    }
    svs.addValidator( builtinValidator );
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  public IOptionSet params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // ISkideProjectProperties
  //

  @Override
  public ITsValidationSupport<ISkideProjectPropertiesValidator> svs() {
    return svs;
  }

  /**
   * Changes the project properties.
   * <p>
   * The argument may contains only part of the properties. Unknown properties will be ignored.
   *
   * @param aProperties {@link IOptionSet} - changed properties
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Override
  public void setProperties( IOptionSet aProperties ) {
    TsValidationFailedRtException.checkError( svs.validate( aProperties ) );
    params.refreshSet( aProperties );
    IKeepablesStorage ks = storage.ktorStorage();
    ks.writeItem( ISkideProjectPropertiesConstants.STORAGE_OPID_PROPECT_PROPS, params,
        OptionSetKeeper.KEEPER_INDENTED );
    eventer.fireChangeEvent();
  }

}
