package org.toxsoft.skide.plugin.exconn.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.plugin.exconn.*;
import org.toxsoft.skide.plugin.exconn.Activator;
import org.toxsoft.skide.plugin.exconn.main.*;
import org.toxsoft.skide.plugin.exconn.service.*;
import org.toxsoft.uskat.backend.memtext.*;
import org.toxsoft.uskat.backend.s5.gui.*;
import org.toxsoft.uskat.backend.s5.gui.utils.*;
import org.toxsoft.uskat.core.gui.conn.cfg.*;

/**
 * Plugin addon.
 *
 * @author hazard157
 */
public class AddonSkidePluginExconn
    extends MwsAbstractAddon {

  /**
   * Section ID to store {@link IConnectionConfigService#listConfigs()} using
   * {@link IKeepablesStorage#writeColl(String, ITsCollection, IEntityKeeper)}
   */
  private static final String SECTID_CONNECTION_CONFIGS = "ConnectionConfigs"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public AddonSkidePluginExconn() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static void initConnectionConfigService( IEclipseContext aAppContext ) {
    ConnectionConfigService ccService = (ConnectionConfigService)aAppContext.get( IConnectionConfigService.class );
    // register known providers
    // dima 24.08.23
    IConnectionConfigProvider p1 = new ConnectionConfigProvider( MtbBackendToFile.PROVIDER, IOptionSet.NULL );
    if( !ccService.listProviders().hasKey( p1.id() ) ) {
      ccService.registerPovider( p1 );
    }
    if( !ccService.listProviders().hasKey( S5ConnectionConfigProvider.INSTANCE.id() ) ) {
      ccService.registerPovider( S5ConnectionConfigProvider.INSTANCE );
    }
    // load configs
    ITsWorkroom workroom = aAppContext.get( ITsWorkroom.class );
    TsInternalErrorRtException.checkNull( workroom );
    IKeepablesStorage storage = workroom.getStorage( SkidePluginExconn.SKIDE_PLUGIN_ID ).ktorStorage();
    IList<IConnectionConfig> ll = storage.readColl( SECTID_CONNECTION_CONFIGS, ConnectionConfig.KEEPER );
    ccService.setConfigsList( new StridablesList<>( ll ) );
    ccService.eventer().addListener( new IConnectionConfigServiceListener() {

      @Override
      public void onConfigsListChanged( IConnectionConfigService aSource ) {
        storage.writeColl( SECTID_CONNECTION_CONFIGS, ccService.listConfigs(), ConnectionConfig.KEEPER );
      }

      @Override
      public void onProvidersListChanged( IConnectionConfigService aSource ) {
        // nop
      }

    } );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantSkBackendS5Gui() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // register SkIDE plugin
    ISkideEnvironment skEnv = aAppContext.get( ISkideEnvironment.class );
    skEnv.pluginsRegistrator().registerPlugin( SkidePluginExconn.INSTANCE );
    //
    ISkideExternalConnectionsService exConnService = new SkideExternalConnectionsService();
    aAppContext.set( ISkideExternalConnectionsService.class, exConnService );
    //
    initConnectionConfigService( aAppContext );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkidePluginExconnConstants.init( aWinContext );
  }

}
