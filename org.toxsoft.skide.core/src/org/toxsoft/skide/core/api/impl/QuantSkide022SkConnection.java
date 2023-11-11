package org.toxsoft.skide.core.api.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.uskat.core.impl.ISkCoreConfigConstants.*;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.services.timers.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.uskat.backend.sqlite.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * SkIDE builtin connection initialization.
 * <p>
 * Creates and opens the {@link ISkCommand} to the built-in USkat system. Connection will be set as the default
 * connection in {@link ISkConnectionSupplier#defConn()}.
 * <p>
 * Warning: connection is created and opened at the windows level, not the application level.
 * <p>
 * Quant must be executed immediately after {@link QuantSkide010Workroom}.
 *
 * @author hazard157
 */
public class QuantSkide022SkConnection
    extends AbstractQuant {

  /**
   * The name of the USkat system content file in the project workroom.
   */
  public static final String WORKROOM_FILE_SKIDE_SYSTEM = "uskat-system.sqlite"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public QuantSkide022SkConnection() {
    super( QuantSkide022SkConnection.class.getSimpleName() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Opens default {@link ISkConnectionSupplier#defConn()} as the workroom-local textual backend.
   *
   * @param aWinContext {@link IEclipseContext} - the windows level context
   * @param aWorkroom {@link ITsWorkroom} - the workroom to store Sk-backend file
   */
  private static void internalOpenSkConnection( IEclipseContext aWinContext, ITsWorkroom aWorkroom ) {
    ISkConnectionSupplier cs = aWinContext.get( ISkConnectionSupplier.class );
    TsInternalErrorRtException.checkNull( cs );
    TsInternalErrorRtException.checkTrue( cs.defConn().state().isOpen() );
    ITsContext args = new TsContext();
    File file = new File( aWorkroom.wrDir(), WORKROOM_FILE_SKIDE_SYSTEM );
    REFDEF_BACKEND_PROVIDER.setRef( args, SkBackendSqlite.PROVIDER );
    ISkBackensSqliteConstants.OPDEF_DB_FILE_NAME.setValue( args.params(), avStr( file.getAbsolutePath() ) );
    SkDoJobCallerService.REF_TSGUI_TIMER_SERVICE.setRef( args, aWinContext.get( ITsGuiTimersService.class ) );
    ISkCoreConfigConstants.REFDEF_THREAD_SEPARATOR.setRef( args, SkDoJobCallerService.CREATOR );
    cs.defConn().open( args );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQuant
  //

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ITsWorkroom wrSkide = aWinContext.get( ITsWorkroom.class );
    TsInternalErrorRtException.checkNull( wrSkide );
    internalOpenSkConnection( aWinContext, wrSkide );
  }

}
