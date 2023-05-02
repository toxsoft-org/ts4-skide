package org.toxsoft.skide.core.gui.m5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.skide.core.api.*;

/**
 * LM for {@link SkideUnitM5Model}.
 *
 * @author hazard157
 */
class SkideProjectUnitM5LifecycleManager
    extends M5LifecycleManager<ISkideUnit, ISkidePluginsRegistrator> {

  public SkideProjectUnitM5LifecycleManager( IM5Model<ISkideUnit> aModel, ISkidePluginsRegistrator aMaster ) {
    super( aModel, false, false, false, true, aMaster );
  }

  @Override
  protected IList<ISkideUnit> doListEntities() {
    return master().listUnits();
  }

}
