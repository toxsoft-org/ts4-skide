package org.toxsoft.skide.core.incub;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;

/**
 * Filter pane is used on top of tree/table viewer to display only selected items in it.
 * <p>
 * Generates {@link IGenericChangeListener#onGenericChangeEvent(Object)} notification when user changes fiter setting or
 * turns filter on/off in GUI.
 *
 * @author hazard157
 * @param <T> - displayed items type
 */
public interface IGenericFilterPane<T>
    extends ILazyControl<Control>, IGenericChangeEventCapable {

  /**
   * Determines if filtering is turned on.
   * <p>
   * Usually pane contains checkbox to turn on/off the filtering. However some implementations may not the ability to
   * change filtering flag. For such implementations method always returns <code>true</code> and
   * {@link #setFilterOn(boolean)} does not have any effect.
   *
   * @return boolean - a flag that filtering is on
   */
  boolean isFilterOn();

  /**
   * Turnw on/off the filtering if implementation allows it.
   *
   * @param aOn boolean - a flag that filtering is on
   */
  void setFilterOn( boolean aOn );

  /**
   * Returns the filter according to paramaters entered by the user in this panel.
   *
   * @return {@link ITsFilter}&lt;T&gt; - user-specified filter, never is <code>null</code>
   */
  ITsFilter<T> getFilter();

}
