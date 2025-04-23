package org.toxsoft.skide.core.api.ucateg;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.api.ucateg.ISkResources.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.skide.core.api.*;

/**
 * SkIDE units built-in categories constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideUnitCategoryConstants {

  String UCATEGID_SYSDESCR          = SKIDE_ID + ".ucateg.Sysdescr";         //$NON-NLS-1$
  String UCATEGID_DELPOYMENT        = SKIDE_ID + ".ucateg.Deployment";       //$NON-NLS-1$
  String UCATEGID_EXTERNAL_SYSTEMS  = SKIDE_ID + ".ucateg.ExternalSystems";  //$NON-NLS-1$
  String UCATEGID_DEVELOPMENT_DEBUG = SKIDE_ID + ".ucateg.DevelopmentDebug"; //$NON-NLS-1$
  String UCATEGID_ADMINISTRATION    = SKIDE_ID + ".ucateg.Administration";   //$NON-NLS-1$
  String UCATEGID_APP_SPECIFIC      = SKIDE_ID + ".ucateg.AppSpecific";      //$NON-NLS-1$

  String OPID_SKIDE_UNIT_CATEGORY = SKIDE_FULL_ID + ".UnitCategory"; //$NON-NLS-1$

  /**
   * Units category: system description and other editors (classes, objects, refbooks, mnemos, etc).
   */
  IStridableParameterized UCATEG_SYSDESCR = StridableParameterized.create( UCATEGID_SYSDESCR, //
      TSID_NAME, STR_SYSDESCR, //
      TSID_DESCRIPTION, STR_SYSDESCR_D, //
      TSID_ICON_ID, ICONID_CATEG_SYSDESCR //
  );

  /**
   * Units category: deployment of the edited system.
   */
  IStridableParameterized UCATEG_DELPOYMENT = StridableParameterized.create( UCATEGID_DELPOYMENT, //
      TSID_NAME, STR_DELPOYMENT, //
      TSID_DESCRIPTION, STR_DELPOYMENT_D, //
      TSID_ICON_ID, ICONID_CATEG_DELPOYMENT //
  );

  /**
   * Units category: communications to external systems (bridges OPC/Modbus data exchange, PublicAPI, etc.)
   */
  IStridableParameterized UCATEG_EXTERNAL_SYSTEMS = StridableParameterized.create( UCATEGID_EXTERNAL_SYSTEMS, //
      TSID_NAME, STR_EXTERNAL_SYSTEMS, //
      TSID_DESCRIPTION, STR_EXTERNAL_SYSTEMS_D, //
      TSID_ICON_ID, ICONID_CATEG_EXTERNAL_SYSTEMS //
  );

  /**
   * Units category: software development and debug (interaction with Java source code, runtime investigation, etc).
   */
  IStridableParameterized UCATEG_DEVELOPMENT_DEBUG = StridableParameterized.create( UCATEGID_DEVELOPMENT_DEBUG, //
      TSID_NAME, STR_DEVELOPMENT_DEBUG, //
      TSID_DESCRIPTION, STR_DEVELOPMENT_DEBUG_D, //
      TSID_ICON_ID, ICONID_CATEG_DEVELOPMENT_DEBUG //
  );

  /**
   * Units category: administrative tasks (user and role management, templates preparation, etc).
   */
  IStridableParameterized UCATEG_ADMINISTRATION = StridableParameterized.create( UCATEGID_ADMINISTRATION, //
      TSID_NAME, STR_ADMINISTRATION, //
      TSID_DESCRIPTION, STR_ADMINISTRATION_D, //
      TSID_ICON_ID, ICONID_CATEG_ADMINISTRATION //
  );

  /**
   * Units category: application specific SkIDE units.
   */
  IStridableParameterized UCATEG_APP_SPECIFIC = StridableParameterized.create( UCATEGID_APP_SPECIFIC, //
      TSID_NAME, STR_APP_SPECIFIC, //
      TSID_DESCRIPTION, STR_APP_SPECIFIC_D, //
      TSID_ICON_ID, ICONID_CATEG_APP_SPECIFIC //
  );

  /**
   * All categories in the order of appearance in tree.
   */
  IStridablesList<IStridableParameterized> ALL_UNIT_CATEGORIES = new StridablesList<>( //
      UCATEG_SYSDESCR, //
      UCATEG_DELPOYMENT, //
      UCATEG_EXTERNAL_SYSTEMS, //
      UCATEG_DEVELOPMENT_DEBUG, //
      UCATEG_ADMINISTRATION, //
      UCATEG_APP_SPECIFIC //
  );

  /**
   * Option for {@link ISkideUnit#params()}: the category ID.
   * <p>
   * Values may be one of the <b><code>UCATEG_XXX</code></b> constant.
   */
  IDataDef OPDEF_SKIDE_UNIT_CATEGORY = DataDef.create( OPID_SKIDE_UNIT_CATEGORY, STRING, //
      TSID_NAME, STR_SKIDE_UNIT_CATEGORY, //
      TSID_DESCRIPTION, STR_SKIDE_UNIT_CATEGORY_D, //
      TSID_DEFAULT_VALUE, UCATEGID_SYSDESCR //
  );

}
