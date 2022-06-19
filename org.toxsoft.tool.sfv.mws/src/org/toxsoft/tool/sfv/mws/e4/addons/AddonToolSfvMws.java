package org.toxsoft.tool.sfv.mws.e4.addons;

import static org.toxsoft.tool.sfv.gui.IToolSfvGuiConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.tool.sfv.gui.*;

/**
 * Module addon.
 *
 * @author hazard157
 */
public class AddonToolSfvMws
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonToolSfvMws() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantToolSfvGui() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void updateVisibility( IEclipseContext aWinContext ) {
    MWindow mainWindow = aWinContext.get( MWindow.class );
    ITsE4Helper helper = aWinContext.get( ITsE4Helper.class );
    MMenu mmnuSfvTool = helper.findElement( mainWindow, MMNUID_SFV_TOOL, MMenu.class, EModelService.IN_MAIN_MENU );
    if( mmnuSfvTool != null ) {
      boolean visible = false;
      String perspId = helper.currentPerspId();
      if( perspId != null && perspId.equals( PERSPID_SFV_TOOL ) ) {
        visible = true;
      }
      mmnuSfvTool.setVisible( visible );
    }
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ITsE4Helper helper = aWinContext.get( ITsE4Helper.class );
    helper.perspectiveEventer().addListener( ( aSrc, aPerspId ) -> updateVisibility( aWinContext ) );
  }

}
