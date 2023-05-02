package org.toxsoft.skide.core.api.impl;

import static org.toxsoft.skide.core.api.ISkideProjectPropertiesConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
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

  private final GenericChangeEventer eventer;
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
    params.setAll( ks.readItem( STORAGE_OPID_PROPECT_PROPS, OptionSetKeeper.KEEPER_INDENTED, IOptionSet.NULL ) );
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

  // nop yet

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Changes the project properties.
   * <p>
   * The argument may contains only part of the properties. Unknown properties will be ignored.
   *
   * @param aProperties {@link IOptionSet} - changed properties
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setProperties( IOptionSet aProperties ) {
    TsNullArgumentRtException.checkNull( aProperties );
    for( IDataDef dd : ALL_SPP_OPS ) {
      IAtomicValue av = aProperties.getValue( dd.id(), null );
      if( av != null ) {
        if( !dd.validator().validate( av ).isError() ) {
          params.setValue( dd, av );
        }
      }
    }
    IKeepablesStorage ks = storage.ktorStorage();
    ks.writeItem( STORAGE_OPID_PROPECT_PROPS, params, OptionSetKeeper.KEEPER_INDENTED );
    eventer.fireChangeEvent();
  }

}
