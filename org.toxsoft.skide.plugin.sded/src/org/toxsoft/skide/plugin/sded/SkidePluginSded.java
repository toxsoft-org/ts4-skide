package org.toxsoft.skide.plugin.sded;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.plugin.sded.ISkResources.*;

import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.skide.core.env.*;
import org.toxsoft.uskat.core.gui.*;

/**
 * {@link ISkidePlugin} implementation to register SDED in SkIDE.
 *
 * @author hazard157
 */
public class SkidePluginSded
    extends AbstractSkidePlugin {

  // FIXME contribute Sysdescr to the main connection project tree node

  // FIXME contribute ObjectsService to the main connection project tree node

  /**
   * Singleton instance.
   */
  public static final ISkidePlugin INSTANCE = new SkidePluginSded();

  private SkidePluginSded() {
    super( Activator.PLUGIN_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_SKIDE_PLUGIN, //
        TSID_DESCRIPTION, STR_D_SKIDE_PLUGIN, //
        TSID_ICON_ID, ISkCoreGuiConstants.ICONID_SDED_CLASSES_LIST //
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
