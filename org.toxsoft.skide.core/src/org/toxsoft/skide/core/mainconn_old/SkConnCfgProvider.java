package org.toxsoft.skide.core.mainconn_old;

import static org.toxsoft.uskat.core.impl.ISkCoreConfigConstants.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.backend.*;
import org.toxsoft.uskat.core.backend.metainf.*;
import org.toxsoft.uskat.core.impl.*;

// TODO TANSLATE

/**
 * Implementation of {@link ISkConnCfgProvider}.
 * <p>
 * To open Sk-connection there are two set of arguments (options and references):
 * <ul>
 * <li>backend specific arguments - are most important part of the arguments;</li>
 * <li>common arguments - are the same for all backends.</li>
 * </ul>
 * This implementation handles common argumetns by itself and uses {@link ISkBackendMetaInfo} to handle backend-specific
 * arguments.
 * <p>
 * Provider identification as {@link IStridable} is the same as {@link ISkBackendMetaInfo},
 *
 * @author hazard157
 */
public abstract non-sealed class SkConnCfgProvider
    implements ISkConnCfgProvider {

  private final IStridablesListEdit<IDataDef> opDefs = new StridablesList<>();

  private final ISkBackendProvider backendProvider;
  private final ISkBackendMetaInfo backInfo;

  /**
   * Constructor.
   *
   * @param aSource {@link ISkBackendProvider} - underlying backend provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkConnCfgProvider( ISkBackendProvider aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    backendProvider = aSource;
    backInfo = backendProvider.getMetaInfo();
    // define options
    opDefs.addAll( ISkCoreConfigConstants.ALL_SK_CORE_CFG_PARAMS );
    opDefs.addAll( backInfo.argOps() );
    opDefs.addAll( backendMetaInfo().getAuthentificationType().authentificationOptionDefs() );

    // TODO REFDEF_BACKEND_PROVIDER
    // TODO REFDEF_USER_SERVICES
    // TODO REFDEF_THREAD_SEPARATOR

  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return backInfo.id();
  }

  @Override
  public String nmName() {
    return backInfo.nmName();
  }

  @Override
  public String description() {
    return backInfo.description();
  }

  // ------------------------------------------------------------------------------------
  // ISkConnCfgProvider
  //

  @Override
  final public ISkBackendMetaInfo backendMetaInfo() {
    return backInfo;
  }

  @Override
  final public IStridablesListEdit<IDataDef> listOpDef() {
    return opDefs;
  }

  @Override
  final public ValidationResult validateOpValues( IOptionSet aOpValues ) {
    TsNullArgumentRtException.checkNull( aOpValues );
    ValidationResult vr = OptionSetUtils.validateOptionSet( aOpValues, opDefs );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.firstNonOk( vr, doValidate( aOpValues ) );
  }

  @Override
  final public void fillArgs( ITsContext aSkConnArgs, IOptionSet aOpValues ) {
    TsNullArgumentRtException.checkNull( aSkConnArgs );
    TsValidationFailedRtException.checkError( validateOpValues( aOpValues ) );
    // copy options
    for( IDataDef opdef : listOpDef() ) {
      if( aOpValues.hasValue( opdef.id() ) ) {
        aSkConnArgs.params().setValue( opdef, aOpValues.getValue( opdef ) );
      }
      else {
        aSkConnArgs.params().setValue( opdef, opdef.defaultValue() );
      }
    }
    // copy known refs
    REFDEF_BACKEND_PROVIDER.setRef( aSkConnArgs, backendProvider() );

    // create Sk-connection opening argument
    doProcessArgs( aSkConnArgs );
  }

  // ------------------------------------------------------------------------------------
  // class API
  //

  /**
   * Returns the underlying backend provider.
   *
   * @return {@link ISkBackendProvider} - the backend provider
   */
  final public ISkBackendProvider backendProvider() {
    return backendProvider;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * The subclass may perform additional checks on option values.
   * <p>
   * At the time of the call, it is checked that the set has all the mandatory options and that the type of all values
   * is compatible with the option atomic type from {@link #listOpDef()}.
   * <p>
   * Base class simply returns {@link ValidationResult#SUCCESS}, when overridden, there is no need to call the parent
   * method.
   *
   * @param aOpValues {@link IOptionSet} - values of the options listed in {@link #listOpDef()}
   * @return {@link ValidationResult} - validation result
   */
  protected ValidationResult doValidate( IOptionSet aOpValues ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * The subclass may perform additional processing of the Sk-connection opening arguments.
   * <p>
   * Base class does nothing, there is no need to call the parent method when overriding.
   *
   * @param aSkConnArgs {@link ITsContext} - editable context that will be the argument to open the connection
   */
  protected void doProcessArgs( ITsContext aSkConnArgs ) {
    // nop
  }

}
