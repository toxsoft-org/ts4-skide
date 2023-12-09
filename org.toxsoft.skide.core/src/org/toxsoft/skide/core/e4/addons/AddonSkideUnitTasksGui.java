package org.toxsoft.skide.core.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;

/**
 * Adds GUI items of the registered tasks {@link ISkideGenericTaskManager#listTasks()}.
 *
 * @author hazard157
 */
public class AddonSkideUnitTasksGui
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkideUnitTasksGui() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkideEnvironment skideEnv = aWinContext.get( ISkideEnvironment.class );
    TsInternalErrorRtException.checkNull( skideEnv );
    IStridablesList<IGenericTaskInfo> taskInfos = skideEnv.taskManager().listTasks();
    ITsE4Helper e4Helper = aWinContext.get( ITsE4Helper.class );

    // TODO add menu items (one per task) to the SkIDE menu
    MWindow mainWindow = aWinContext.get( MWindow.class );
    MMenu skideMenu = e4Helper.findElement( mainWindow, //
        IMwsCoreConstants.MWSID_MENU_MAIN_FILE, MMenu.class, EModelService.IN_MAIN_MENU );

    TsTestUtils.nl();
    TsTestUtils.pl( "=== SkIDE menu: %s", skideMenu );
    TsTestUtils.pl( "=== Tasks count: %d", taskInfos.size() );

    TsTestUtils.nl();
  }

}
