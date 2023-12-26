package org.toxsoft.skide.core.e4.handlers;

import static org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogConstants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;
import static org.toxsoft.skide.core.l10n.ISkideCoreSharedResources.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.skide.core.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.gui.m5.*;

/**
 * Command: show registered SkIDE plugins with contributed units in dialog.
 * <p>
 * Command ID: {@link ISkideCoreConstants#CMDID_SKIDE_SHOW_PLUGINS}.
 *
 * @author hazard157
 */
public class CmdSkideShowPluginsList {

  // TODO change unit models to the common model for plugins and units

  private static final String TREE_MODE_ID = "PluginsAndUnits"; //$NON-NLS-1$

  @Execute
  void exec( Shell aShell, IM5Domain aM5, ISkideEnvironment aSkEnv, IEclipseContext aContext ) {
    ITsGuiContext ctx = new TsGuiContext( aContext );
    // create collection panel
    IM5Model<ISkideUnit> model = aM5.getModel( SkideUnitM5Model.MODEL_ID, ISkideUnit.class );
    IM5LifecycleManager<ISkideUnit> lm = model.getLifecycleManager( aSkEnv.pluginsRegistrator() );
    IM5ItemsProvider<ISkideUnit> ip = lm.itemsProvider();
    OPDEF_NODE_ICON_SIZE.setValue( ctx.params(), avValobj( EIconSize.IS_32X32 ) );
    OPDEF_IS_TOOLBAR.setValue( ctx.params(), AV_FALSE );
    MultiPaneComponentModown<ISkideUnit> mpc = new MultiPaneComponentModown<>( ctx, model, ip );
    IM5CollectionPanel<ISkideUnit> panel = new M5CollectionPanelMpcModownWrapper<>( mpc, true );
    // set panel to always display tree plugins-units
    TreeModeInfo<ISkideUnit> tmi = new TreeModeInfo<>( TREE_MODE_ID, EMPTY_STRING, EMPTY_STRING, null, //
        new SkideUnitM5TreeMakerByPlugins( aSkEnv.pluginsRegistrator() ) );
    mpc.treeModeManager().addTreeMode( tmi );
    mpc.treeModeManager().setCurrentMode( TREE_MODE_ID );
    // show dialog
    TsDialogInfo di = new TsDialogInfo( ctx, aShell, DLG_SHOW_UNITS, DLG_SHOW_UNITS_D, DF_NO_APPROVE );
    di.setMinSizeShellRelative( 10, 50 );
    M5GuiUtils.showCollPanel( di, panel );
  }

}
