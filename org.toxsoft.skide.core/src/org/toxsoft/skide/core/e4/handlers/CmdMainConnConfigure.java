package org.toxsoft.skide.core.e4.handlers;

import static org.toxsoft.skide.core.e4.handlers.ISkResources.*;
import static org.toxsoft.uskat.core.gui.conn.cfg.m5.IConnectionConfigM5Constants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.uskat.core.gui.conn.cfg.*;

/**
 * Command: Create and edit the configurations of the main connection.
 *
 * @author hazard157
 */
public class CmdMainConnConfigure {

  @Execute
  void exec( Shell aShell, IM5Domain aM5, IEclipseContext aEcContext, IConnectionConfigService aCcs ) {
    ITsGuiContext ctx = new TsGuiContext( aEcContext );
    TsDialogInfo di =
        new TsDialogInfo( ctx, aShell, DLG_C_CC_CONFIGURE, DLG_T_CC_CONFIGURE, ITsDialogConstants.DF_NO_APPROVE );
    IM5Model<IConnectionConfig> model = aM5.getModel( MID_SK_CONN_CFG, IConnectionConfig.class );
    IM5LifecycleManager<IConnectionConfig> lm = model.getLifecycleManager( aCcs );
    IM5ItemsProvider<IConnectionConfig> ip = lm.itemsProvider();
    M5GuiUtils.askSelectItem( di, model, null, ip, lm );
  }

}
