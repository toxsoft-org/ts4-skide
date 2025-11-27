package org.toxsoft.uskat.core.gui.sded2.incub;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.txtmatch.*;

@SuppressWarnings( "javadoc" )
public interface IClassFilterParamsBuilder {

  record ParamFilterCfg ( String opId, IAtomicValue value, EQueryParamUsage usage ) {}

  record ClassIdCfg ( TextMatcher aClassIdMather, EQueryParamUsage usage ) {}

  IListEdit<ParamFilterCfg> paramFilterCfgs();

  IListEdit<ClassIdCfg> classIdFilterCfgs();

  // TODO filter by claiming services

  // TODO filter by properties existence

  /**
   * Returns created instance of the filter parameters.
   *
   * @return {@link ITsCombiFilterParams} - filter parameters
   */
  ITsCombiFilterParams createFilterParams();

}
