package org.toxsoft.skide.core.api.impl;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.gui.m5.*;

/**
 * SkIDE environment initialization.
 * <p>
 * Creates {@link ISkideEnvironment} instance and places it in the application level context. Initializes registered
 * plugins with the window level context, closes plugins when application quits.
 *
 * @author hazard157
 */
public class QuantSkide030Environment
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantSkide030Environment() {
    super( QuantSkide030Environment.class.getSimpleName() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQuant
  //

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    ISkideEnvironment skEnv = new SkideEnvironment( aAppContext );
    aAppContext.set( ISkideEnvironment.class, skEnv );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // Initialize plugins
    ITsGuiContext ctx = new TsGuiContext( aWinContext );
    ITsWorkroom wr = aWinContext.get( ITsWorkroom.class );
    SkideEnvironment skEnv = (SkideEnvironment)aWinContext.get( ISkideEnvironment.class );
    skEnv.papiInitialize( ctx, skEnv, wr );
    // M5
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new SkideUnitM5Model() );
  }

  @Override
  protected void doCloseWin( MWindow aWindow ) {
    SkideEnvironment skEnv = (SkideEnvironment)aWindow.getContext().get( ISkideEnvironment.class );
    skEnv.close();
  }

}
