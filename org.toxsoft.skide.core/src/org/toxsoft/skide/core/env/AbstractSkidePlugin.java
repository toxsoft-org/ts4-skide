package org.toxsoft.skide.core.env;

import org.toxsoft.core.tsgui.bricks.ctx.*;
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
    implements ISkidePlugin, ITsGuiContextable {

  private final SkideProjectTreeContribution ptContribution = new SkideProjectTreeContribution();

  private ITsGuiContext      tsContext = null;
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
   * @param aContext {@link ITsGuiContext} - the context
   * @param aStorage {@link ITsWorkroomStorage} - storage for this plugin
   */
  final public void initialize( ITsGuiContext aContext, ITsWorkroomStorage aStorage ) {
    TsNullArgumentRtException.checkNulls( aContext, aStorage );
    tsContext = aContext;
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
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    TsIllegalStateRtException.checkNull( tsContext );
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // ISkidePlugin
  //

  @Override
  public SkideProjectTreeContribution projTreeContribution() {
    return ptContribution;
  }

  // ------------------------------------------------------------------------------------
  // to implement
  //

  /**
   * Subclass must initialize the plugin if necessary.
   * <p>
   * {@link #tsContext()} and {@link #wrStorage()} already returns the valid values.
   */
  protected abstract void doInit();

}
