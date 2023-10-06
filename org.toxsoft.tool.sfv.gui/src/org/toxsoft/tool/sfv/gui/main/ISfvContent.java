package org.toxsoft.tool.sfv.gui.main;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.notifier.*;

/**
 * Sectioned content.
 *
 * @author hazard157
 */
public interface ISfvContent
    extends IGenericChangeEventCapable {

  /**
   * Returns all the sections.
   *
   * @return {@link INotifierListEdit}&lt;{@link ISfvSection}&gt; - an editable sections list
   */
  INotifierListEdit<ISfvSection> sections();

  /**
   * Lists all sections with the specified ID in the order of appearance in {@link #sections()}.
   *
   * @param aSectionId String - section ID
   * @return {@link IListEdit}&lt;{@link ISfvSection}&gt; - an editable instance of newly created sections list
   */
  IListEdit<ISfvSection> listSectionsById( String aSectionId );

}
