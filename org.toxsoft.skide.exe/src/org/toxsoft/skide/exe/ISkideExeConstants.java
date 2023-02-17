package org.toxsoft.skide.exe;

import static org.toxsoft.skide.exe.ISkideExeSharedResources.*;

import java.time.*;

import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Application common constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideExeConstants {

  // ------------------------------------------------------------------------------------
  // App info

  String    APP_ID      = "org.toxsoft.skide";                            //$NON-NLS-1$
  String    APP_ALIAS   = "skide";                                        //$NON-NLS-1$
  TsVersion APP_VERSION = new TsVersion( 4, 0, 2023, Month.FEBRUARY, 12 );

  ITsApplicationInfo APP_INFO = new TsApplicationInfo( APP_ID, STR_N_APP_INFO, STR_D_APP_INFO, APP_ALIAS, APP_VERSION );

}
