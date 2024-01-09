package org.toxsoft.skide.task.codegen.main;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.task.codegen.ISkideTaskCodegenConstants.*;
import static org.toxsoft.skide.task.codegen.ISkideTaskCodegenSharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * SkiDE unit: view and edit SkIDE project properties.
 *
 * @author hazard157
 */
public class SkideUnitTaskCodegenConfig
    extends AbstractSkideUnit {

  /**
   * The unit ID.
   */
  public static final String UNIT_ID = SKIDE_FULL_ID + ".unit.task_codegen_config"; //$NON-NLS-1$

  SkideUnitTaskCodegenConfig( ITsGuiContext aContext, AbstractSkidePlugin aCreator ) {
    super( UNIT_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_TASK_CODEGEN, //
        TSID_DESCRIPTION, STR_TASK_CODEGEN_D, //
        TSID_ICON_ID, ICONID_TASK_CODEGEN //
    ), aContext, aCreator );
    unitActions().add( ACDEF_ABOUT );
  }

  @Override
  protected AbstractSkideUnitPanel doCreateUnitPanel( ITsGuiContext aContext ) {
    return new SkideUnitTaskCodegenConfigPanel( aContext, this );
  }

}
