package org.toxsoft.tool.sfv.gui.e4.handlers;

import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.tool.sfv.gui.e4.services.*;

/**
 * "New" command.
 *
 * @author hazard157
 */
public class CmdToolSfvNewFile {

  @Execute
  void exec( ISfvToolService aSts ) {
    aSts.bound().clear();
  }

}
