package org.toxsoft.tool.sfv.gui.e4.services;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filebound.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.tool.sfv.gui.e4.main.*;

/**
 * {@link ISfvToolService} implementation.
 *
 * @author hazard157
 */
public class SfvToolService
    implements ISfvToolService {

  private final ITsCollectionChangeListener sectionsListChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      // reset existing current section ID if no such section exsist after editing
      if( currSect != null && content.sections().hasElem( currSect ) ) {
        currSect = null;
        sectIdEventer.fireChangeEvent();
      }
    }
  };

  private final SfvContent              content;
  private final IKeepedContentFileBound fileBound;
  private final GenericChangeEventer    sectIdEventer;

  private ISfvSection currSect = null;

  /**
   * Constructor.
   */
  public SfvToolService() {
    sectIdEventer = new GenericChangeEventer( this );
    content = new SfvContent();
    fileBound = new KeepedContentFileBound( content, IOptionSet.NULL );
    content.sections().addCollectionChangeListener( sectionsListChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // ISfvToolService
  //

  @Override
  public IKeepedContentFileBound bound() {
    return fileBound;
  }

  @Override
  public ISfvContent content() {
    return content;
  }

  @Override
  public ISfvSection currentSection() {
    return currSect;
  }

  @Override
  public void setCurrentSection( ISfvSection aSection ) {
    if( aSection != null && !content.sections().hasElem( aSection ) ) {
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
