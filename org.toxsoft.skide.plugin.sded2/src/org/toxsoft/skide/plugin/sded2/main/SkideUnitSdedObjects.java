package org.toxsoft.skide.plugin.sded2.main;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.api.ISkidePluginHardConstants.*;
import static org.toxsoft.skide.core.api.ucateg.ISkideUnitCategoryConstants.*;
import static org.toxsoft.skide.plugin.sded2.l10n.ISkidePluginSdedSharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.plugin.sded2.*;
import org.toxsoft.skide.plugin.sded2.tasks.codegen.*;
import org.toxsoft.skide.plugin.sded2.tasks.upload.*;

/**
 * SkiDE unit: Object service objects editor.
 *
 * @author hazard157
 */
public class SkideUnitSdedObjects
    extends AbstractSkideUnit {

  /**
   * The plugin ID.
   */
  public static final String UNIT_ID = SKIDE_FULL_ID + ".unit.sded.objects"; //$NON-NLS-1$

  SkideUnitSdedObjects( ITsGuiContext aContext, AbstractSkidePlugin aCreator ) {
    super( UNIT_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_OBJECTS_EDITOR, //
        TSID_DESCRIPTION, STR_SKIDE_OBJECTS_EDITOR_D, //
        OPDEF_SKIDE_UNIT_CATEGORY, UCATEGID_SYSDESCR, //
        OPDEF_SKIDE_UNIT_SEQ_NO, avInt( 85 ), //
        TSID_ICON_ID, ISkidePluginSded2Constants.ICONID_OBJS_EDITOR //
    ), aContext, aCreator );
    unitActions().add( ACDEF_ABOUT );
  }

  @Override
  protected AbstractSkideUnitPanel doCreateUnitPanel( ITsGuiContext aContext ) {
    return new SkideUnitPanelObjects( aContext, this );
  }

  @Override
  protected void doFillTasks( IStringMapEdit<AbstractSkideUnitTask> aTaskRunnersMap ) {
    AbstractSkideUnitTask task = new TaskObjectsCodegen( this );
    aTaskRunnersMap.put( task.taskInfo().id(), task );
    task = new TaskObjectsUpload( this );
    aTaskRunnersMap.put( task.taskInfo().id(), task );
  }

}
