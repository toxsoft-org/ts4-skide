package org.toxsoft.tool.sfv.gui.m5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.tool.sfv.gui.e4.main.*;
import org.toxsoft.tool.sfv.gui.e4.services.*;

/**
 * M5 lifecycle manager for model {@link SfvSectionM5Model}.
 *
 * @author hazard157
 */
class SfvSectionM5LifecycleManager
    extends M5LifecycleManager<ISfvSection, ISfvToolService> {

  final IListReorderer<ISfvSection> reorderer;

  SfvSectionM5LifecycleManager( IM5Model<ISfvSection> aModel, ISfvToolService aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
    reorderer = new ListReorderer<>( master().content().sections() );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<ISfvSection> aValues ) {
    String sectId = aValues.getAsAv( SfvSectionM5Model.FID_SECTION_ID ).asString();
    String sectContent = aValues.getAsAv( SfvSectionM5Model.FID_SECTION_CONTENT ).asString();
    ISfvSection ss = new SfvSection( sectId, sectContent );
    return master().content().sections().canAdd( ss );
  }

  @Override
  protected ISfvSection doCreate( IM5Bunch<ISfvSection> aValues ) {
    String sectId = aValues.getAsAv( SfvSectionM5Model.FID_SECTION_ID ).asString();
    String sectContent = aValues.getAsAv( SfvSectionM5Model.FID_SECTION_CONTENT ).asString();
    ISfvSection ss = new SfvSection( sectId, sectContent );
    master().content().sections().add( ss );
    return ss;
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ISfvSection> aValues ) {
    String sectId = aValues.getAsAv( SfvSectionM5Model.FID_SECTION_ID ).asString();
    String sectContent = aValues.getAsAv( SfvSectionM5Model.FID_SECTION_CONTENT ).asString();
    ISfvSection ss = new SfvSection( sectId, sectContent );
    int prevIndex = master().content().sections().indexOf( aValues.originalEntity() );
    return master().content().sections().canReplace( prevIndex, ss );
  }

  @Override
  protected ISfvSection doEdit( IM5Bunch<ISfvSection> aValues ) {
    String sectId = aValues.getAsAv( SfvSectionM5Model.FID_SECTION_ID ).asString();
    String sectContent = aValues.getAsAv( SfvSectionM5Model.FID_SECTION_CONTENT ).asString();
    ISfvSection ss = new SfvSection( sectId, sectContent );
    int prevIndex = master().content().sections().indexOf( aValues.originalEntity() );
    master().content().sections().set( prevIndex, ss );
    return ss;
  }

  @Override
  protected ValidationResult doBeforeRemove( ISfvSection aEntity ) {
    int index = master().content().sections().indexOf( aEntity );
    return master().content().sections().canRemove( index );
  }

  @Override
  protected void doRemove( ISfvSection aEntity ) {
    master().content().sections().remove( aEntity );
  }

  @Override
  protected IList<ISfvSection> doListEntities() {
    return master().content().sections();
  }

  @Override
  protected IListReorderer<ISfvSection> doGetItemsReorderer() {
    return reorderer;
  }

}
