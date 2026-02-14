package org.toxsoft.uskat.core.gui.sded2.km5.sysdecsr;

import static org.toxsoft.uskat.core.gui.sded2.ISded2Constants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * M5-model of Sk-class property of any kind.
 * <p>
 * The model is designed to represent all properties of a class as a single list.
 *
 * @author hazard157
 */
public class Sded2DtoPropertyInfoM5Model
    extends KM5ConnectedModelBase<IDtoClassPropInfoBase> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = SDED2_M5_ID + ".DtoCmdInfo"; //$NON-NLS-1$

  public final IM5AttributeFieldDef<ESkClassPropKind> KIND = null; // FIXME null

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - Sk-connection to be used in constructor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Sded2DtoPropertyInfoM5Model( ISkConnection aConn ) {
    super( MODEL_ID, IDtoClassPropInfoBase.class, aConn );
    // TODO Auto-generated constructor stub
  }

}
