package org.toxsoft.skide.plugin.sded.tasks.upload;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

/**
 * Rules for selecting classes by the user for uploading to the server.
 * <p>
 * Rules are applied as a filter to the source classes list provided by {@link ISkSysdescr#listClasses()}.
 *
 * @author hazard157
 */
public class ClassesUploadSelectionRules
    implements ITsFilter<ISkClassInfo> {

  /**
   * TODO VALED to edit the rules
   */

  // public static final UserDefinedUploadClassesFilter ALL = new UserDefinedUploadClassesFilter();

  /**
   * Constructor.
   */
  public ClassesUploadSelectionRules() {
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void saveToOptions( IOptionSetEdit aOps ) {
    // TODO UserDefinedUploadClassesFilter.saveToOptions()
  }

  public void loadFromOptions( IOptionSet aOps ) {
    // TODO UserDefinedUploadClassesFilter.loadFromOptions()
  }

  // ------------------------------------------------------------------------------------
  // ITsFilter
  //

  @Override
  public boolean accept( ISkClassInfo aObj ) {
    // TODO Auto-generated method stub
    return true;
  }

}
