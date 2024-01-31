package org.toxsoft.skide.plugin.exconn.main;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.api.ucateg.ISkideUnitCategoryConstants.*;
import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnConstants.*;
import static org.toxsoft.skide.plugin.exconn.ISkidePluginExconnSharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.core.gui.tasks.*;

/**
 * SkiDE unit: view and edit SkIDE project properties.
 *
 * @author hazard157
 */
public class SkideUnitTaskUpload
    extends AbstractSkideUnit {

  /**
   * The unit ID.
   */
  public static final String UNIT_ID = SKIDE_FULL_ID + ".unit.task_upload_config"; //$NON-NLS-1$

  SkideUnitTaskUpload( ITsGuiContext aContext, AbstractSkidePlugin aCreator ) {
    super( UNIT_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_TASK_UPLOAD, //
        TSID_DESCRIPTION, STR_TASK_UPLOAD_D, //
        OPDEF_SKIDE_UNIT_CATEGORY, UCATEGID_DELPOYMENT, //
        TSID_ICON_ID, ICONID_TASK_UPLOAD //
    ), aContext, aCreator );
    unitActions().add( ACDEF_ABOUT );
  }

  @Override
  protected AbstractSkideUnitPanel doCreateUnitPanel( ITsGuiContext aContext ) {
    return new SkideUnitTaskPanel( aContext, this, UploadToServerTaskProcessor.TASK_ID );
  }

}
