package org.toxsoft.skide.core.e4.handlers;

import static org.toxsoft.skide.core.e4.handlers.ISkResources.*;
import static org.toxsoft.skide.core.proj.ISkideProjectConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Command: edit SkIDE project properties.
 *
 * @author hazard157
 */
public class CmdSkideEditProjectProps {

  @Execute
  void exec( ITsProject aProject, IEclipseContext aEclipseContext ) {
    ITsGuiContext ctx = new TsGuiContext( aEclipseContext );
    ITsDialogInfo tdi = new TsDialogInfo( ctx, DLG_C_EDIT_PROJ_PROPS, DLG_T_EDIT_PROJ_PROPS );
    IOptionSet p = DialogOptionsEdit.editOpset( tdi, ALL_SKIDE_PROJECT_PROPERIY_OPDEFS, aProject.params() );
    if( p != null ) {
      aProject.params().addAll( p );
    }
  }

}
