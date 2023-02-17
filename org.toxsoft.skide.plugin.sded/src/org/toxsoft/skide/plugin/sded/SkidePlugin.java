package org.toxsoft.skide.plugin.sded;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.plugin.sded.ISkResources.*;

import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.skide.core.main.*;
import org.toxsoft.uskat.sded.gui.*;

/**
 * {@link ISkidePlugin} implementation to register SDED in SkIDE.
 *
 * @author hazard157
 */
public class SkidePlugin
    extends AbstractSkidePlugin {

  /**
   * Singleton instance.
   */
  public static final ISkidePlugin INSTANCE = new SkidePlugin();

  private SkidePlugin() {
    super( Activator.PLUGIN_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_SKIDE_PLUGIN, //
        TSID_DESCRIPTION, STR_D_SKIDE_PLUGIN, //
        TSID_ICON_ID, ISkSdedGuiConstants.ICONID_SDED_CLASSES_LIST //
    ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkidePlugin
  //

  @Override
  protected void doInit() {
    // nop
  }

}
