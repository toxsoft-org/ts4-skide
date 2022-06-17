package org.toxsoft.tool.sfv.gui.e4.services;

import java.io.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.tool.sfv.gui.e4.main.*;

/**
 * SFV tool supporting service.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISfvToolService {

  File getFile();

  void open( File aFile );

  void save();

  void saveAs( File aFile );

  IGenericChangeEventer fileBindingChangeEventer();

  INotifierListEdit<ISfvSection> sections();

  IListEdit<ISfvSection> listSectionsById( String aSectionId );

  IGenericChangeEventer sectionsContentChangeEventer();

  // FIXME change to index or ISfvSection, because there may be >1 sections with the same ID
  // String currentSectionId();

  ISfvSection currentSection();

  void setCurrentSection( ISfvSection aSection );

  // unknown IDs are ignored
  // void setCurrentSectionId( String aSectionId );

  IGenericChangeEventer currentSecionIdChangeEventer();

}
