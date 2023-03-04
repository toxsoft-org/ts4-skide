package org.toxsoft.skide.core.mainconn_old;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.mainconn_old.ISkResources.*;

import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.skide.core.*;
import org.toxsoft.skide.core.env.*;
import org.toxsoft.uskat.base.gui.conn.*;

/**
 * Manages SkIDE main connection.
 * <p>
 * Main connection is always {@link ISkConnectionSupplier#defConn()}.
 *
 * @author hazard157
 */
public class SkidePluginConnCfg
    extends AbstractSkidePlugin {

  /**
   * ID of this DkIDE plugin.
   */
  public static final String SKIDE_PLUGIN_ID = "org.toxsoft.skide.core.mainconn"; //$NON-NLS-1$

  /**
   * Singleton instance.
   */
  public static final ISkidePlugin INSTANCE = new SkidePluginConnCfg();

  /**
   * TODO Надо следать управление соединениями как встроенная возможность.
   * <p>
   * Речь идет об управлении главным соединением.
   */

  private SkidePluginConnCfg() {
    super( SKIDE_PLUGIN_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_SKIDE_PLUGIN, //
        TSID_DESCRIPTION, STR_D_SKIDE_PLUGIN, //
        TSID_ICON_ID, ISkideCoreConstants.ICONID_APP_ICON //
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
