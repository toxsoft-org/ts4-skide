package org.toxsoft.tool.sfv.gui.e4.uiparts;

import static org.toxsoft.tool.sfv.gui.IToolSfvGuiConstants.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.toxsoft.core.tsgui.graphics.fonts.IFontInfo;
import org.toxsoft.core.tsgui.mws.bases.MwsAbstractPart;
import org.toxsoft.core.tslib.bricks.apprefs.IPrefBundle;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.tool.sfv.gui.e4.services.ISfvToolService;

import jakarta.inject.Inject;

/**
 * SFV tool: view content of the section.
 *
 * @author hazard157
 */
public class UipartSfvSectionContent
    extends MwsAbstractPart {

  private final IGenericChangeListener stsListener = aSource -> {
    if( this.sfvToolService.currentSection() != null ) {
      this.txtContent.setText( this.sfvToolService.currentSection().sectionContent() );
    }
    else {
      this.txtContent.setText( TsLibUtils.EMPTY_STRING );
    }
  };

  @Inject
  ISfvToolService sfvToolService;

  Text        txtContent;
  IPrefBundle prefBundle;

  @Override
  protected void doInit( Composite aParent ) {
    prefBundle = appPrefs().getBundle( PBID_SFV_TOOL );
    prefBundle.prefs().addCollectionChangeListener( ( aSource, aOp, aItem ) -> {
      if( aItem == null || ((String)aItem).equals( APPRM_SECTION_FONT.id() ) ) {
        IFontInfo finf = getAppPrefsValue( PBID_SFV_TOOL, APPRM_SECTION_FONT ).asValobj();
        Font font = fontManager().getFont( finf );
        txtContent.setFont( font );
      }
    } );
    txtContent = new Text( aParent, SWT.READ_ONLY | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
    IFontInfo finf = getAppPrefsValue( PBID_SFV_TOOL, APPRM_SECTION_FONT ).asValobj();
    Font font = fontManager().getFont( finf );
    txtContent.setFont( font );
    sfvToolService.currentSecionIdChangeEventer().addListener( stsListener );
  }

}
