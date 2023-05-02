package org.toxsoft.skide.pligin.sded.main;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.pligin.sded.ISkidePluginSdedSharedResources.*;
import static org.toxsoft.uskat.core.gui.ISkCoreGuiConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * SkiDE unit: USkat users and roles management.
 *
 * @author hazard157
 */
public class SkideUnitClasses
    extends AbstractSkideUnit {

  /**
   * The plugin ID.
   */
  public static final String UNIT_ID = SKIDE_FULL_ID + ".unit.classes"; //$NON-NLS-1$

  SkideUnitClasses( ITsGuiContext aContext, AbstractSkidePlugin aCreator ) {
    super( UNIT_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_CLASSES_EDITOR, //
        TSID_DESCRIPTION, STR_SKIDE_CLASSES_EDITOR_D, //
        TSID_ICON_ID, ICONID_SDED_CLASSES_LIST //
    ), aContext, aCreator );
    unitActions().add( ACDEF_ABOUT );
  }

  @Override
  protected void doHandleAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_ABOUT: {
        // TODO display complete info about unit
        TsDialogUtils.info( getShell(), id() + '\n' + nmName() + '\n' + description() );
        break;
      }
      default: {
        // TODO display info about known but unhandled action
        TsDialogUtils.info( getShell(), aActionId );
      }
    }
  }

  @Override
  protected AbstractSkideUnitPanel doCreateUnitPanel( ITsGuiContext aContext ) {
    return new SkideUnitPanelClasses( aContext, this );
  }

}
