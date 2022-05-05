package org.toxsoft.skide.exe.e4.processors;

import static org.toxsoft.core.tsgui.mws.IMwsCoreConstants.*;
import static org.toxsoft.skide.exe.e4.processors.ISkResources.*;

import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.mws.bases.*;

/**
 * Processor to tune standard MWS GUI for SkIDE needs.
 *
 * @author hazard157
 */
public class ProcessorSkideGuiTuner
    extends MwsAbstractProcessor {

  @Override
  protected void doProcess() {
    // change menu name "File" to "Project"
    MMenu menuFile = findElement( mainWindow(), MWSID_MENU_MAIN_FILE, MMenu.class, EModelService.IN_MAIN_MENU );
    menuFile.setLabel( STR_L_MENU_PROJECT );
    menuFile.setTooltip( STR_P_MENU_PROJECT );
  }

}
