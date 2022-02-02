package org.toxsoft.skide.core;

import org.toxsoft.core.unit.txtproj.lib.impl.TsProjectFileFormatInfo;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideCoreConstants {

  String SKIDE_ID = "org.toxsoft.skide"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";  //$NON-NLS-1$
  String ICONID_APP_ICON           = "app-icon"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // SkIDE project file meta-info
  //
  int    PROJ_FILE_FORMAT_VERSTION = 2;
  String PROJ_FILE_APP_ID          = SKIDE_ID + ".project"; //$NON-NLS-1$

  TsProjectFileFormatInfo PROJECT_FILE_FORMAT_INFO =
      new TsProjectFileFormatInfo( PROJ_FILE_APP_ID, PROJ_FILE_FORMAT_VERSTION );

}
