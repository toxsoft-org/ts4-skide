package org.toxsoft.uskat.core.gui.sded2.km5.skobj;

import static org.toxsoft.uskat.core.gui.sded2.ISded2Constants.*;

import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * M5-model of {@link ISkObject} to be used with SDED object editor.
 * <p>
 * Note: this model does not creates lifecycle manager, create {@link Sded2SkObjectM5LifecycleManager} instead.
 *
 * @author hazard157
 */
public class Sded2SkObjectM5Model
    extends KM5ConnectedModelBase<ISkObject>
    implements ISkConnected {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = SDED2_M5_ID + ".SkObject"; //$NON-NLS-1$

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - Sk-connection to be used in constructor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Sded2SkObjectM5Model( ISkConnection aConn ) {
    super( MODEL_ID, ISkObject.class, aConn );
  }

}
