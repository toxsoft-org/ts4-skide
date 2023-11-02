package org.toxsoft.skide.core.api.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.ctx.impl.TsContextRefDef.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tsgui.mws.services.timers.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * Sk-Service: calls {@link IDevCoreApi#doJobInCoreMainThread()} from the {@link ITsGuiTimersService} quick timer.
 *
 * @author mvk
 */
public final class SkDoJobCallerService
    extends AbstractSkService {

  /**
   * Service creator singleton.
   */
  public static final ISkServiceCreator<AbstractSkService> CREATOR = SkDoJobCallerService::new;

  /**
   * The service ID.
   */
  public static final String SERVICE_ID = SK_SYSEXT_SERVICE_ID_PREFIX + ".DoJobCallerService"; //$NON-NLS-1$

  /**
   * Mandotary context parameter: {@link ITsGuiTimersService}
   */
  public static final ITsContextRefDef<ITsGuiTimersService> REF_TSGUI_TIMER_SERVICE =
      create( SERVICE_ID + ".Display", ITsGuiTimersService.class, //$NON-NLS-1$
          TSID_NAME, "TImer service", // N_DISPLAY,
          TSID_DESCRIPTION, "The TsGUI library times service to use quick times for CoreApi doJob()", // D_DISPLAY,
          TSID_IS_MANDATORY, AV_TRUE, //
          TSID_IS_NULL_ALLOWED, AV_FALSE //
      );

  /**
   * Constructor.
   *
   * @param aCoreApi {@link IDevCoreApi} - owner core API implementation
   */
  SkDoJobCallerService( IDevCoreApi aCoreApi ) {
    super( SERVICE_ID, aCoreApi );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkService
  //
  @Override
  protected void doInit( ITsContextRo aArgs ) {
    ITsGuiTimersService timersService = REF_TSGUI_TIMER_SERVICE.getRef( aArgs );
    timersService.quickTimers().addListener( aRtTime -> coreApi().doJobInCoreMainThread() );
  }

  @Override
  protected void doClose() {
    // nop
  }

}
