package org.toxsoft.tool.sfv.gui.e4.services;

import static org.toxsoft.tool.sfv.gui.e4.services.ITsResources.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.tool.sfv.gui.e4.main.*;

/**
 * {@link ISfvToolService} implementation.
 *
 * @author hazard157
 */
public class SfvToolService
    implements ISfvToolService {

  private static final int CONTENT_WARN_SIZE = 100 * 1024 * 1024; // 100M

  private final GenericChangeEventer fileEventer;
  private final GenericChangeEventer contentEventer;
  private final GenericChangeEventer sectIdEventer;

  private final ITsCollectionChangeListener sectionsListChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      // reset existing current section ID if no such section exsist after editing
      if( currSect != null && sectionsList.hasElem( currSect ) ) {
        currSect = null;
        sectIdEventer.fireChangeEvent();
      }
      contentEventer.fireChangeEvent();
    }
  };

  private final ITsListValidator<ISfvSection> sectionsChangeValidator = new ITsListValidator<>() {

    private ValidationResult checkSfvSection( ISfvSection aSection ) {
      if( aSection.sectionId().isBlank() ) {
        return ValidationResult.error( FMT_ERR_SECT_ID_BLANK, aSection.sectionId() );
      }
      if( !StridUtils.isValidIdPath( aSection.sectionId() ) ) {
        return ValidationResult.error( FMT_ERR_SECT_ID_NON_IDPATH, aSection.sectionId() );
      }
      if( aSection.sectionContent().length() > CONTENT_WARN_SIZE ) {
        return ValidationResult.warn( FMT_WARN_BIG_CONTENT, aSection.sectionId() );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canReplace( INotifierList<ISfvSection> aSource, ISfvSection aExistingItem,
        ISfvSection aNewItem ) {
      ValidationResult vr = checkSfvSection( aNewItem );
      if( !vr.isError() ) {
        IListEdit<ISfvSection> foundSects = listSectionsById( aNewItem.sectionId() );
        foundSects.remove( aExistingItem );
        if( !foundSects.isEmpty() ) {
          vr = ValidationResult.firstNonOk( vr,
              ValidationResult.warn( FMT_WARN_DUP_SECT_ID_EXIST, aNewItem.sectionId() ) );
        }
      }
      return vr;
    }

    @Override
    public ValidationResult canRemove( INotifierList<ISfvSection> aSource, ISfvSection aRemovingItem ) {
      if( !sectionsList.hasElem( aRemovingItem ) ) {
        return ValidationResult.error( FMT_ERR_SECT_NOT_EXIST, aRemovingItem.sectionId() );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canAdd( INotifierList<ISfvSection> aSource, ISfvSection aNewItem ) {
      ValidationResult vr = checkSfvSection( aNewItem );
      if( !vr.isError() ) {
        if( !listSectionsById( aNewItem.sectionId() ).isEmpty() ) {
          vr = ValidationResult.firstNonOk( vr, ValidationResult.warn( FMT_WARN_SECT_ID_EXIST, aNewItem.sectionId() ) );
        }
      }
      return vr;
    }
  };

  private final INotifierListEdit<ISfvSection> sectionsList =
      new NotifierListEditWrapper<>( new ElemLinkedBundleList<>() );

  private File        file     = null;
  private ISfvSection currSect = null;

  /**
   * Constructor.
   */
  public SfvToolService() {
    fileEventer = new GenericChangeEventer( this );
    contentEventer = new GenericChangeEventer( this );
    sectIdEventer = new GenericChangeEventer( this );
    sectionsList.addCollectionChangeListener( sectionsListChangeListener );
    sectionsList.addCollectionChangeValidator( sectionsChangeValidator );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ISfvSection findFirstSectionById( String aSectionId ) {
    for( ISfvSection ss : sectionsList ) {
      if( ss.sectionId().equals( aSectionId ) ) {
        return ss;
      }
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // ISfvToolService
  //

  @Override
  public File getFile() {
    return file;
  }

  @Override
  public void open( File aFile ) {
    sectionsList.setAll( SfvToolUtils.openFile( aFile ) );
    if( !Objects.equals( aFile, file ) ) {
      file = aFile;
      fileEventer.fireChangeEvent();
    }
    contentEventer.fireChangeEvent();
    if( currSect != null ) {
      currSect = null;
      sectIdEventer.fireChangeEvent();
    }
  }

  @Override
  public void save() {
    TsIllegalArgumentRtException.checkNull( file );
    saveAs( file );
  }

  @Override
  public void saveAs( File aFile ) {
    SfvToolUtils.saveFile( aFile, sectionsList );
    if( !Objects.equals( aFile, file ) ) {
      file = aFile;
      fileEventer.fireChangeEvent();
    }
  }

  @Override
  public IGenericChangeEventer fileBindingChangeEventer() {
    return fileEventer;
  }

  @Override
  public INotifierListEdit<ISfvSection> sections() {
    return sectionsList;
  }

  @Override
  public IListEdit<ISfvSection> listSectionsById( String aSectionId ) {
    TsNullArgumentRtException.checkNull( aSectionId );
    IListEdit<ISfvSection> ll = new ElemArrayList<>();
    for( ISfvSection ss : sectionsList ) {
      if( ss.sectionId().equals( aSectionId ) ) {
        ll.add( ss );
      }
    }
    return ll;
  }

  @Override
  public IGenericChangeEventer sectionsContentChangeEventer() {
    return contentEventer;
  }

  @Override
  public ISfvSection currentSection() {
    return currSect;
  }

  @Override
  public void setCurrentSection( ISfvSection aSection ) {
    if( aSection != null && !sectionsList.hasElem( aSection ) ) {
      return;
    }
    if( aSection != currSect ) {
      currSect = aSection;
      sectIdEventer.fireChangeEvent();
    }
  }

  @Override
  public IGenericChangeEventer currentSecionIdChangeEventer() {
    return sectIdEventer;
  }

}
