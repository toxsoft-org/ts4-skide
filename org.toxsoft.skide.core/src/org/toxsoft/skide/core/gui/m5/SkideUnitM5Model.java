package org.toxsoft.skide.core.gui.m5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.skide.core.api.*;

/**
 * M5-model of {@link ISkideUnit}.
 *
 * @author hazard157
 */
public class SkideUnitM5Model
    extends M5Model<ISkideUnit> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = SKIDE_FULL_ID + ".m5.ProjectUnit"; //$NON-NLS-1$

  /**
   * Field {@link ISkideUnit#id()}
   */
  public final M5AttributeFieldDef<ISkideUnit> ID = new M5StdFieldDefId<>();

  /**
   * Field {@link ISkideUnit#nmName()}
   */
  public final M5AttributeFieldDef<ISkideUnit> NAME = new M5StdFieldDefName<>() {

    @Override
    protected Image doGetFieldValueIcon( ISkideUnit aEntity, EIconSize aIconSize ) {
      if( aEntity.iconId() != null ) {
        return iconManager().loadStdIcon( aEntity.iconId(), aIconSize );
      }
      return null;
    }

  };

  /**
   * Field {@link ISkideUnit#description()}
   */
  public final M5AttributeFieldDef<ISkideUnit> DESCRIPTION = new M5StdFieldDefDescription<>();

  /**
   * Constructor.
   */
  public SkideUnitM5Model() {
    super( MODEL_ID, ISkideUnit.class );
    addFieldDefs( ID, NAME, DESCRIPTION );
    ID.removeFlags( M5FF_COLUMN );
  }

  @Override
  protected IM5LifecycleManager<ISkideUnit> doCreateDefaultLifecycleManager() {
    ISkidePluginsRegistrator pluginsRegistrator = tsContext().get( ISkideEnvironment.class ).pluginsRegistrator();
    return new SkideProjectUnitM5LifecycleManager( this, pluginsRegistrator );
  }

  @Override
  protected IM5LifecycleManager<ISkideUnit> doCreateLifecycleManager( Object aMaster ) {
    return new SkideProjectUnitM5LifecycleManager( this, ISkidePluginsRegistrator.class.cast( aMaster ) );
  }

}
