package org.toxsoft.skide.plugin.sded.tasks.upload;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Rules for selecting objects by the user for uploading to the server.
 * <p>
 * Rules are applied as a filter to the source objects list provided by
 * {@link ISkObjectService#listObjs(String, boolean)}.
 *
 * @author max
 */
public class ObjectsUploadSelectionRules
    implements ITsFilter<ISkObject> {

  /**
   * Saves rules configuration values to the options set.
   * <p>
   * Common usage is to prepare option set to be saved to the storage
   * {@link AbstractSkideUnitTask#setCfgOptionValues(IOptionSet)}.
   *
   * @param aOps {@link IOptionSetEdit} - the editable option set
   */
  public void saveToOptions( IOptionSetEdit aOps ) {
    // TODO UserDefinedUploadObjectsFilter.saveToOptions()
  }

  /**
   * Loads rules configuration values from the options set.
   * <p>
   * Common usage is to load values task the input arguments {@link ITsContext#params()}.
   *
   * @param aOps {@link IOptionSet} - the option set
   */
  public void loadFromOptions( IOptionSet aOps ) {
    // TODO UserDefinedUploadObjectsFilter.loadFromOptions()
  }

  @Override
  public boolean accept( ISkObject aObj ) {
    // TODO Auto-generated method stub
    return true;
  }

}
