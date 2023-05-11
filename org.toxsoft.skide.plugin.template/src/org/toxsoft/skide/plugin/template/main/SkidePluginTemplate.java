package org.toxsoft.skide.plugin.template.main;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.plugin.template.ISkidePluginTemplateConstants.*;
import static org.toxsoft.skide.plugin.template.ISkidePluginTemplateSharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skide.core.api.*;

/**
 * SkIDE plugin: Sysdescr (classes) editor.
 *
 * @author hazard157
 */
public class SkidePluginTemplate
    extends AbstractSkidePlugin {

  /**
   * The plugin ID.
   */
  public static final String SKIDE_PLUGIN_ID = SKIDE_FULL_ID + ".plugin.template"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final AbstractSkidePlugin INSTANCE = new SkidePluginTemplate();

  SkidePluginTemplate() {
    super( SKIDE_PLUGIN_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_PLUGIN_TEMPLATE, //
        TSID_DESCRIPTION, STR_SKIDE_PLUGIN_TEMPLATE_D, //
        TSID_ICON_ID, ICONID_SKIDE_PLUGIN //
    ) );
  }

  @Override
  protected void doCreateUnits( ITsGuiContext aContext, IStridablesListEdit<ISkideUnit> aUnitsList ) {
    aUnitsList.add( new SkideUnitTemplate1( aContext, this ) );
    aUnitsList.add( new SkideUnitTemplate2( aContext, this ) );
  }

}
