package org.toxsoft.uskat.core.gui.glib2;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.uskat.core.gui.km5.sded.IKM5SdedConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.glib.*;

/**
 * Object editor panel - allows to create, edit and delete objects in Sysdescr.
 * <p>
 * Contains:
 * <ul>
 * <li>left pane - classes tree;</li>
 * <li>middle pane - list of objects of the selected class;</li>
 * <li>right pane - TODO selected object editors ???.</li>
 * </ul>
 *
 * @author hazard157
 */
public class SdedObjectEditor2
    extends AbstractSkLazyPanel {

  private final IM5CollectionPanel<ISkClassInfo> clsTree;
  private final IM5CollectionPanel<ISkObject>    objsList;

  /**
   * Constructor.
   * <p>
   * Panel will use {@link ISkConnection} with the given ID from {@link ISkConnectionSupplier#getConn(IdChain)}. If
   * <code>aSuppliedConnectionId</code> = <code>null</code>, then {@link ISkConnectionSupplier#defConn()} will be used.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aSuppliedConnectionId {@link IdChain} - ID of connection or <code>null</code> for default
   */
  public SdedObjectEditor2( ITsGuiContext aContext, IdChain aSuppliedConnectionId ) {
    super( aContext, aSuppliedConnectionId );
    // clsTree
    IM5Model<ISkClassInfo> m5mClass = m5().getModel( MID_SDED_SK_CLASS_INFO, ISkClassInfo.class );
    IM5LifecycleManager<ISkClassInfo> lmClass = m5mClass.getLifecycleManager( skConn() );
    IM5ItemsProvider<ISkClassInfo> ipClass = lmClass.itemsProvider();
    ITsGuiContext ctx1 = new TsGuiContext( tsContext() );
    OPDEF_IS_ACTIONS_TREE_MODES.setValue( ctx1.params(), AV_TRUE );
    OPDEF_IS_SUPPORTS_TREE.setValue( ctx1.params(), AV_TRUE );
    clsTree = m5mClass.panelCreator().createCollViewerPanel( ctx1, ipClass );
    // objList
    IM5Model<ISkObject> m5mObjs = m5().getModel( MID_SDED_SK_OBJECT, ISkObject.class );
    IM5LifecycleManager<ISkObject> lmObjs = m5mObjs.getLifecycleManager( skConn() );
    IM5ItemsProvider<ISkObject> ipObjs = lmObjs.itemsProvider();
    ITsGuiContext ctx2 = new TsGuiContext( tsContext() );
    OPDEF_IS_ACTIONS_TREE_MODES.setValue( ctx1.params(), AV_FALSE );
    OPDEF_IS_SUPPORTS_TREE.setValue( ctx1.params(), AV_FALSE );
    objsList = m5mObjs.panelCreator().createCollChecksPanel( ctx2, ipObjs );

    // TODO SdedObjectEditor2.doInitGui()
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkLazyPanel
  //

  @Override
  protected void doInitGui( Composite aParent ) {
    SashForm sfMain = new SashForm( aParent, SWT.HORIZONTAL );
    // clsTree
    clsTree.createControl( sfMain );
    objsList.createControl( sfMain );

    // TODO SdedObjectEditor2.doInitGui()
  }

}
