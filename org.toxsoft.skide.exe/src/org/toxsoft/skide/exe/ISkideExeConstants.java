package org.toxsoft.skide.exe;

import static org.toxsoft.skide.exe.ISkResources.*;

import org.toxsoft.core.tsgui.mws.appinf.ITsApplicationInfo;
import org.toxsoft.core.tsgui.mws.appinf.TsApplicationInfo;
import org.toxsoft.core.tslib.utils.TsVersion;

/**
 * Общие константы приложения Hmade.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideExeConstants {

  // ------------------------------------------------------------------------------------
  // App info

  String    APP_ID      = "ru.toxsoft.skide";      //$NON-NLS-1$
  String    APP_ALIAS   = "skide";                 //$NON-NLS-1$
  TsVersion APP_VERSION = new TsVersion( 3, 0, 0 );

  ITsApplicationInfo APP_INFO = new TsApplicationInfo( APP_ID, STR_N_APP_INFO, STR_D_APP_INFO, APP_ALIAS, APP_VERSION );

}
