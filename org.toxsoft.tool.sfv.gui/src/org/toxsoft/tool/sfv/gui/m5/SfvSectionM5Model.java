package org.toxsoft.tool.sfv.gui.m5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.tool.sfv.gui.m5.ITsResources.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.tool.sfv.gui.e4.main.*;
import org.toxsoft.tool.sfv.gui.e4.services.*;

/**
 * M5-model of {@link ISfvSection}.
 *
 * @author hazard157
 */
public class SfvSectionM5Model
    extends M5Model<ISfvSection> {

  /**
   * Model ID.
   */
  public static final String MODEL_ID = TS_ID + ".SfvSection"; //$NON-NLS-1$

  /**
   * ID of field {@link #SECTION_ID}
   */
  public static final String FID_SECTION_ID = "sectionId"; //$NON-NLS-1$

  /**
   * ID of field {@link #SECTION_CONTENT}
   */
  public static final String FID_SECTION_CONTENT = "sectionContent"; //$NON-NLS-1$

  /**
   * Attribute {@link ISfvSection#sectionId()}
   */
  public static final IM5AttributeFieldDef<ISfvSection> SECTION_ID =
      new M5AttributeFieldDef<>( FID_SECTION_ID, DDEF_IDPATH ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_SECTION_ID, STR_D_SECTION_ID );
          setFlags( M5FF_COLUMN );
        }

        protected IAtomicValue doGetFieldValue( ISfvSection aEntity ) {
          return avStr( aEntity.sectionId() );
        }

      };

  /**
   * Attribute {@link ISfvSection#sectionContent()}
   */
  public static final IM5AttributeFieldDef<ISfvSection> SECTION_CONTENT =
      new M5AttributeFieldDef<>( FID_SECTION_CONTENT, EAtomicType.STRING, //
          TSID_NAME, STR_N_SECTION_CONTENT, //
          TSID_DESCRIPTION, STR_D_SECTION_CONTENT, //
          OPID_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME, //
          ValedStringText.OPDEF_IS_MULTI_LINE, AV_TRUE, //
          OPID_VERTICAL_SPAN, avInt( 5 ) //
      ) {

        protected IAtomicValue doGetFieldValue( ISfvSection aEntity ) {
          return avStr( aEntity.sectionContent() );
        }

      };

  /**
   * Constructor.
   */
  public SfvSectionM5Model() {
    super( MODEL_ID, ISfvSection.class );
    setNameAndDescription( STR_N_M5M_SFV_SECTION, STR_D_M5M_SFV_SECTION );
    addFieldDefs( SECTION_ID, SECTION_CONTENT );
  }

  @Override
  protected IM5LifecycleManager<ISfvSection> doCreateDefaultLifecycleManager() {
    ISfvToolService sts = tsContext().get( ISfvToolService.class );
    return new SfvSectionM5LifecycleManager( this, sts );
  }

  @Override
  protected IM5LifecycleManager<ISfvSection> doCreateLifecycleManager( Object aMaster ) {
    return new SfvSectionM5LifecycleManager( this, ISfvToolService.class.cast( aMaster ) );
  }

}
