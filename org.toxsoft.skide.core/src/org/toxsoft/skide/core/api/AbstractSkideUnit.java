package org.toxsoft.skide.core.api;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * {@link ISkideUnit} implementation.
 *
 * @author hazard157
 */
public non-sealed abstract class AbstractSkideUnit
    extends StridableParameterized
    implements ISkideUnit, ITsGuiContextable {

  private final IStridablesListEdit<ITsActionDef> unitActions = new StridablesList<>();

  private final ITsGuiContext       tsContext;
  private final AbstractSkidePlugin creatorPlugin;

  /**
   * Constructor.
   *
   * @param aId String - the plugin ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @param aContext {@link ITsGuiContext} - the context
   * @param aCreator {@link AbstractSkidePlugin} - the creator plugin
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public AbstractSkideUnit( String aId, IOptionSet aParams, ITsGuiContext aContext, AbstractSkidePlugin aCreator ) {
    super( aId, aParams );
    TsNullArgumentRtException.checkNulls( aContext, aCreator );
    tsContext = aContext;
    creatorPlugin = aCreator;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  final public void handleAction( String aActionId ) {
    if( unitActions.hasKey( aActionId ) ) {
      doHandleAction( aActionId );
    }
  }

  // ------------------------------------------------------------------------------------
  // ISkideUnit
  //

  @Override
  final public AbstractSkidePlugin skidePlugin() {
    return creatorPlugin;
  }

  @Override
  final public IStridablesListEdit<ITsActionDef> unitActions() {
    return unitActions;
  }

  @Override
  final public AbstractSkideUnitPanel createUnitPanel( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    AbstractSkideUnitPanel p = doCreateUnitPanel( aContext );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * The subclass must execute actions if any is defined in {@link #unitActions()}.
   * <p>
   * Does nothing in the base class so there is no need to call superclass method when overriding.
   *
   * @param aActionId String - ID of action, checked to be from kist of {@link #unitActions()}
   */
  protected void doHandleAction( String aActionId ) {
    // nop
  }

  /**
   * Subclass must create the new instance of the unit panel
   *
   * @param aContext {@link ITsGuiContext} - the context, not <code>null</code>
   * @return {@link AbstractSkideUnitPanel} created instance
   */
  protected abstract AbstractSkideUnitPanel doCreateUnitPanel( ITsGuiContext aContext );

}
