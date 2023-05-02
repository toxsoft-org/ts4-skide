package org.toxsoft.skide.exe.e4.processors;

import static org.toxsoft.core.tsgui.mws.IMwsCoreConstants.*;
import static org.toxsoft.skide.exe.ISkideExeSharedResources.*;

import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Processor to tune standard MWS GUI for SkIDE needs.
 * <p>
 * Register processor as <code>apply=always</code> and <code>beforefragment=false</code>.
 *
 * @author hazard157
 */
public class ProcessorSkideGuiTuner
    extends MwsAbstractProcessor {

  @Override
  protected void doProcess() {
    // change menu name "File" to "SkIDE"
    MMenu mmnuFile = findElement( mainWindow(), MWSID_MENU_MAIN_FILE, MMenu.class, EModelService.IN_MAIN_MENU );
    TsInternalErrorRtException.checkNull( mmnuFile );
    mmnuFile.setLabel( STR_MENU_SKIDE );
    mmnuFile.setTooltip( STR_MENU_SKIDE_D );
  }

}
