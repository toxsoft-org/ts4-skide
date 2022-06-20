package org.toxsoft.tool.sfv.gui.e4.services;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filebound.*;
import org.toxsoft.tool.sfv.gui.e4.main.*;

/**
 * SFV tool supporting service.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISfvToolService {

  IKeepedContentFileBound bound();

  ISfvContent content();

  // INotifierListEdit<ISfvSection> sections();
  //
  // IListEdit<ISfvSection> listSectionsById( String aSectionId );
  //
  // IGenericChangeEventer sectionsContentChangeEventer();

  ISfvSection currentSection();

  void setCurrentSection( ISfvSection aSection );

  IGenericChangeEventer currentSecionIdChangeEventer();

}
