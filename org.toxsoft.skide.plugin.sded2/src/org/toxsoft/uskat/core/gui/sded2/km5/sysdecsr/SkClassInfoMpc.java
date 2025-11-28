package org.toxsoft.uskat.core.gui.sded2.km5.sysdecsr;

import static org.toxsoft.uskat.core.gui.sded2.l10n.ISded2SharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.utils.*;

/**
 * {@link MultiPaneComponentModown} implementation for {@link Sded2SkClassInfoM5Model}.
 *
 * @author hazard157
 */
class SkClassInfoMpc
    extends MultiPaneComponentModown<ISkClassInfo>
    implements ISkGuiContextable {

  public SkClassInfoMpc( ITsGuiContext aContext, IM5Model<ISkClassInfo> aModel,
      IM5ItemsProvider<ISkClassInfo> aItemsProvider, IM5LifecycleManager<ISkClassInfo> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
    // add tree nodes
    TreeModeInfo<ISkClassInfo> tmiByHierarchy = new TreeModeInfo<>( "ByHierarchy", //$NON-NLS-1$
        STR_TMI_CLASS_BY_HIERARCHY, STR_TMI_CLASS_BY_HIERARCHY_D, null,
        new SkClassTreeMakers.ByHierarchy( aContext, skConn() ) );
    treeModeManager().addTreeMode( tmiByHierarchy );
    treeModeManager().setCurrentMode( tmiByHierarchy.id() );
  }

  @Override
  protected void doTuneBeforeDisplay() {
    tree().console().expandAll();
  }

  // ------------------------------------------------------------------------------------
  // ISkGuiContextable
  //

  @Override
  public ISkConnection skConn() {
    return model().domain().tsContext().get( ISkConnection.class );
  }

}
