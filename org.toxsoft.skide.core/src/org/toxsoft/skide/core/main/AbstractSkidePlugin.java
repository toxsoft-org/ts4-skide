package org.toxsoft.skide.core.main;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.workroom.*;

/**
 * {@link ISkidePlugin} base implementation.
 *
 * @author hazard157
 */
public non-sealed abstract class AbstractSkidePlugin
    extends StridableParameterized
    implements ISkidePlugin {

  private ITsWorkroomStorage wrStorage = null;

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public AbstractSkidePlugin( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

  // ------------------------------------------------------------------------------------
  // class API
  //

  /**
   * Initializes the registered plugin.
   * <p>
   * Method is not intended to be called by users. It is called internally by the SkIDE core.
   *
   * @param aStorage {@link ITsWorkroomStorage} - storage for this plugin
   */
  final public void initialize( ITsWorkroomStorage aStorage ) {
    TsNullArgumentRtException.checkNull( aStorage );
    wrStorage = aStorage;
    doInit();
  }

  /**
   * Returns this plugin-specific storage in SkIDE workroom.
   * <p>
   * The storage subsystem ID {@link ITsWorkroomStorage#subsysId()} is the same as this plugin ID
   * {@link ISkidePlugin#id()}.
   *
   * @return {@link ITsWorkroomStorage} - plugin storage in workroom
   */
  public ITsWorkroomStorage wrStorage() {
    TsIllegalStateRtException.checkNull( wrStorage );
    return wrStorage;
  }

  // ------------------------------------------------------------------------------------
  // ISkidePlugin
  //

  // ------------------------------------------------------------------------------------
  // to implement
  //

  /**
   * Subclass must initialize the plugin if necessary.
   * <p>
   * {@link #wrStorage} already returns the valid storage.
   */
  protected abstract void doInit();

}
