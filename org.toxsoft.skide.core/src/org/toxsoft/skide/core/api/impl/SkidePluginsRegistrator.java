package org.toxsoft.skide.core.api.impl;

import static org.toxsoft.skide.core.l10n.ISkideCoreSharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.skide.core.api.*;

/**
 * {@link ISkidePluginsRegistrator} implementation.
 *
 * @author hazard157
 */
public final class SkidePluginsRegistrator
    implements ISkidePluginsRegistrator, ICloseable {

  private final IStridablesListEdit<AbstractSkidePlugin> registeredPlugins  = new StridablesList<>();
  private final IStridablesListEdit<AbstractSkidePlugin> initializedPlugins = new StridablesList<>();

  /**
   * List of created units, <code>null</code> means that units were not created yet.
   */
  private IStridablesList<ISkideUnit> createdUnits = null;

  /**
   * Constructor.
   */
  public SkidePluginsRegistrator() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  @SuppressWarnings( "javadoc" )
  public void papiInitPlugins( ITsGuiContext aContext, ISkideEnvironment aSkEnv, ITsWorkroom aWorkroom ) {
    TsNullArgumentRtException.checkNulls( aContext, aSkEnv, aWorkroom );
    // init plugins
    for( AbstractSkidePlugin p : registeredPlugins ) {
      ITsWorkroomStorage pluginStorage = aWorkroom.getStorage( p.id() );
      IPluginEnvironment plEnv = new PluginEnvironment( pluginStorage );
      try {
        p.papiInitialize( aSkEnv, plEnv );
        initializedPlugins.add( p );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex, FMT_ERR_CANT_INIT_SKIDE_PLUGIN, p.id(), ex.getMessage() );
      }
    }
    // create units
    IStridablesListEdit<ISkideUnit> units = new StridablesList<>();
    for( AbstractSkidePlugin p : initializedPlugins ) {
      IStridablesList<ISkideUnit> uu = p.createUnits( aContext );
      for( ISkideUnit u : uu ) {
        if( units.hasKey( u.id() ) ) {
          ISkideUnit uExisting = units.getByKey( u.id() );
          LoggerUtils.errorLogger().warning( FMT_ERR_DUPLICATE_PROJ_UNIT_ID, u.id(), p.id(),
              uExisting.skidePlugin().id() );
        }
        else {
          units.add( u );
        }
      }
    }
    createdUnits = units;
  }

  // ------------------------------------------------------------------------------------
  // ISkidePluginsRegistrator
  //

  @Override
  public IStridablesList<AbstractSkidePlugin> listRegisteredPlugins() {
    return registeredPlugins;
  }

  @Override
  public void registerPlugin( AbstractSkidePlugin aPlugin ) {
    TsNullArgumentRtException.checkNull( aPlugin );
    TsItemAlreadyExistsRtException.checkTrue( registeredPlugins.hasKey( aPlugin.id() ) );
    registeredPlugins.add( aPlugin );
  }

  @Override
  public IStridablesList<ISkideUnit> listUnits() {
    TsIllegalStateRtException.checkNull( createdUnits );
    return createdUnits;
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    for( AbstractSkidePlugin p : initializedPlugins ) {
      try {
        p.close();
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

}
