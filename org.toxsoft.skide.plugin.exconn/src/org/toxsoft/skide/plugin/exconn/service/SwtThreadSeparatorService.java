package org.toxsoft.skide.plugin.exconn.service;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.create;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.ctx.impl.TsContextRefDef.create;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * Служба: разделение потоков.
 * <p>
 * Решает задачи разделения доступа к данным между потоками системы и SWT.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
public final class SwtThreadSeparatorService
    extends AbstractSkService {

  /**
   * Service creator singleton.
   */
  public static final ISkServiceCreator<AbstractSkService> CREATOR = SwtThreadSeparatorService::new;

  /**
   * The service ID.
   */
  public static final String SERVICE_ID = "SwtThreadSeparator"; //$NON-NLS-1$

  /**
   * Mandatory context parameter: Display
   * <p>
   * Тип: {@link Display}
   */
  public static final ITsContextRefDef<Display> REF_DISPLAY = create( SERVICE_ID + ".Display", Display.class, //$NON-NLS-1$
      TSID_NAME, "Display", // N_DISPLAY,
      TSID_DESCRIPTION, "Display", // D_DISPLAY,
      TSID_IS_NULL_ALLOWED, AV_FALSE );

  /**
   * Mandatory context parameter: dojob timeout (msec)
   * <p>
   * Тип: {@link EAtomicType#INTEGER}
   */
  public static final IDataDef OP_DOJOB_TIMEOUT = create( SERVICE_ID + ".DoJobTimeout", INTEGER, //$NON-NLS-1$
      TSID_NAME, "Timeout", // N_DOJOB_TIMEOUT, //
      TSID_DESCRIPTION, "Dojob timeout (msec)", // D_DOJOB_TIMEOUT, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, AvUtils.avInt( 100 ) );

  private final IDevCoreApi devCoreApi;
  private Display           display;
  private int               doJobTimeout;

  /**
   * Constructor.
   *
   * @param aCoreApi {@link IDevCoreApi} - owner core API implementation
   */
  SwtThreadSeparatorService( IDevCoreApi aCoreApi ) {
    super( SERVICE_ID, aCoreApi );
    devCoreApi = aCoreApi;
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkService
  //
  @Override
  protected void doInit( ITsContextRo aArgs ) {
    display = REF_DISPLAY.getRef( aArgs );
    doJobTimeout = OP_DOJOB_TIMEOUT.getValue( aArgs.params() ).asInt();
    // Запуск задачи разделения потоков
    display.asyncExec( new Runnable() {

      @Override
      public void run() {
        devCoreApi.doJobInCoreMainThread();
        display.timerExec( doJobTimeout, this );
      }
    } );
  }

  @Override
  protected void doClose() {
    // nop
  }

}
