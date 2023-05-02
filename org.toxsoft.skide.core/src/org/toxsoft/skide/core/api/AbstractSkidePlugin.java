package org.toxsoft.skide.core.api;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The SKIDE plugin is a functionality provider as the part of the SkIDE modular framework.
 * <p>
 * This interface is to be implemented by SkIDE contributor and instance to be registered in
 * {@link ISkidePluginsRegistrator}.
 * <p>
 * SkIDE framework provides to environments to the plugin:
 * <ul>
 * <li>{@link #skEnv()} - SkIDE commmon environment, the single instance is shared between all plugins;</li>
 * <li>{@link #plEnv()} - plugin specific environment, each plugin has it's own instance.</li>
 * </ul>
 * <p>
 * Plugin contributes units to the SkIDE project by the method {@link #createUnits(ITsGuiContext)}. Project unit is a
 * visualization of the plugin in the <i>SkIDE project perspectiove</i>. Plugins are free to create individual
 * perspectives with arbitrary content.
 *
 * @author hazard157
 */
public abstract class AbstractSkidePlugin
    extends StridableParameterized
    implements ICloseable {

  private ISkideEnvironment  skideEnvironment  = null;
  private IPluginEnvironment pluginEnvironment = null;

  /**
   * Constructor.
   *
   * @param aId String - the plugin ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public AbstractSkidePlugin( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  final public void close() {
    doClose();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the same environment as found in the application context.
   *
   * @return {@link ISkideEnvironment} - common environment for all plugins
   */
  final public ISkideEnvironment skEnv() {
    TsIllegalStateRtException.checkNull( skideEnvironment );
    return skideEnvironment;
  }

  /**
   * Returns the environment instance specifically created fro this plugin.
   *
   * @return {@link IPluginEnvironment} - plugin-specific environment
   */
  final public IPluginEnvironment plEnv() {
    TsIllegalStateRtException.checkNull( pluginEnvironment );
    return pluginEnvironment;
  }

  /**
   * Creates and returns conttributed project units.
   * <p>
   * This method is called only once.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link IStridablesList}&lt;{@link ISkideUnit}&gt; - the units contributed by the plugin
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  final public IStridablesList<ISkideUnit> createUnits( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IStridablesListEdit<ISkideUnit> units = new StridablesList<>();
    doCreateUnits( aContext, units );
    for( ISkideUnit u : units ) {
      TsInternalErrorRtException.checkTrue( u.skidePlugin() != this );
    }
    return units;
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  /**
   * Internal metho, must not be called by the clients.
   */
  @SuppressWarnings( "javadoc" )
  final public void papiInitialize( ISkideEnvironment aSkEnv, IPluginEnvironment aPlEnv ) {
    TsInternalErrorRtException.checkNull( aSkEnv );
    TsInternalErrorRtException.checkNull( aPlEnv );
    TsInternalErrorRtException.checkNoNull( skideEnvironment );
    TsInternalErrorRtException.checkNoNull( pluginEnvironment );
    skideEnvironment = aSkEnv;
    pluginEnvironment = aPlEnv;
    doInititlize();
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass may perform additional initialization.
   * <p>
   * The environments {@link #skEnv()} and {@link #plEnv()} are initialized.
   * <p>
   * Does nothing in the base class so there is no need to call superclass method when overriding.
   */
  protected void doInititlize() {
    // nop
  }

  /**
   * Subclass must create units and put in the provided list.
   * <p>
   * This method is called after successful initialization.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aUnitsList {@link IStridablesList}&lt;{@link ISkideUnit}&gt; - an editable empty list
   */
  protected abstract void doCreateUnits( ITsGuiContext aContext, IStridablesListEdit<ISkideUnit> aUnitsList );

  /**
   * Subclass may perform addition cluen-up when SkIDE application finishes.
   * <p>
   * Called from the method {@link #close()} the plugin closes.
   * <p>
   * Note: this method is called only for successfully initialized plugins.
   * <p>
   * Does nothing in the base class so there is no need to call superclass method when overriding.
   */
  protected void doClose() {
    // nop
  }

}
