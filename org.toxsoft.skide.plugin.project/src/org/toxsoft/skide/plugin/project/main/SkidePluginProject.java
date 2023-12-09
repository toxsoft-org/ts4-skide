package org.toxsoft.skide.plugin.project.main;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.plugin.project.ISkidePluginProjectConstants.*;
import static org.toxsoft.skide.plugin.project.ISkidePluginProjectSharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skide.core.api.*;

/**
 * SkIDE plugin: work with project-wide settings and tasks.
 *
 * @author hazard157
 */
public class SkidePluginProject
    extends AbstractSkidePlugin {

  /**
   * The plugin ID.
   */
  public static final String SKIDE_PLUGIN_ID = SKIDE_FULL_ID + ".plugin.project"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final AbstractSkidePlugin INSTANCE = new SkidePluginProject();

  SkidePluginProject() {
    super( SKIDE_PLUGIN_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_PLUGIN_PROJECT, //
        TSID_DESCRIPTION, STR_SKIDE_PLUGIN_PROJECT_D, //
        TSID_ICON_ID, ICONID_SKIDE_PLUGIN //
    ) );
  }

  @Override
  protected void doCreateUnits( ITsGuiContext aContext, IStridablesListEdit<ISkideUnit> aUnitsList ) {
    aUnitsList.add( new SkideUnitProjectProps( aContext, this ) );
    // aUnitsList.add( new SkideUnitProject2( aContext, this ) );
  }

}
