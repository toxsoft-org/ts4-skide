package org.toxsoft.uskat.core.gui.sded2.km5.sysdecsr;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

/**
 * {@link MultiPaneComponentModown} implementation for {@link Sded2SkClassInfoM5Model}.
 *
 * @author hazard157
 */
class ClassInfoMpc
    extends MultiPaneComponentModown<ISkClassInfo> {

  public ClassInfoMpc( ITsGuiContext aContext, IM5Model<ISkClassInfo> aModel,
      IM5ItemsProvider<ISkClassInfo> aItemsProvider, IM5LifecycleManager<ISkClassInfo> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
  }

}
