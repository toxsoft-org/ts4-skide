package org.toxsoft.skide.core.e4.handlers;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
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
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.uskat.core.gui.conn.cfg.*;

/**
 * Command: Select configuration to establish the main connection.
 *
 * @author hazard157
 */
public class CmdMainConnSelect {

  @Execute
  void exec( Shell aShell, IM5Domain aM5, IEclipseContext aEcContext, IConnectionConfigService aCcs ) {
    ITsGuiContext ctx = new TsGuiContext( aEcContext );
    IMultiPaneComponentConstants.OPDEF_IS_TOOLBAR.setValue( ctx.params(), AV_FALSE );
    IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AV_FALSE );
    TsDialogInfo di = new TsDialogInfo( ctx, aShell, DLG_C_CC_SELECT, DLG_T_CC_SELECT, 0 );
    IM5Model<IConnectionConfig> model = aM5.getModel( MID_SK_CONN_CFG, IConnectionConfig.class );
    IM5LifecycleManager<IConnectionConfig> lm = model.getLifecycleManager( aCcs );
    IM5ItemsProvider<IConnectionConfig> ip = lm.itemsProvider();
    IConnectionConfig cc = M5GuiUtils.askSelectItem( di, model, null, ip, null );
    if( cc != null ) {
      // FIXME reestablish the main connection
    }
  }

}
