package org.toxsoft.skide.plugin.exconn.service;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.login.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.conn.cfg.*;

/**
 * The external connections management API.
 * <p>
 * The reference to this interface are put in the application level context.
 *
 * @author hazard157
 */
public interface ISkideExternalConnectionsService {

  /**
   * Invokes connection configuration selection dialog.
   * <p>
   * Configuration is selected from the list {@link IConnectionConfigService#listConfigs()}.
   *
   * @param aContext {@link ITsGuiContext} - the context for connection creation
   * @return String - the ID of the configuration or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  String selectConfig( ITsGuiContext aContext );

  /**
   * Opens the connection with the specified configuration..
   * <p>
   * Method returns the ID of the open connection. Returned {@link IdChain} contains exactly two branches: first is the
   * argument <code>aConfigId</code>, second is the generated unique IDpath.
   * <p>
   * Returned ID is used as the key to the {@link ISkConnectionSupplier} service, so {@link ISkConnection} may be
   * retrieved by {@link ISkConnectionSupplier#getConn(IdChain)}.
   * <p>
   * The caller must close connection.
   *
   * @param aConfigId String - ID of the config in {@link IConnectionConfigService#listConfigs()}
   * @param aContext {@link ITsGuiContext} - the context for connection creation
   * @param aLoginInfo {@link ILoginInfo} - the authentification info if needed or {@link ILoginInfo#NONE}
   * @return {@link IdChain} - the ID of the open connection or <code>null</code> if user cancels selection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException config ID not found in {@link IConnectionConfigService#listConfigs()}
   * @throws TsItemNotFoundRtException configuration found but {@link IConnectionConfigProvider} not found
   */
  IdChain openConnection( String aConfigId, ITsGuiContext aContext, ILoginInfo aLoginInfo );

  // ------------------------------------------------------------------------------------
  // Inline methods for convenience
  //

  @SuppressWarnings( "javadoc" )
  @Deprecated
  default IdChain selectConfigAndOpenConnection( ITsGuiContext aContext ) {
    String cfgId = selectConfig( aContext );
    if( cfgId != null ) {
      return openConnection( cfgId, aContext, ILoginInfo.NONE );
    }
    return null;
  }

}
