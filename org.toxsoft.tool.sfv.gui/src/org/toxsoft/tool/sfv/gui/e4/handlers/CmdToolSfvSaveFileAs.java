package org.toxsoft.tool.sfv.gui.e4.handlers;

import static org.toxsoft.tool.sfv.gui.IToolSfvGuiConstants.*;

import java.io.*;

import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.tool.sfv.gui.e4.services.*;

/**
 * "Save as" command.
 *
 * @author hazard157
 */
public class CmdToolSfvSaveFileAs {

  @Execute
  void exec( ISfvToolService aSts, Shell aShell, IAppPreferences aAppPrefs ) {
    IPrefBundle pb = aAppPrefs.findBundle( PBID_SFV_TOOL );
    String lastPath = pb.prefs().getStr( APPRM_LAST_PATH.id(), TsLibUtils.EMPTY_STRING );
    File f = TsRcpDialogUtils.askFileSave( aShell, lastPath );
    if( f != null ) {
      aSts.bound().saveAs( f );
      pb.prefs().setStr( APPRM_LAST_PATH, lastPath );
    }
  }

}
