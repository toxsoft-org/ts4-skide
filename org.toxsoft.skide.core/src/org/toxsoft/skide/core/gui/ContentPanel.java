package org.toxsoft.skide.core.gui;

import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * {@link PanelSkideProjectExplorer} right panel implementation.
 *
 * @author hazard157
 */
class ContentPanel
    extends TsPanel {

  /**
   * Unit panels, once created, are stored in this map and placed in this panel's stack layout.
   */
  private final IStringMapEdit<AbstractSkideUnitPanel> unitPanelsMap = new StringMap<>();

  private final StackLayout stackLayout = new StackLayout();

  public ContentPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    stackLayout.marginWidth = stackLayout.marginHeight = 0;
    this.setLayout( stackLayout );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the unit to display it's content.
   *
   * @param aUnit {@link ISkideUnit} - the unit or <code>null</code>
   */
  void setUnitToDisplay( ISkideUnit aUnit ) {
    if( aUnit == null ) {
      stackLayout.topControl = null;
      this.layout();
      return;
    }
    AbstractSkideUnitPanel panel = unitPanelsMap.findByKey( aUnit.id() );
    if( panel == null ) {
      try {
        ITsGuiContext ctx = new TsGuiContext( tsContext() );
        panel = aUnit.createUnitPanel( ctx );
        panel.createControl( this );
        unitPanelsMap.put( aUnit.id(), panel );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
        stackLayout.topControl = null;
        this.layout();
        return;
      }
    }
    stackLayout.topControl = panel.getControl();
    this.layout();
  }

}
