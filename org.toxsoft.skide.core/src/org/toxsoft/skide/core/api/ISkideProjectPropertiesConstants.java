package org.toxsoft.skide.core.api;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.l10n.ISkideCoreSharedResources.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.txtproj.lib.workroom.*;

/**
 * Constants of {@link ISkideProjectProperties}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideProjectPropertiesConstants {

  /**
   * The section ID of the {@link ISkideProjectProperties} storage in {@link ITsWorkroom#getApplicationStorage()}.
   */
  String STORAGE_OPID_PROPECT_PROPS = SKIDE_FULL_ID + ".ProjectProperties"; //$NON-NLS-1$

  String DEFAULT_PROJECT_ID    = "org.toxsoft.skide.default_project_id"; //$NON-NLS-1$
  String DEFAULT_PROJECT_ALIAS = "default_project_alias";                //$NON-NLS-1$

  String OPID_SPP_PROJ_ID     = IAvMetaConstants.TSID_ID;
  String OPID_SPP_ALIAS       = TS_ID + ".Alias";                 //$NON-NLS-1$
  String OPID_SPP_NAME        = IAvMetaConstants.TSID_NAME;
  String OPID_SPP_DESCRIPTION = IAvMetaConstants.TSID_DESCRIPTION;

  IDataDef OPDEF_SPP_PROJ_ID = DataDef.createOverride2( OPID_SPP_PROJ_ID, DDEF_IDPATH, //
      TSID_NAME, STR_SPP_PROJ_ID, //
      TSID_DESCRIPTION, STR_SPP_PROJ_ID_D, //
      TSID_DEFAULT_VALUE, avStr( DEFAULT_PROJECT_ID ) //
  );

  IDataDef OPDEF_SPP_ALIAS = DataDef.createOverride2( OPID_SPP_ALIAS, DDEF_IDNAME, //
      TSID_NAME, STR_SPP_ALIAS, //
      TSID_DESCRIPTION, STR_SPP_ALIAS_D, //
      TSID_DEFAULT_VALUE, avStr( DEFAULT_PROJECT_ALIAS ) //
  );

  IDataDef OPDEF_SPP_NAME = DataDef.createOverride2( OPID_SPP_NAME, DDEF_NAME, //
      TSID_NAME, STR_SPP_NAME, //
      TSID_DESCRIPTION, STR_SPP_NAME_D //
  );

  IDataDef OPDEF_SPP_DESCRIPTION = DataDef.createOverride2( OPID_SPP_DESCRIPTION, DDEF_DESCRIPTION, //
      TSID_NAME, STR_SPP_DESCRIPTION, //
      TSID_DESCRIPTION, STR_SPP_DESCRIPTION_D //
  // OPDEF_IS_HEIGHT_FIXED, AV_FALSE, //
  // OPDEF_VERTICAL_SPAN, avInt( 4 ) //
  );

  IStridablesList<IDataDef> ALL_SPP_OPS = new StridablesList<>( //
      OPDEF_SPP_PROJ_ID, //
      OPDEF_SPP_ALIAS, //
      OPDEF_SPP_NAME, //
      OPDEF_SPP_DESCRIPTION //
  );

}
