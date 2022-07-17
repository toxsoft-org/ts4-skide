package org.toxsoft.skide.core.proj;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.proj.ISkResources.*;

import java.time.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.core.txtproj.lib.impl.*;

/**
 * SkIDE project related constants.
 *
 * @author hazard157
 */
public interface ISkideProjectConstants {

  /**
   * TODO: SkIDE project<br>
   * 1. Add project before save listener and update LAST_EDITED project option<br>
   */

  // ------------------------------------------------------------------------------------
  // Project file format

  /**
   * SkIDE file format version number.
   */
  int PROJ_FILE_FORMAT_VERSTION = 4;

  /**
   * SkIDE project file format identifier.
   */
  String PROJ_FILE_APP_ID = TS_FULL_ID + ".skide.project"; //$NON-NLS-1$

  /**
   * Information about SkIDE project file format.
   */
  TsProjectFileFormatInfo PROJECT_FILE_FORMAT_INFO =
      new TsProjectFileFormatInfo( PROJ_FILE_APP_ID, PROJ_FILE_FORMAT_VERSTION );

  // ------------------------------------------------------------------------------------
  // Project properties options definitions

  /**
   * Project human-readable short name.
   */
  IDataDef PROPDEF_SKIDE_PROJ_NAME = DataDef.createOverride2( TSID_NAME, DDEF_NAME, //
      TSID_NAME, STR_N_SKPROJOP_NAME, //
      TSID_DESCRIPTION, STR_D_SKPROJOP_NAME //
  );

  /**
   * Project human-readable short name.
   */
  IDataDef PROPDEF_SKIDE_PROJ_DESCRIPTION = DataDef.createOverride2( TSID_DESCRIPTION, DDEF_DESCRIPTION, //
      TSID_NAME, STR_N_SKPROJOP_DESCRIPTION, //
      TSID_DESCRIPTION, STR_D_SKPROJOP_DESCRIPTION //
  );

  /**
   * Project human-readable short name.
   */
  IDataDef PROPDEF_SKIDE_PROJ_LAST_SAVE_TIME = DataDef.create( "LastSavedAt", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_SKPROJOP_LAST_SAVE_TIME, //
      TSID_DESCRIPTION, STR_D_SKPROJOP_LAST_SAVE_TIME, //
      TSID_IS_READ_ONLY, AV_TRUE, //
      TSID_KEEPER_ID, LocalDateTimeKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( LocalDateTime.now() ) //
  );

  /**
   * All known options of the project parameters {@link ITsProject#params()}.
   */
  IStridablesList<IDataDef> ALL_SKIDE_PROJECT_PROPERIY_OPDEFS = new StridablesList<>( //
      PROPDEF_SKIDE_PROJ_NAME, //
      PROPDEF_SKIDE_PROJ_DESCRIPTION, //
      PROPDEF_SKIDE_PROJ_LAST_SAVE_TIME //
  );

}
