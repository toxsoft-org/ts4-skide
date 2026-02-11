package org.toxsoft.uskat.core.gui.sded2.km5.sysdecsr;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.uskat.core.gui.km5.sded.IKM5SdedConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;
import org.toxsoft.core.tsgui.panels.vecboard.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

/**
 * {@link IM5EntityPanel} implementation to be returned by {@link Sded2SkClassInfoM5Model}.
 * <p>
 * Entity panel contains:
 * <ul>
 * <li>top part - ladder layout field editors for fields class ID, parent ID, name and description;</li>
 * <li>bottom part - tab folder with separate tab for class properties like: attr, links, RTdata, etc.</li>
 * </ul>
 *
 * @author hazard157
 */
class Sded2SkClassInfoM5EntityPanel
    extends M5DefaultEntityEditorPanel<ISkClassInfo> {

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - entity model
   * @param aLifecycleManager {@link IM5LifecycleManager} - optional lifecycle manager, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Sded2SkClassInfoM5EntityPanel( ITsGuiContext aContext, IM5Model<ISkClassInfo> aModel,
      IM5LifecycleManager<ISkClassInfo> aLifecycleManager ) {
    super( aContext, aModel, aLifecycleManager );
  }

  // ------------------------------------------------------------------------------------
  // M5DefaultEntityEditorPanel
  //

  @Override
  protected void doInitLayout() {
    // split editors per tabs: tab per class property kind and all other editors on "Parameters" tab
    IStringMapEdit<IValedControl<?>> paramsTabEditors = new StringMap<>();
    IStringMapEdit<IValedControl<?>> otherTabPerEditors = new StringMap<>();
    for( String fieldId : editors().keys() ) {
      switch( fieldId ) {
        case FID_CLASS_ID:
        case FID_PARENT_ID:
        case TSID_NAME:
        case TSID_DESCRIPTION: {
          paramsTabEditors.put( fieldId, editors().getByKey( fieldId ) );
          break;
        }
        default: {
          otherTabPerEditors.put( fieldId, editors().getByKey( fieldId ) );
          break;
        }
      }
    }
    // main board
    VecTabLayout vecMain = new VecTabLayout( false );
    board().setLayout( vecMain );
    // board for "Parameters" tab
    IVecBoard vbParams = new VecBoard();
    vbParams.setLayout( makeLadderLayout( model(), paramsTabEditors ) );
    // Tab "Parameters"
    IVecTabLayoutData ld = new VecTabLayoutData( "Params", "???", ICONID_DIALOG_INFORMATION, EIconSize.IS_16X16 );
    vecMain.addControl( vbParams, ld );
    // other tabs
    for( String fieldId : otherTabPerEditors.keys() ) {
      IValedControl<?> varEditor = otherTabPerEditors.getByKey( fieldId );
      IM5FieldDef<?, ?> fd = model().fieldDefs().getByKey( fieldId );
      String label = TsLibUtils.EMPTY_STRING;
      if( !fd.nmName().isEmpty() ) {
        label = "  " + fd.nmName(); //$NON-NLS-1$
      }
      String tooltip = fd.description();
      IVecTabLayoutData layoutData = new VecTabLayoutData( label, tooltip, fd.iconId(), EIconSize.IS_16X16 );
      vecMain.addControl( varEditor, layoutData );
    }

  }

  /**
   * Puts each editor in the separate tab of the newly created {@link IVecTabLayout}.
   *
   * @param aEditors {@link IStringMap}&lt;{@link IValedControl}&gt; - map "field ID" - "field editor"
   * @return {@link IVecLadderLayout} - created tab layout
   */
  public IVecTabLayout makeTabPerEditorLayout( IStringMap<IValedControl<?>> aEditors ) {
    IVecTabLayout ll = new VecTabLayout( false );
    for( String fieldId : aEditors.keys() ) {
      IValedControl<?> varEditor = aEditors.getByKey( fieldId );
      IM5FieldDef<?, ?> fd = model().fieldDefs().getByKey( fieldId );
      String label = TsLibUtils.EMPTY_STRING;
      if( !fd.nmName().isEmpty() ) {
        label = "  " + fd.nmName(); //$NON-NLS-1$
      }
      String tooltip = fd.description();
      IVecTabLayoutData layoutData = new VecTabLayoutData( label, tooltip, fd.iconId(), EIconSize.IS_16X16 );
      ll.addControl( varEditor, layoutData );
    }
    return ll;
  }

}
