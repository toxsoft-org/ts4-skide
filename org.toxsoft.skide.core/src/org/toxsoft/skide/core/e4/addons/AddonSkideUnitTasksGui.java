package org.toxsoft.skide.core.e4.addons;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.commands.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;

/**
 * Adds GUI items of the registered tasks {@link ISkideTaskRegistrator#getRegisteredProcessors()}.
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
  // implementation
  //

  private static void initializeSkideTasksMenu( IEclipseContext aWinContext ) {
    // preconditions
    ISkideEnvironment skideEnv = aWinContext.get( ISkideEnvironment.class );
    TsInternalErrorRtException.checkNull( skideEnv );
    ITsE4Helper e4Helper = aWinContext.get( ITsE4Helper.class );
    MWindow mainWindow = aWinContext.get( MWindow.class );
    EModelService modelService = aWinContext.get( EModelService.class );
    MApplication application = aWinContext.get( MApplication.class );
    MMenu tasksMenu = e4Helper.findElement( mainWindow, MMNUID_SKIDE_TASKS, MMenu.class, EModelService.IN_MAIN_MENU );
    TsInternalErrorRtException.checkNull( tasksMenu );
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    IStringMap<SkideTaskProcessor> processorsMap = skideEnv.taskRegistrator().getRegisteredProcessors();
    if( processorsMap.isEmpty() ) {
      return;
    }
    // initialize tasks menu
    MCommand cmd = e4Helper.findElement( application, CMDID_SKIDE_RUN_TASK, MCommand.class, EModelService.ANYWHERE );
    // add separator after menu item "select and configure task"
    MMenuSeparator separator = modelService.createModelElement( MMenuSeparator.class );
    tasksMenu.getChildren().add( separator );
    // menu items - one per registered task
    int counter = 0;
    for( SkideTaskProcessor processor : processorsMap ) {
      IGenericTaskInfo taskInfo = processor.taskInfo();
      MHandledMenuItem mItem = modelService.createModelElement( MHandledMenuItem.class );
      mItem.setLabel( taskInfo.nmName() );
      mItem.setTooltip( taskInfo.description() );
      if( taskInfo.iconId() != null ) {
        mItem.setIconURI( iconManager.findStdIconBundleUri( taskInfo.iconId(), EIconSize.IS_24X24 ) );
      }
      mItem.setCommand( cmd );
      MParameter commandParam = modelService.createModelElement( MParameter.class );
      commandParam.setName( CMDARGID_SKIDE_RUN_TASK_ID );
      commandParam.setElementId( CMDARGID_SKIDE_RUN_TASK_ID + '.' + counter++ );
      commandParam.setValue( taskInfo.id() );
      mItem.getParameters().add( commandParam );
      tasksMenu.getChildren().add( mItem );
    }
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
    initializeSkideTasksMenu( aWinContext );
  }

}
