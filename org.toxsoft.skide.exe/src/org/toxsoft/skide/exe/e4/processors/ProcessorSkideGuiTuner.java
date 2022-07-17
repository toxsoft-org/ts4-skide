package org.toxsoft.skide.exe.e4.processors;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.mws.IMwsCoreConstants.*;
import static org.toxsoft.core.txtproj.mws.IUnitTxtprojMwsConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.exe.e4.processors.ISkResources.*;

import org.eclipse.e4.ui.model.application.commands.*;
import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.bases.*;

/**
 * Processor to tune standard MWS GUI for SkIDE needs.
 *
 * @author hazard157
 */
public class ProcessorSkideGuiTuner
    extends MwsAbstractProcessor {

  /**
   * Icons size of icons in menu.
   * <p>
   * Warning: must match size of icons specified for menu in Application.e4xmi in plugin org.toxsoft.tsgui.mws!
   */
  private static final EIconSize INITIAL_MENU_ICON_SIZE = EIconSize.IS_24X24;

  @Override
  protected void doProcess() {
    // change menu name "File" to "Project"
    MMenu mmnuProjectCommands;
    int index; // insert items at, for separate project menu is at end, and after PROJECT_SAVE_AS in file menu
    if( isFileMenuAlwaysUsed() ) {
      mmnuProjectCommands = findElement( mainWindow(), MWSID_MENU_MAIN_FILE, MMenu.class, EModelService.IN_MAIN_MENU );
      index = 4; // FIXME must find item PROJECT_SAVE_AS and insert after it
    }
    else {
      mmnuProjectCommands =
          findElement( mainWindow(), MWSID_MENU_MAIN_PROJECT, MMenu.class, EModelService.IN_MAIN_MENU );
      index = mmnuProjectCommands.getChildren().size();
    }
    mmnuProjectCommands.setLabel( STR_L_MENU_PROJECT );
    mmnuProjectCommands.setTooltip( STR_P_MENU_PROJECT );
    // add menu item "Edit project properties"
    MHandledMenuItem mItem = modelService().createModelElement( MHandledMenuItem.class );
    MCommand cmd = findElement( application(), CMDID_SKIDE_EDIT_PROJ_PROPS, MCommand.class, EModelService.ANYWHERE );
    mItem.setCommand( cmd );
    mItem.setIconURI( makeTsguiIconUri( ICONID_TRANSPARENT, INITIAL_MENU_ICON_SIZE ) );
    mmnuProjectCommands.getChildren().add( index, mItem );
    mmnuProjectCommands.getChildren().add( index, modelService().createModelElement( MMenuSeparator.class ) );
  }

  private boolean isFileMenuAlwaysUsed() {
    return OPDEF_ALWAYS_USE_FILE_MENU.getValue( mwsService().context().params() ).asBool();
  }

}
