package org.toxsoft.skide.plugin.exconn.main;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.lang.reflect.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.operation.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.uskat.core.gui.conn.cfg.*;
import org.toxsoft.uskat.core.gui.conn.m5.*;

/**
 * {@link AbstractSkideUnitPanel} implementation.
 *
 * @author hazard157
 * @author dima
 */
class SkideExconnUnitPanel
    extends AbstractSkideUnitPanel {

  private TsComposite backplane;

  public SkideExconnUnitPanel( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    backplane = new TsComposite( aParent );
    backplane.setLayout( new BorderLayout() );
    IConnectionConfigService ccService = tsContext().get( IConnectionConfigService.class );
    IM5Model<IConnectionConfig> model =
        m5().getModel( IConnectionConfigM5Constants.MID_SK_CONN_CFG, IConnectionConfig.class );
    IM5LifecycleManager<IConnectionConfig> lm = model.getLifecycleManager( ccService );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.params().addAll( tsContext().params() );
    IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(),
        avValobj( EBorderLayoutPlacement.SOUTH ) );
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AvUtils.AV_FALSE );
    MultiPaneComponentModown<IConnectionConfig> mpc =
        new MultiPaneComponentModown<>( ctx, model, lm.itemsProvider(), lm );
    mpc.createControl( backplane );
    mpc.getControl().setLayoutData( BorderLayout.WEST );
    return backplane;
  }

  /**
   * Выполняет работы в отдельном потоке при открытом диалоге ожидания.
   *
   * @param aShell Shell родительское окно
   * @param aDialogName String - имя диалога ожидания.
   * @param aRunnable IRunnableWithProgress - реализация потока выполнения работы.
   */
  public static void runInWaitingDialog( Shell aShell, String aDialogName, IRunnableWithProgress aRunnable ) {

    final ProgressMonitorDialog dialog = new ProgressMonitorDialog( aShell ) {

      @Override
      protected Control createDialogArea( Composite aParent ) {
        Control c = super.createDialogArea( aParent );
        c.getShell().setText( aDialogName );
        return c;
      }
    };

    try {
      dialog.run( true, false, aRunnable );
    }
    catch( InvocationTargetException | InterruptedException e ) {
      LoggerUtils.error( e );
      Display.getDefault().asyncExec( () -> TsDialogUtils.error( aShell, e.getCause() != null ? e.getCause() : e ) );
    }
  }

}
