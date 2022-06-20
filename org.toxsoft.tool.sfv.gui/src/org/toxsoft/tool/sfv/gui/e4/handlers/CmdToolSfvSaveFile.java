package org.toxsoft.tool.sfv.gui.e4.handlers;

import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.tool.sfv.gui.e4.services.*;

/**
 * "Save" command.
 *
 * @author hazard157
 */
public class CmdToolSfvSaveFile {

  @Execute
  void exec( ISfvToolService aSts ) {
    if( aSts.bound().file() != null ) {
      aSts.bound().save();
    }
  }

  @CanExecute
  boolean canExec( ISfvToolService aSts ) {
    return aSts.bound().file() != null && aSts.bound().isAltered();
  }

}
