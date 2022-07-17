package org.toxsoft.skide.exe;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skide.core.proj.ISkideProjectConstants.*;
import static org.toxsoft.skide.exe.ISkideExeConstants.*;

import java.io.*;

import org.eclipse.osgi.service.environment.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tslib.bricks.apprefs.impl.*;
import org.toxsoft.core.tslib.utils.progargs.*;
import org.toxsoft.core.txtproj.mws.*;

/**
 * The plugin activator.
 *
 * @author hazard157
 */
public class Activator
    extends MwsActivator {

  /**
   * The plugin ID (for Java static imports).
   */
  public static final String PLUGIN_ID = "org.toxsoft.skide.exe"; //$NON-NLS-1$

  /**
   * Command line argument to overridef {@link #DEFAULT_CFG_FILE_NAME}.
   * <p>
   * An absolute path may be specified as INI-file name.
   */
  public static final String CMDLINE_ARG_CFG_FILE_NAME = "config"; //$NON-NLS-1$

  /**
   * Apllication preferences INI-file default name.
   * <p>
   * File is searched in program startup directory.
   */
  public static final String DEFAULT_CFG_FILE_NAME = APP_ALIAS + ".cfg"; //$NON-NLS-1$

  private static Activator instance = null;

  /**
   * Constructor.
   */
  public Activator() {
    super( PLUGIN_ID );
    checkInstance( instance );
    instance = this;
  }

  @Override
  protected void doStart() {
    IMwsOsgiService mws = findOsgiService( IMwsOsgiService.class );
    mws.setAppInfo( APP_INFO );
    // txtproj plugin configuration and project file format info
    IUnitTxtprojMwsConstants.OPDEF_SHOW_CMD_IN_TOOLBAR.setValue( mws.context().params(), AV_FALSE );
    IUnitTxtprojMwsConstants.OPDEF_SHOW_CMD_IN_MENU.setValue( mws.context().params(), AV_TRUE );
    IUnitTxtprojMwsConstants.OPDEF_IS_REDUCED_TOOLBAR_BUTTONS.setValue( mws.context().params(), AV_TRUE );
    IUnitTxtprojMwsConstants.OPDEF_ALWAYS_USE_FILE_MENU.setValue( mws.context().params(), AV_FALSE );
    IUnitTxtprojMwsConstants.OPDEF_IMMEDIATE_LOAD_PROJ.setValue( mws.context().params(), AV_TRUE );
    IUnitTxtprojMwsConstants.OPDEF_PROJECT_FILE_FORMAT_INFO.setValue( mws.context().params(),
        avValobj( PROJECT_FILE_FORMAT_INFO ) );
    // application preferences will be stored in INI-file
    EnvironmentInfo envInfo = getOsgiService( EnvironmentInfo.class );
    ProgramArgs pa = new ProgramArgs( envInfo.getCommandLineArgs() );
    String cfgFileName = pa.getArgValue( CMDLINE_ARG_CFG_FILE_NAME, DEFAULT_CFG_FILE_NAME );
    File cfgFile = new File( cfgFileName );
    AbstractAppPreferencesStorage apStorage = new AppPreferencesConfigIniStorage( cfgFile );
    mws.context().put( AbstractAppPreferencesStorage.class, apStorage );
  }

  /**
   * Returns the reference to the activator singleton.
   *
   * @return {@link Activator} - the activator singleton
   */
  public static Activator getInstance() {
    return instance;
  }

}
