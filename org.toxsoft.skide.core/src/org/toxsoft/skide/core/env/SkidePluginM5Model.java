package org.toxsoft.skide.core.env;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * M5- model of {@link ISkidePlugin}.
 *
 * @author hazard157
 */
public class SkidePluginM5Model
    extends M5Model<ISkidePlugin> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = SKIDE_ID + "m5.SkidePlugin"; //$NON-NLS-1$

  /**
   * Field {@link ISkidePlugin#id()}
   */
  public final IM5AttributeFieldDef<ISkidePlugin> ID = new M5StdFieldDefId<>();

  /**
   * Field {@link ISkidePlugin#nmName()}
   */
  public final IM5AttributeFieldDef<ISkidePlugin> NAME = new M5StdFieldDefName<>();

  /**
   * Field {@link ISkidePlugin#description()}
   */
  public final IM5AttributeFieldDef<ISkidePlugin> DESCRIPTION = new M5StdFieldDefDescription<>();

  /**
   * Constructor.
   */
  public SkidePluginM5Model() {
    super( MODEL_ID, ISkidePlugin.class );
    addFieldDefs( ID, NAME, DESCRIPTION );
  }

  static class LifecycleManager
      extends M5LifecycleManager<ISkidePlugin, ISkidePluginsRegistrator> {

    public LifecycleManager( IM5Model<ISkidePlugin> aModel, ISkidePluginsRegistrator aMaster ) {
      super( aModel, false, false, false, true, aMaster );
    }

    @Override
    protected IList<ISkidePlugin> doListEntities() {
      return master().listRegisteredPlugins();
    }

  }

  @Override
  protected IM5LifecycleManager<ISkidePlugin> doCreateDefaultLifecycleManager() {
    ISkidePluginsRegistrator skideEnv = tsContext().get( ISkidePluginsRegistrator.class );
    return new LifecycleManager( this, skideEnv );
  }

  @Override
  protected IM5LifecycleManager<ISkidePlugin> doCreateLifecycleManager( Object aMaster ) {
    return new LifecycleManager( this, ISkidePluginsRegistrator.class.cast( aMaster ) );
  }

}
