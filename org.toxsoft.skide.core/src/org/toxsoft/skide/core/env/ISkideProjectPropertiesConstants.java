package org.toxsoft.skide.core.env;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.env.ISkResources.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Constants of {@link ISkideProjectProperties}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideProjectPropertiesConstants {

  String OPID_SPP_NAME        = IAvMetaConstants.TSID_NAME;
  String OPID_SPP_DESCRIPTION = IAvMetaConstants.TSID_DESCRIPTION;

  IDataDef OPDEF_SPP_NAME = DataDef.create3( OPID_SPP_NAME, DDEF_NAME, //
      TSID_NAME, STR_N_SPP_NAME, //
      TSID_DESCRIPTION, STR_D_SPP_NAME //
  );

  IDataDef OPDEF_SPP_DESCRIPTION = DataDef.create3( OPID_SPP_DESCRIPTION, DDEF_DESCRIPTION, //
      TSID_NAME, STR_N_SPP_DESCRIPTION, //
      TSID_DESCRIPTION, STR_D_SPP_DESCRIPTION, //
      OPDEF_IS_HEIGHT_FIXED, AV_FALSE, //
      OPDEF_VERTICAL_SPAN, avInt( 4 ) //
  );

  IStridablesList<IDataDef> ALL_SPP_OPS = new StridablesList<>( //
      OPDEF_SPP_NAME, //
      OPDEF_SPP_DESCRIPTION //
  );

}
