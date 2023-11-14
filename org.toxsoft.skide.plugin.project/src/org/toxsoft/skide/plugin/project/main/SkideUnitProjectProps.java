package org.toxsoft.skide.plugin.project.main;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.plugin.project.ISkidePluginProjectSharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * SkiDE unit: view and edit SkIDE project properties.
 * <p>
 * SkIDE project properties {@link ISkideProjectProperties} are stores in SkIDE environment
 * {@link ISkideEnvironment#projectProperties()}. This unit adds GUI to view and edit properties and to manage SkIDE
 * windows title text.
 *
 * @author hazard157
 */
public class SkideUnitProjectProps
    extends AbstractSkideUnit {

  /**
   * The plugin ID.
   */
  public static final String UNIT_ID = SKIDE_FULL_ID + ".unit.project_1"; //$NON-NLS-1$

  SkideUnitProjectProps( ITsGuiContext aContext, AbstractSkidePlugin aCreator ) {
    super( UNIT_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_PROJECT_PROPS, //
        TSID_DESCRIPTION, STR_SKIDE_PROJECT_PROPS_D, //
        TSID_ICON_ID, ICONID_ARROW_RIGHT //
    ), aContext, aCreator );
    unitActions().add( ACDEF_ABOUT );

    /**
     * TODO add handler to change windows title<br>
     * TODO invoke dialog at SkIDE startup if project vital properties are not set or are set to defaults<br>
     * TODO add menu item in "SkIDE" menu with "Project properties"<br>
     * TODO <br>
     */
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
    return new SkideUnitProjectPropsPanel( aContext, this );
  }

}
