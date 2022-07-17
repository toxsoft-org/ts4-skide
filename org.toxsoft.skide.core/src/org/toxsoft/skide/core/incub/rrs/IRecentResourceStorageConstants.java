package org.toxsoft.skide.core.incub.rrs;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Constants of {@link IRecentResourceStorage} configuration.
 *
 * @author hazard157
 */
public interface IRecentResourceStorageConstants {

  /**
   * {@link IRecentResourceStorage#params()} option: max number of stored recent resources.
   */
  IDataDef OPDEF_MAX_STORED_RESOURCES = DataDef.create( "MaxRecentResourcesCount", INTEGER, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, avInt( 10 ) //
  );

}
