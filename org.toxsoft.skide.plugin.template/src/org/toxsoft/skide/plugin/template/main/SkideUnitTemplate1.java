package org.toxsoft.skide.plugin.template.main;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.plugin.template.ISkidePluginTemplateSharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * SkiDE unit: unit template 1.
 *
 * @author hazard157
 */
public class SkideUnitTemplate1
    extends AbstractSkideUnit {

  /**
   * The plugin ID.
   */
  public static final String UNIT_ID = SKIDE_FULL_ID + ".unit.template_1"; //$NON-NLS-1$

  SkideUnitTemplate1( ITsGuiContext aContext, AbstractSkidePlugin aCreator ) {
    super( UNIT_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_TEMPLATE_UNIT_1, //
        TSID_DESCRIPTION, STR_SKIDE_TEMPLATE_UNIT_1_D, //
        TSID_ICON_ID, ICONID_ARROW_RIGHT //
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
    return new SkideUnit1Panel( aContext, this );
  }

}
