package org.toxsoft.skide.plugin.exconn.main;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnConstants.*;
import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnSharedResources.*;

import org.toxsoft.core.tsgui.panels.misc.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.login.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.plugin.exconn.service.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.conn.cfg.*;
import org.toxsoft.uskat.core.gui.conn.valed.*;

/**
 * Code generation task meta-information.
 *
 * @author hazard157
 */
public final class UploadToServerTaskProcessor
    extends SkideTaskProcessor {

  /**
   * The task ID.
   */
  public static final String TASK_ID = SKIDE_FULL_ID + ".task.UploadToServer"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_IN_EXCONN_ID}.
   */
  public static final String OPID_IN_EXCONN_ID = TASK_ID + ".inOp.ExconnId"; //$NON-NLS-1$

  /**
   * Input option: ID of the connection config to open, one of the {@link IConnectionConfigService#listConfigs()}.
   */
  public static final IDataDef OPDEF_IN_EXCONN_ID = DataDef.create3( OPID_IN_EXCONN_ID, DDEF_IDPATH, //
      TSID_NAME, STR_OP_IN_EXCONN_ID, //
      TSID_DESCRIPTION, STR_OP_IN_EXCONN_ID_D, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvStringConnConfIdCombo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, AV_STR_NONE_ID //
  );

  /**
   * Input reference: open {@link ISkConnection} to the upload destination server.
   */
  public static final ITsContextRefDef<ISkConnection> REFDEF_IN_OPEN_SK_CONN =
      new TsContextRefDef<>( ISkConnection.class, //
          OptionSetUtils.createOpSet( //
              TSID_IS_MANDATORY, AV_TRUE //
          ) );

  private static final IGenericTaskInfo TASK_INFO = new GenericTaskInfo( TASK_ID, OptionSetUtils.createOpSet( //
      TSID_NAME, STR_TASK_UPLOAD, //
      TSID_DESCRIPTION, STR_TASK_UPLOAD_D, //
      TSID_ICON_ID, ICONID_TASK_UPLOAD //
  ) ) {

    {
      inOps().add( OPDEF_IN_EXCONN_ID );
      // inRefs().add( REFDEF_OPEN_SK_CONN );
    }

  };

  /**
   * The singleton instance
   */
  public static final SkideTaskProcessor INSTANCE = new UploadToServerTaskProcessor();

  private UploadToServerTaskProcessor() {
    super( TASK_INFO );
  }

  @Override
  protected void doBeforeRunTask( ITsContext aInput ) {
    String ccId = OPDEF_IN_EXCONN_ID.getValue( aInput.params() ).asString();
    // ask LoginInfo if needed
    IConnectionConfigService ccService = tsContext().get( IConnectionConfigService.class );
    IConnectionConfig cc = ccService.listConfigs().getByKey( ccId );
    IConnectionConfigProvider ccProvider = ccService.listProviders().getByKey( cc.providerId() );
    ILoginInfo loginInfo = ILoginInfo.NONE; // TODO load default lpogin info from somewhere
    switch( ccProvider.backendMetaInfo().getAuthentificationType() ) {
      case NONE: {
        break;
      }
      case SIMPLE: {
        loginInfo = PanelLoginInfo.edit( tsContext(), loginInfo, ITsValidator.PASS );
        if( loginInfo == null ) {
          loginInfo = ILoginInfo.NONE; // try to connect with default login info
        }
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    ISkideExternalConnectionsService exConServ = tsContext().get( ISkideExternalConnectionsService.class );
    IdChain exConnId = exConServ.openConnection( ccId, tsContext(), loginInfo );
    ISkConnectionSupplier connSupp = tsContext().get( ISkConnectionSupplier.class );
    ISkConnection skConn = connSupp.getConn( exConnId );
    REFDEF_IN_OPEN_SK_CONN.setRef( aInput, skConn );
  }

  @Override
  protected void doAfterRunTask( ITsContextRo aInput ) {
    ISkConnection skConn = REFDEF_IN_OPEN_SK_CONN.getRef( aInput );
    skConn.close();
  }

  @Override
  protected ValidationResult doCanRun( ITsContextRo aInput ) {
    IConnectionConfigService ccService = tsContext().get( IConnectionConfigService.class );
    String ccId = OPDEF_IN_EXCONN_ID.getValue( aInput.params() ).asString();
    if( !ccService.listConfigs().hasKey( ccId ) ) {
      return ValidationResult.error( FMT_ERR_NO_SUCH_CONN_CONF_ID, ccId );
    }
    return ValidationResult.SUCCESS;
  }

}
