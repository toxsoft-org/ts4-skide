package org.toxsoft.skide.plugin.sded.tasks.upload;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.skide.core.api.tasks.*;
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

  /**
   * Saves rules configuration values to the options set.
   * <p>
   * Common usage is to prepare option set to be saved to the storage
   * {@link AbstractSkideUnitTask#setCfgOptionValues(IOptionSet)}.
   *
   * @param aOps {@link IOptionSetEdit} - the editable option set
   */
  public void saveToOptions( IOptionSetEdit aOps ) {
    // TODO UserDefinedUploadClassesFilter.saveToOptions()
  }

  /**
   * Loads rules configuration values from the options set.
   * <p>
   * Common usage is to load values task the input arguments {@link ITsContext#params()}.
   *
   * @param aOps {@link IOptionSet} - the option set
   */
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
