package org.toxsoft.skide.core.api.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.core.txtproj.lib.workroom.*;
import org.toxsoft.skide.core.api.*;

/**
 * {@link IPluginEnvironment} implementation.
 *
 * @author hazard157
 */
class PluginEnvironment
    implements IPluginEnvironment {

  private static final String UNIT_STORAGE_FILE_EXT = ".unit_storage"; //$NON-NLS-1$

  private final ITsWorkroomStorage            wrStorage;
  private final IStringMap<IKeepablesStorage> ksMap = new StringMap<>();

  public PluginEnvironment( ITsWorkroomStorage aWrStorage ) {
    wrStorage = aWrStorage;
  }

  // ------------------------------------------------------------------------------------
  // IPluginEnvironment
  //

  @Override
  public ITsWorkroomStorage wrStorage() {
    return wrStorage;
  }

  @Override
  public IKeepablesStorage unitStorage( String aUnitId ) {
    StridUtils.checkValidIdPath( aUnitId );
    IKeepablesStorage ks = ksMap.findByKey( aUnitId );
    if( ks == null ) {
      File f = new File( wrStorage.rootDir(), aUnitId + UNIT_STORAGE_FILE_EXT );
      ks = new KeepablesStorageInFile( f );
    }
    return ks;
  }

}
