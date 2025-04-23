package org.toxsoft.skide.core;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkResources.*;
import static org.toxsoft.skide.core.l10n.ISkideCoreSharedResources.*;

import java.time.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkideCoreConstants {

  String SKIDE_ID      = "skide";               //$NON-NLS-1$
  String SKIDE_FULL_ID = TS_FULL_ID + ".skide"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // App info

  TsVersion APP_VERSION = new TsVersion( 5, 0, 2023, Month.MARCH, 23 );

  ITsApplicationInfo APP_INFO =
      new TsApplicationInfo( SKIDE_FULL_ID, STR_APP_INFO, STR_APP_INFO_D, SKIDE_ID, APP_VERSION );

  // ------------------------------------------------------------------------------------
  // E4

  String PERSPID_SKIDE_PROJECT = "org.toxsoft.skide.persp.project"; //$NON-NLS-1$

  String MMNUID_SKIDE_TASKS = "org.toxsoft.skide.menu.tasks"; //$NON-NLS-1$

  String TOOLBARID_SKIDE_TASKS = "org.toxsoft.skide.toolbar.tasks"; //$NON-NLS-1$

  String CMDID_SKIDE_SHOW_PLUGINS   = "org.toxsoft.skide.cmd.show_plugins";   //$NON-NLS-1$
  String CMDID_SKIDE_RUN_TASK       = "org.toxsoft.skide.cmd.run_task";       //$NON-NLS-1$
  String CMDARGID_SKIDE_RUN_TASK_ID = "org.toxsoft.skide.cmdarg.run_task_id"; //$NON-NLS-1$
  String CMDID_SKIDE_CONFIGURE_TASK = "org.toxsoft.skide.cmd.configure_task"; //$NON-NLS-1$
  String CMDARGID_SKIDE_CFG_TASK_ID = "org.toxsoft.skide.cmdarg.cfg_task_id"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME      = "ICONID_";                  //$NON-NLS-1$
  String ICONID_APP_ICON                = "app-icon";                 //$NON-NLS-1$
  String ICONID_FOLDER_GENERAL          = "folder-general";           //$NON-NLS-1$
  String ICONID_FILE_GENERAL            = "file-general";             //$NON-NLS-1$
  String ICONID_TASK                    = "task";                     //$NON-NLS-1$
  String ICONID_TASKS                   = "tasks";                    //$NON-NLS-1$
  String ICONID_TASK_CONFIG             = "task-config";              //$NON-NLS-1$
  String ICONID_TASKS_CONFIG            = "tasks-config";             //$NON-NLS-1$
  String ICONID_TASK_RUN                = "task-run";                 //$NON-NLS-1$
  String ICONID_TASK_RESULTS            = "task-results";             //$NON-NLS-1$
  String ICONID_CATEG_SYSDESCR          = "ucateg-sysdescr";          //$NON-NLS-1$
  String ICONID_CATEG_DELPOYMENT        = "ucateg-deployment";        //$NON-NLS-1$
  String ICONID_CATEG_EXTERNAL_SYSTEMS  = "ucateg-external-systems";  //$NON-NLS-1$
  String ICONID_CATEG_DEVELOPMENT_DEBUG = "ucateg-development-debug"; //$NON-NLS-1$
  String ICONID_CATEG_ADMINISTRATION    = "ucateg-administration";    //$NON-NLS-1$
  String ICONID_CATEG_APP_SPECIFIC      = "ucateg-app-specific";      //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Application preferences

  String PBID_SKIDE_MAIN = PERSPID_SKIDE_PROJECT;

  IDataDef APPREF_UNITS_LIST_ICON_SIZE = DataDef.create( SKIDE_ID + ".UnitsListIconSize", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_UNITS_LIST_ICON_SIZE, //
      TSID_DESCRIPTION, STR_UNITS_LIST_ICON_SIZE_D, //
      TSID_KEEPER_ID, EIconSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EIconSize.IS_48X48 ) //
  );

  IDataDef APPREF_NO_TASK_CFG_DLG_BEFORE_RUN = DataDef.create( SKIDE_ID + ".NoTaskConfigDialogBeforeRun", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_NO_TASK_CFG_DLG_BEFORE_RUN, //
      TSID_DESCRIPTION, STR_NO_TASK_CFG_DLG_BEFORE_RUN_D, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  IStridablesList<IDataDef> ALL_APREFS = new StridablesList<>( //
      APPREF_UNITS_LIST_ICON_SIZE, //
      APPREF_NO_TASK_CFG_DLG_BEFORE_RUN //
  );

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context.
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkideCoreConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
    IAppPreferences aprefs = aWinContext.get( IAppPreferences.class );
    IPrefBundle pb = aprefs.defineBundle( PBID_SKIDE_MAIN, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_PB_SKIDE_MAIN, //
        TSID_DESCRIPTION, STR_PB_SKIDE_MAIN_D, //
        TSID_ICON_ID, ICONID_APP_ICON //
    ) );
    for( IDataDef dd : ALL_APREFS ) {
      pb.defineOption( dd );
    }
  }

}
