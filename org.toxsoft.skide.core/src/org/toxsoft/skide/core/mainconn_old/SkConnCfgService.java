package org.toxsoft.skide.core.mainconn_old;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.idgen.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.uskat.core.*;

/**
 * {@link ISkConnCfgService} implementation.
 *
 * @author hazard157
 */
class SkConnCfgService
    implements ISkConnCfgService {

  private static final String STORAGE_ID_CFGS_LIST = "ConnectionConfigs";                 //$NON-NLS-1$
  private static final String CONN_CFG_ID_PREFIX   = ISkHardConstants.SK_ID + "_ConnCfg"; //$NON-NLS-1$

  private final IStridGenerator idGen = new UuidStridGenerator( UuidStridGenerator.createState( CONN_CFG_ID_PREFIX ) );

  private final IStridablesListEdit<ISkConnCfgProvider> providers = new StridablesList<>();
  private final ITsWorkroomStorage                      wrStorage;

  SkConnCfgService( ITsWorkroomStorage aStorage ) {
    TsNullArgumentRtException.checkNull( aStorage );
    wrStorage = aStorage;
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private IStridablesListEdit<ISkConnCfg> loadConfigs() {
    IList<ISkConnCfg> ll = wrStorage.ktorStorage().readColl( STORAGE_ID_CFGS_LIST, SkConnCfg.KEEPER );
    return new StridablesList<>( ll );
  }

  private void saveConfigs( IStridablesList<ISkConnCfg> aConfigs ) {
    wrStorage.ktorStorage().writeColl( STORAGE_ID_CFGS_LIST, aConfigs, SkConnCfg.KEEPER );
  }

  // ------------------------------------------------------------------------------------
  // ISkConnCfgService
  //

  @Override
  public IStridablesList<ISkConnCfg> configs() {
    return loadConfigs();
  }

  @Override
  public SkConnCfg createConfig() {
    return new SkConnCfg( idGen.nextId(), IOptionSet.NULL, IOptionSet.NULL );
  }

  @Override
  public void putConfig( ISkConnCfg aCfg ) {
    TsNullArgumentRtException.checkNull( aCfg );
    IStridablesListEdit<ISkConnCfg> ll = loadConfigs();
    ll.put( aCfg );
    saveConfigs( ll );
  }

  @Override
  public void removeConfig( String aCfgId ) {
    TsNullArgumentRtException.checkNull( aCfgId );
    IStridablesListEdit<ISkConnCfg> ll = loadConfigs();
    ll.removeByKey( aCfgId );
    saveConfigs( ll );
  }

  @Override
  public void prepareConnectionArgs( String aConfigId, ITsContext aContext ) {
    TsNullArgumentRtException.checkNulls( aConfigId, aContext );
    ISkConnCfg cfg = loadConfigs().findByKey( aConfigId );
    TsItemNotFoundRtException.checkNull( cfg );
    ISkConnCfgProvider p = providers.findByKey( cfg.providerId() );
    TsItemNotFoundRtException.checkNull( p );
    p.fillArgs( aContext, cfg.values() );
  }

  @Override
  public IStridablesList<ISkConnCfgProvider> listProviders() {
    return providers;
  }

  @Override
  public void registerConfigProvider( ISkConnCfgProvider aProvider ) {
    TsNullArgumentRtException.checkNull( aProvider );
    TsItemAlreadyExistsRtException.checkTrue( providers.hasKey( aProvider.id() ) );
    providers.add( aProvider );
  }

}
