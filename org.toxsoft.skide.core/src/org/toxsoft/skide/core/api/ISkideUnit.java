package org.toxsoft.skide.core.api;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * SkIDE project unit is visual representation of the functionality contributed by the plugin.
 * <p>
 * Each plugin contributes one or more unit to the SkIDE. Although it is possible to create plugin without project units
 * it seems not to have musct sense.
 * <p>
 * Units must have unique IDs. {@link #nmName()} and {@link #description()} is used together with {@link #iconId()} to
 * visualize unit in the list of the units in the SkIDE project perspective. The method
 * {@link #createUnitPanel(ITsGuiContext)} creates panel used to visualize selected item in the list.
 *
 * @author hazard157
 */
public sealed interface ISkideUnit
    extends IStridableParameterized, ITsActionHandler permits AbstractSkideUnit {

  /**
   * Returns the creator plugin.
   *
   * @return {@link AbstractSkidePlugin} - the plugin
   */
  AbstractSkidePlugin skidePlugin();

  /**
   * Returns the actions accosiated with the unit.
   * <p>
   * Method is called every time when unit is becames selected in SkiDE project perspective.
   * <p>
   * Actions are shown in toolbar and/or pop-up menu only when this unit is selected.
   * <p>
   * TODO special flag to show action in menu/toolbar/both
   *
   * @return {@link IStridablesList}&lt;{@link ITsActionDef}&gt; - the list of the unit actions definitions
   */
  IStridablesList<ITsActionDef> unitActions();

  // FIXME change panel to be created once

  /**
   * Creates new instance of the unit panel.
   * <p>
   * Method is called every time when unit is becames selected in SkiDE project perspective.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link AbstractSkideUnitPanel}
   */
  AbstractSkideUnitPanel createUnitPanel( ITsGuiContext aContext );

}
