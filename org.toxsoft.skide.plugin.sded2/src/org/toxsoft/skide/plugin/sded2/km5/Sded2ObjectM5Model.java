package org.toxsoft.skide.plugin.sded2.km5;

import static org.toxsoft.skide.plugin.sded2.ISkidePluginSded2Constants.*;

import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * M5-model of {@link ISkObject} to be used with SDED object editor.
 *
 * @author hazard157
 */
public class Sded2ObjectM5Model
    extends M5Model<ISkObject> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = SDED2_M5_ID + ".SkObject"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public Sded2ObjectM5Model() {
    super( MODEL_ID, ISkObject.class );
  }

}
