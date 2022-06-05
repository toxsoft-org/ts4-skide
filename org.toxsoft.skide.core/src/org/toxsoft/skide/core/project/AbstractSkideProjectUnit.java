package org.toxsoft.skide.core.project;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The only base implementation of {@link ISkideProjectUnit}.
 *
 * @author hazard157
 */
public non-sealed class AbstractSkideProjectUnit
    implements ISkideProjectUnit {

  private final String         id;
  private final IOptionSetEdit params = new OptionSet();

  /**
   * Constructor.
   *
   * @param aId String - entity ID (an IDpath)
   * @param aParams {@link IOptionSet} - initial value of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public AbstractSkideProjectUnit( String aId, IOptionSet aParams ) {
    id = StridUtils.checkValidIdPath( aId );
    params.setAll( aParams );
  }

  // ------------------------------------------------------------------------------------
  // Stridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return DDEF_NAME.getValue( params ).asString();
  }

  @Override
  public String description() {
    return DDEF_DEFAULT_VALUE.getValue( params ).asString();
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  public IOptionSet params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // API for descendants
  //

  protected void setParams( IOptionSet aParams ) {
    params.setAll( aParams );
  }

  protected void updateParams( IOptionSet aParams ) {
    params.addAll( aParams );
  }

}
