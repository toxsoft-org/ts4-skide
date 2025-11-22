package org.toxsoft.skide.plugin.sded2.main;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.plugin.sded2.ISkidePluginSded2Constants.*;
import static org.toxsoft.skide.plugin.sded2.l10n.ISkidePluginSdedSharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skide.core.api.*;

/**
 * SkIDE plugin: Sysdescr (classes) editor.
 *
 * @author hazard157
 */
public class SkidePluginSded
    extends AbstractSkidePlugin {

  /**
   * The plugin ID.
   */
  public static final String SKIDE_PLUGIN_ID = SKIDE_FULL_ID + ".plugin.sded2"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final AbstractSkidePlugin INSTANCE = new SkidePluginSded();

  SkidePluginSded() {
    super( SKIDE_PLUGIN_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_SDED_PLUGIN, //
        TSID_DESCRIPTION, STR_SKIDE_SDED_PLUGIN_D, //
        TSID_ICON_ID, ICONID_SYSDESCR //
    ) );
  }

  @Override
  protected void doCreateUnits( ITsGuiContext aContext, IStridablesListEdit<ISkideUnit> aUnitsList ) {
    aUnitsList.add( new SkideUnitSdedClasses( aContext, this ) );
    aUnitsList.add( new SkideUnitSdedObjects( aContext, this ) );
  }

}
