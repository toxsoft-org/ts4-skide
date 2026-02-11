package org.toxsoft.uskat.core.gui.sded2.gui;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.inpled.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.glib.*;
import org.toxsoft.uskat.core.gui.glib.widgets.*;
import org.toxsoft.uskat.core.gui.sded2.km5.sysdecsr.*;

/**
 * Class editor - allows to create, edit and delete classes in {@link ISkCoreApi#sysdescr()}.
 * <p>
 * Panel contents:
 * <ul>
 * <li>left pane - classes list;</li>
 * <li>right pane - selected class editor with two tabs:
 * <ul>
 * <li>properties editor (each tab - properties of one {@link ESkClassPropKind});</li>
 * <li>single tree view of all tabs.</li>
 * </ul>
 * .</li>
 * </ul>
 * <p>
 * Notes:
 * <ul>
 * <li>FIXME Handles {@link ISkSysdescr#eventer()} events to update displayed classes list;</li>
 * <li>To access USkat core, the {@link ISkConnection} is used as specified in constructor.</li>
 * </ul>
 *
 * @author hazard157
 */
public class Sded2ClassEditor
    extends AbstractSkLazyPanel {

  private final ISkSysdescrListener sysdescrListener = ( api, op, classId ) -> whenSysdescrChanged( op, classId );

  private final IM5CollectionPanel<ISkClassInfo> classesListPane;
  private final GwidViewWidget                   gwidViewWidget;
  private final IM5EntityPanel<ISkClassInfo>     classEditPane;
  private final IInplaceEditorPanel              inplaceEditor;

  /**
   * When this flag is <code>true</code> selection events are ignored in the handler
   * {@link #whenClassListSelectionChanges()}.
   * <p>
   * Flag set/reset in {@link #whenSysdescrChanged(ECrudOp, String)}.
   */
  private boolean ignoreSelectionChange = false;

  /**
   * Constructor.
   * <p>
   * Panel will use {@link ISkConnection} with the given ID from {@link ISkConnectionSupplier#getConn(IdChain)}. If
   * <code>aSuppliedConnectionId</code> = <code>null</code>, then {@link ISkConnectionSupplier#defConn()} will be used.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aSuppliedConnectionId {@link IdChain} - ID of connection or <code>null</code> for default
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Sded2ClassEditor( ITsGuiContext aContext, IdChain aSuppliedConnectionId ) {
    super( aContext, aSuppliedConnectionId );
    // left pane
    IM5Model<ISkClassInfo> modelSk = m5().getModel( Sded2SkClassInfoM5Model.MODEL_ID, ISkClassInfo.class );
    IM5LifecycleManager<ISkClassInfo> lmSk = modelSk.getLifecycleManager( skConn() );
    IM5ItemsProvider<ISkClassInfo> ipSk = lmSk.itemsProvider();
    ITsGuiContext ctx1 = new TsGuiContext( tsContext() );
    OPDEF_IS_FILTER_PANE.setValue( ctx1.params(), AV_TRUE );
    classesListPane = modelSk.panelCreator().createCollEditPanel( ctx1, ipSk, lmSk );

    // right pane components
    gwidViewWidget = new GwidViewWidget( tsContext() );
    IM5Model<ISkClassInfo> modelDto = m5().getModel( Sded2SkClassInfoM5Model.MODEL_ID, ISkClassInfo.class );
    IM5LifecycleManager<ISkClassInfo> lmDto = modelDto.getLifecycleManager( skConn() );
    ITsGuiContext ctxDto = new TsGuiContext( tsContext() );
    classEditPane = modelDto.panelCreator().createEntityEditorPanel( ctxDto, lmDto );
    classEditPane.setEditable( false );
    AbstractContentPanel contentPanel = new InplaceContentM5EntityPanelWrapper<>( ctxDto, classEditPane );
    inplaceEditor = new InplaceEditorContainerPanel( aContext, contentPanel );
  }

  @Override
  protected void doDispose() {
    skSysdescr().eventer().removeListener( sysdescrListener );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Handles selection change in the left panel {@link #classesListPane}.
   */
  private void whenClassListSelectionChanges() {
    if( ignoreSelectionChange ) {
      return;
    }
    ISkClassInfo sel = classesListPane.selectedItem();
    if( inplaceEditor.isEditing() ) {
      inplaceEditor.cancelAndFinishEditing();
    }
    if( sel != null ) {
      // IDtoClassInfo dto = DtoClassInfo.createFromSk( sel, true );
      classEditPane.setEntity( sel );
      gwidViewWidget.setGwid( Gwid.createClass( sel.id() ) );
    }
    else {
      classEditPane.setEntity( null );
      gwidViewWidget.setGwid( null );
    }
    inplaceEditor.refresh();
  }

  /**
   * Handles class(es) changes in {@link ISkSysdescr}, is called from {@link ISkSysdescrListener}.
   *
   * @param aOp {@link ECrudOp} - the kind of change
   * @param aClassId String - affected class ID or <code>null</code> for batch changes {@link ECrudOp#LIST}
   */
  private void whenSysdescrChanged( ECrudOp aOp, String aClassId ) {
    ISkClassInfo sel = classesListPane.selectedItem();
    // no selected class means that there is nothing in right panel, just refresh left panel
    if( sel == null ) {
      classesListPane.refresh();
      return;
    }
    String selClassId = sel.id();
    ignoreSelectionChange = true;
    try {
      classesListPane.refresh();
      sel = skSysdescr().findClassInfo( selClassId );
      classesListPane.setSelectedItem( sel );
      if( inplaceEditor.isEditing() ) {
        // IDtoClassInfo dto = DtoClassInfo.createFromSk( sel, true );
        classEditPane.setEntity( sel );
      }
    }
    finally {
      ignoreSelectionChange = false;
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected void doInitGui( Composite aParent ) {
    SashForm sfMain = new SashForm( aParent, SWT.HORIZONTAL );
    classesListPane.createControl( sfMain );
    // right pane
    Composite rightPane = new Composite( sfMain, SWT.NONE );
    rightPane.setLayout( new BorderLayout() );
    gwidViewWidget.createControl( rightPane );
    gwidViewWidget.getControl().setLayoutData( new BorderData( SWT.TOP ) );
    inplaceEditor.createControl( rightPane );
    inplaceEditor.getControl().setLayoutData( new BorderData( SWT.CENTER ) );
    sfMain.setWeights( 3000, 7000 );
    // setup
    classesListPane.addTsSelectionListener( ( s, i ) -> whenClassListSelectionChanges() );
    skSysdescr().eventer().addListener( sysdescrListener );
  }

}
