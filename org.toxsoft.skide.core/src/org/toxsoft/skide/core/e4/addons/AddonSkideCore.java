package org.toxsoft.skide.core.e4.addons;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.skide.core.*;
import org.toxsoft.skide.core.Activator;
import org.toxsoft.skide.core.main.*;
import org.toxsoft.skide.core.main.impl.*;
import org.toxsoft.uskat.backend.memtext.*;
import org.toxsoft.uskat.base.gui.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * Plugin adoon.
 *
 * @author hazard157
 */
public class AddonSkideCore
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkideCore() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantSkBaseGui() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ISkideEnvironment skideEnv = new SkideEnvironment();
    aAppContext.set( ISkideEnvironment.class, skideEnv );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkideCoreConstants.init( aWinContext );
    // open connection to the project built-in system
    ITsProject skideProject = aWinContext.get( ITsProject.class );
    TsInternalErrorRtException.checkNull( skideProject );
    ITsContext args = new TsContext();
    ISkCoreConfigConstants.REFDEF_BACKEND_PROVIDER.setRef( args, MtbBackendToTsProj.PROVIDER );
    MtbBackendToTsProj.REFDEF_PROJECT.setRef( args, skideProject );
    MtbBackendToTsProj.OPDEF_PDU_ID.setValue( args.params(), avStr( SKIDE_MAIN_SYSTEM_PDU_ID ) );
    ISkConnectionSupplier connectionSupplier = aWinContext.get( ISkConnectionSupplier.class );
    TsInternalErrorRtException.checkNull( connectionSupplier );
    ITsGuiContext ctx = new TsGuiContext( aWinContext );
    ISkConnection conn = connectionSupplier.createConnection( SKIDE_MAIN_SYSTEM_SUPPLIED_CONN_ID, ctx );
    try {
      conn.open( args );
      connectionSupplier.setDefaultConnection( SKIDE_MAIN_SYSTEM_SUPPLIED_CONN_ID );

      // DEBUG
      TsTestUtils.pl( "Main system connection opened" );

    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      Shell shell = aWinContext.get( Shell.class );
      TsDialogUtils.error( shell, ex );
      conn.close();
    }
  }

}
