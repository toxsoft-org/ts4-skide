package org.toxsoft.skide.plugin.project.e4.addons;

import static org.toxsoft.skide.plugin.project.ISkidePluginProjectSharedResources.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.plugin.project.*;
import org.toxsoft.skide.plugin.project.main.*;

/**
 * Plugin addon.
 *
 * @author hazard157
 */
public class AddonSkidePluginProject
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkidePluginProject() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * if project ID or ALIAS has default or invalid values, invoke the project properties edit dialog
   *
   * @param aWinContext {@link IEclipseContext} - window level context
   */
  void editProjectPropertiesIfNeeded( IEclipseContext aWinContext ) {
    // check project ID and ALIAS validity
    ISkideProjectProperties spp = aWinContext.get( ISkideEnvironment.class ).projectProperties();
    String projId = spp.projId();
    String projAlias = spp.projAlias();
    boolean needProIdEdit = projId.isBlank() //
        || projId.equals( ISkideProjectPropertiesConstants.DEFAULT_PROJECT_ID ) //
        || !StridUtils.isValidIdPath( projId );
    boolean needProAliasEdit = projAlias.isBlank() //
        || projAlias.equals( ISkideProjectPropertiesConstants.DEFAULT_PROJECT_ALIAS )//
        || !StridUtils.isValidIdName( projAlias );
    if( !needProIdEdit && !needProAliasEdit ) {
      return;
    }
    // invoke project properties edit dialog
    ITsDialogInfo tdi = new TsDialogInfo( new TsGuiContext( aWinContext ), DLG_EDIT_PROJ_PROPS, DLG_EDIT_PROJ_PROPS_D );
    IOptionSet ops = DialogOptionsEdit.editOpset( tdi, ISkideProjectPropertiesConstants.ALL_SPP_OPS, spp.params()
    // , aValue -> spp.svs().validator().validate( aValue )
    );
    if( ops != null ) {
      spp.setProperties( ops );
    }
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ISkideEnvironment skEnv = aAppContext.get( ISkideEnvironment.class );
    skEnv.pluginsRegistrator().registerPlugin( SkidePluginProject.INSTANCE );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkidePluginProjectConstants.init( aWinContext );
    //
    Display display = aWinContext.get( Display.class );
    display.asyncExec( () -> {
      editProjectPropertiesIfNeeded( aWinContext );
    } );
  }

}
