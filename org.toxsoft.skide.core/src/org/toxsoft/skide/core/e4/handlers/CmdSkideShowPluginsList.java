package org.toxsoft.skide.core.e4.handlers;

import static org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogConstants.*;
import static org.toxsoft.skide.core.e4.handlers.ISkResources.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.skide.core.env.*;

/**
 * Command: show registered SkIDE plugins {@link ISkidePluginsRegistrator#listRegisteredPlugins()} in dialog,
 *
 * @author hazard157
 */
public class CmdSkideShowPluginsList {

  @Execute
  void exec( Shell aShell, IM5Domain aM5, ISkideEnvironment aSkideEnv, IEclipseContext aContext ) {
    IM5Model<ISkidePlugin> model = aM5.getModel( SkidePluginM5Model.MODEL_ID, ISkidePlugin.class );
    IM5ItemsProvider<ISkidePlugin> ip = model.getLifecycleManager( aSkideEnv.pluginsRegistrator() ).itemsProvider();
    ITsGuiContext ctx = new TsGuiContext( aContext );
    ITsDialogInfo di = new TsDialogInfo( ctx, aShell, DLG_C_SHOW_PLUGINS, DLG_T_SHOW_PLUGINS, DF_NO_APPROVE );
    M5GuiUtils.showItemsList( di, model, ip );
  }

}
