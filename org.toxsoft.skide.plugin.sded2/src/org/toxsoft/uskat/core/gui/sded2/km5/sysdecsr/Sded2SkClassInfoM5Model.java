package org.toxsoft.uskat.core.gui.sded2.km5.sysdecsr;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.uskat.core.gui.km5.sded.IKM5SdedConstants.*;
import static org.toxsoft.uskat.core.gui.km5.sded.ISkSdedKm5SharedResources.*;
import static org.toxsoft.uskat.core.gui.sded2.l10n.ISded2SharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.models.misc.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.gui.sded2.*;

/**
 * M5-model of {@link ISkClassInfo}.
 *
 * @author hazard157
 */
public class Sded2SkClassInfoM5Model
    extends KM5ConnectedModelBase<ISkClassInfo> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = ISded2Constants.SDED2_M5_ID + ".SkClassInfo"; //$NON-NLS-1$

  /**
   * Field {@link ISkClassInfo#id()}.
   */
  public final IM5AttributeFieldDef<ISkClassInfo> CLASS_ID = new M5AttributeFieldDef<>( FID_CLASS_ID, DDEF_IDPATH, //
      TSID_NAME, STR_SCI_CLASS_ID, //
      TSID_DESCRIPTION, STR_SCI_CLASS_ID_D, //
      M5_OPDEF_FLAGS, avInt( M5FF_INVARIANT | M5FF_COLUMN ) //
  ) {

    protected IAtomicValue doGetFieldValue( ISkClassInfo aEntity ) {
      return avStr( aEntity.id() );
    }

  };

  /**
   * Field {@link ISkClassInfo#parentId()}.
   */
  public final IM5SingleLookupFieldDef<ISkClassInfo, String> PARENT_ID =
      new M5SingleLookupFieldDef<>( FID_PARENT_ID, StringM5Model.MODEL_ID, //
          TSID_NAME, STR_SCI_PARENT_ID, //
          TSID_DESCRIPTION, STR_SCI_PARENT_ID_D, //
          M5_OPDEF_FLAGS, avInt( M5FF_INVARIANT | M5FF_DETAIL ), //
          TSID_DEFAULT_VALUE, avStr( IGwHardConstants.GW_ROOT_CLASS_ID ) //
      ) {

        @Override
        protected void doInit() {
          setLookupProvider( () -> skSysdescr().listClasses().keys() );
        }

        protected String doGetFieldValue( ISkClassInfo aEntity ) {
          return aEntity.parentId();
        }

      };

  /**
   * Field {@link ISkClassInfo#nmName()}.
   */
  public final IM5AttributeFieldDef<ISkClassInfo> NAME = new M5AttributeFieldDef<>( FID_NAME, DDEF_NAME, //
      TSID_NAME, STR_SCI_NAME, //
      TSID_DESCRIPTION, STR_SCI_NAME_D, //
      M5_OPDEF_FLAGS, avInt( M5FF_COLUMN ) //
  ) {

    protected IAtomicValue doGetFieldValue( ISkClassInfo aEntity ) {
      return avStr( aEntity.nmName() );
    }

  };

  /**
   * Attribute {@link ISkClassInfo#description()}.
   */
  public final IM5AttributeFieldDef<ISkClassInfo> DESCRIPTION =
      new M5AttributeFieldDef<>( FID_DESCRIPTION, DDEF_DESCRIPTION, //
          TSID_NAME, STR_SCI_DECSRIPTION, //
          TSID_DESCRIPTION, STR_SCI_DECSRIPTION_D, //
          M5_OPDEF_FLAGS, avInt( M5FF_COLUMN ) //
      ) {

        protected IAtomicValue doGetFieldValue( ISkClassInfo aEntity ) {
          return avStr( aEntity.description() );
        }

      };

  // TODO field PARAMS ?

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - Sk-connection to be used in constructor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Sded2SkClassInfoM5Model( ISkConnection aConn ) {
    super( MODEL_ID, ISkClassInfo.class, aConn );
    setNameAndDescription( STR_N_M5M_CLASS, STR_D_M5M_CLASS );
    addFieldDefs( CLASS_ID, NAME, PARENT_ID, DESCRIPTION );
    setPanelCreator( new M5DefaultPanelCreator<>() {

      protected IM5CollectionPanel<ISkClassInfo> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<ISkClassInfo> aItemsProvider, IM5LifecycleManager<ISkClassInfo> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<ISkClassInfo> mpc =
            new SkClassInfoMpc( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

      protected IM5CollectionPanel<ISkClassInfo> doCreateCollViewerPanel( ITsGuiContext aContext,
          IM5ItemsProvider<ISkClassInfo> aItemsProvider ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_FALSE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<ISkClassInfo> mpc = new SkClassInfoMpc( aContext, model(), aItemsProvider, null );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, true );
      }

      protected IM5FilterPanel<ISkClassInfo> doCreateFilterPanel( ITsGuiContext aContext ) {
        return new SkClassM5FilterPane( aContext, model() );
      }

    } );
  }

  // ------------------------------------------------------------------------------------
  // M5Model
  //

  @Override
  protected IM5LifecycleManager<ISkClassInfo> doCreateDefaultLifecycleManager() {
    return new Sded2SkClassInfoM5LifecycleManager( this, skConn() );
  }

  @Override
  protected IM5LifecycleManager<ISkClassInfo> doCreateLifecycleManager( Object aMaster ) {
    return new Sded2SkClassInfoM5LifecycleManager( this, ISkConnection.class.cast( aMaster ) );
  }

}
