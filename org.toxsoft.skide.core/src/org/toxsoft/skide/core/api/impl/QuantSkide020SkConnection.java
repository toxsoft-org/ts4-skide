package org.toxsoft.skide.core.api.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skide.core.l10n.ISkideCoreSharedResources.*;
import static org.toxsoft.uskat.backend.memtext.MtbBackendToFile.*;
import static org.toxsoft.uskat.core.impl.ISkCoreConfigConstants.*;

import java.io.File;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Display;
import org.toxsoft.core.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.core.tslib.bricks.ctx.ITsContext;
import org.toxsoft.core.tslib.bricks.ctx.impl.TsContext;
import org.toxsoft.core.tslib.utils.errors.TsInternalErrorRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.core.tslib.utils.progargs.ProgramArgs;
import org.toxsoft.core.txtproj.lib.workroom.ITsWorkroom;
import org.toxsoft.uskat.backend.memtext.MtbBackendToFile;
import org.toxsoft.uskat.backend.sqlite.ISkBackensSqliteConstants;
import org.toxsoft.uskat.backend.sqlite.SkBackendSqlite;
import org.toxsoft.uskat.core.api.cmdserv.ISkCommand;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.gui.conn.SkGuiThreadExecutor;
import org.toxsoft.uskat.core.impl.ISkCoreConfigConstants;

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
public class QuantSkide020SkConnection
    extends AbstractQuant {

  /**
   * The name of the USkat system DB textual file in the project workroom.
   */
  public static final String WORKROOM_FILE_SKIDE_SYSDB_TXT = "uskat-system.textual"; //$NON-NLS-1$

  /**
   * The name of the USkat system DB SQLite file in the project workroom.
   */
  public static final String WORKROOM_FILE_SKIDE_SYSDB_SQLITE = "uskat-system.sqlite"; //$NON-NLS-1$

  /**
   * Command line argument, if specified to "true" SkIDE will use still experimental SQLite backend for system DB.
   */
  public static final String CMDLINE_ARG_USE_SQLITE_SYSDB = "UseSqliteSysDb"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public QuantSkide020SkConnection() {
    super( QuantSkide020SkConnection.class.getSimpleName() );
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
  private static void internalOpenTxtSkConnection( IEclipseContext aWinContext, ITsWorkroom aWorkroom ) {
    ISkConnectionSupplier cs = aWinContext.get( ISkConnectionSupplier.class );
    TsInternalErrorRtException.checkNull( cs );
    TsInternalErrorRtException.checkTrue( cs.defConn().state().isOpen() );
    ITsContext args = new TsContext();
    File file = new File( aWorkroom.wrDir(), WORKROOM_FILE_SKIDE_SYSDB_TXT );
    Display display = aWinContext.get( Display.class );
    OPDEF_FILE_PATH.setValue( args.params(), avStr( file.getAbsolutePath() ) );
    REFDEF_BACKEND_PROVIDER.setRef( args, MtbBackendToFile.PROVIDER );
    ISkCoreConfigConstants.REFDEF_THREAD_EXECUTOR.setRef( args, new SkGuiThreadExecutor( display ) );
    cs.defConn().open( args );
    LoggerUtils.defaultLogger().info( LOG_FMT_INFO_TEXTUAL_SYSDB, file.getAbsolutePath() );
  }

  /**
   * Opens default {@link ISkConnectionSupplier#defConn()} as the workroom-local SQLite backend.
   *
   * @param aWinContext {@link IEclipseContext} - the windows level context
   * @param aWorkroom {@link ITsWorkroom} - the workroom to store Sk-backend file
   */
  private static void internalOpenSqliteSkConnection( IEclipseContext aWinContext, ITsWorkroom aWorkroom ) {
    ISkConnectionSupplier cs = aWinContext.get( ISkConnectionSupplier.class );
    TsInternalErrorRtException.checkNull( cs );
    TsInternalErrorRtException.checkTrue( cs.defConn().state().isOpen() );
    ITsContext args = new TsContext();
    File file = new File( aWorkroom.wrDir(), WORKROOM_FILE_SKIDE_SYSDB_SQLITE );
    Display display = aWinContext.get( Display.class );
    REFDEF_BACKEND_PROVIDER.setRef( args, SkBackendSqlite.PROVIDER );
    ISkBackensSqliteConstants.OPDEF_DB_FILE_NAME.setValue( args.params(), avStr( file.getAbsolutePath() ) );
    ISkCoreConfigConstants.REFDEF_THREAD_EXECUTOR.setRef( args, new SkGuiThreadExecutor( display ) );
    cs.defConn().open( args );
    LoggerUtils.defaultLogger().info( LOG_FMT_INFO_SQLITE_SYSDB, file.getAbsolutePath() );
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
    ProgramArgs progArgs = aWinContext.get( ProgramArgs.class );
    String strCmdLineUseSqlite = progArgs.getArgValue( CMDLINE_ARG_USE_SQLITE_SYSDB, Boolean.FALSE.toString() );
    boolean useSqlite = Boolean.parseBoolean( strCmdLineUseSqlite );
    if( useSqlite ) {
      internalOpenSqliteSkConnection( aWinContext, wrSkide );
    }
    else {
      internalOpenTxtSkConnection( aWinContext, wrSkide );
    }
  }

}
