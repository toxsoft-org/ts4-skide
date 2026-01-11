package org.toxsoft.undev;

import static org.toxsoft.core.tslib.gw.IGwHardConstants.*;
import static org.toxsoft.uskat.core.gui.sded2.l10n.ISded2SharedResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Panel displays {@link IDtoClassInfo} fields except properties xxxInfos().
 * <p>
 * FIXME has ability to collapse to single line panel.
 * <p>
 * Attention: this panel does not changes or in any other way uses properties infos of {@link IDtoClassInfo}.
 *
 * @author hazard157
 */
public class PanelClassNaming
    extends AbstractGenericEntityEditPanel<IDtoClassInfo> {

  /**
   * VALED used as a compact mode widget.
   *
   * @author hazard157
   */
  class ValedCompactModeWidget
      extends AbstractValedLabelAndButton<IDtoClassInfo> {

    ValedCompactModeWidget( ITsGuiContext aContext ) {
      super( aContext );
    }

    @Override
    protected void doUpdateLabelControl() {
      String text = StridUtils.printf( StridUtils.FORMAT_ID_NAME, dtoClass );
      getLabelControl().setText( text );
      getLabelControl().setToolTipText( dtoClass.description() );
    }

    @Override
    protected boolean doProcessButtonPress() {
      // TODO PanelClassNaming.ValedCompactModeWidget.doProcessButtonPress()
      TsDialogUtils.underDevelopment( getShell() );
      return false;
    }

    @Override
    protected void doDoSetUnvalidatedValue( IDtoClassInfo aValue ) {
      // nop - value is stored in wrapper panel
    }

    @Override
    protected IDtoClassInfo doGetUnvalidatedValue() {
      return dtoClass;
    }

  }

  private final IValedControlValueChangeListener valedListener = ( src, isFin ) -> this.fireChangeEvent();

  private static final IDtoClassInfo FOO_DTO_CLASS =
      new DtoClassInfo( IStridable.NONE_ID, GW_ROOT_CLASS_ID, IOptionSet.NULL );

  private IDtoClassInfo dtoClass       = FOO_DTO_CLASS;
  private Composite     backplane      = null;
  private boolean       isPanelCompact = false;

  private ValedCompactModeWidget compactModeWidget = null;

  // TODO full-size widgets?

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aIsViewer boolean - viewer flag, sets {@link #isViewer()} value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelClassNaming( ITsGuiContext aContext, boolean aIsViewer ) {
    super( aContext, aIsViewer );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Creates widgets on {@link #backplane} depending on mode {@link #isPanelCompact}..
   */
  private void recreateGui() {
    backplane.setLayoutDeferred( true );
    try {
      clearGui();
      if( isPanelCompact ) {
        createComactGui();
      }
      else {
        createFullSizedGui();
      }
    }
    finally {
      backplane.setLayoutDeferred( false );
      backplane.getParent().layout( true, true );
    }
  }

  /**
   * Removes current widgets from {@link #backplane}, de-registers listeners and performs other clean-up.
   */
  private void clearGui() {
    if( !isControlValid() ) {
      return;
    }
    if( isPanelCompact ) {
      compactModeWidget.eventer().removeListener( valedListener );
      compactModeWidget.getControl().dispose();
      compactModeWidget = null;
    }
    else {
      // TODO PanelClassNaming.clearGui()
    }
  }

  /**
   * Creates compact panel widgets on {@link #backplane}.
   */
  private void createComactGui() {
    backplane.setLayout( new BorderLayout() );
    compactModeWidget = new ValedCompactModeWidget( new TsGuiContext( tsContext() ) );
    compactModeWidget.createControl( backplane );
    compactModeWidget.getControl().setLayoutData( new BorderData( SWT.CENTER ) );
    compactModeWidget.setValue( dtoClass );
    compactModeWidget.eventer().addListener( valedListener );
  }

  /**
   * Creates full-sized panel widgets on {@link #backplane}.
   */
  private void createFullSizedGui() {
    backplane.setLayout( new GridLayout( 2, false ) );
    CLabel l;
    // ID
    l = new CLabel( backplane, SWT.LEFT );
    l.setText( STR_SCI_CLASS_ID );
    l.setToolTipText( STR_SCI_CLASS_ID_D );

    // nmName
    // parent ID
    // description
    // ??? params

    // TODO PanelClassNaming.createFullSizedGui()
  }

  // ------------------------------------------------------------------------------------
  // AbstractGenericEntityEditPanel
  //

  @Override
  protected IDtoClassInfo doGetEntity() {

    // FIXME collect data from widgets

    return dtoClass != FOO_DTO_CLASS ? dtoClass : null;
  }

  @Override
  protected void doProcessSetEntity() {
    dtoClass = specifiedEntity() != null ? specifiedEntity() : FOO_DTO_CLASS;

    // TODO display @dtoClass in widgets

  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    backplane = new Composite( aParent, SWT.BORDER );
    setControl( backplane );
    recreateGui();
    return backplane;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines if current panel display mode is a compact one.
   * <p>
   * Full-size mode is a common panel containing widgets of all displayed fields like {@link IDtoClassInfo#nmName()},
   * {@link IDtoClassInfo#parentId()}, etc. In the compact mode panel one non-editable label with string like "ClassID -
   * Class name" and a button to invoke full-size panel as a modal dialog for editing.
   *
   * @return boolean - <code>true</code> compact panel, <code>false</code> - full size
   */
  public boolean isCompactPanel() {
    return isPanelCompact;
  }

  /**
   * Sets panel to the compact or full-size display mode.
   *
   * @param aCompact boolean - <code>true</code> compact panel, <code>false</code> - full size
   */
  public void setCompactPanel( boolean aCompact ) {
    if( isPanelCompact != aCompact ) {
      isPanelCompact = aCompact;
      if( isControlValid() ) {
        recreateGui();
      }
    }
  }

}
