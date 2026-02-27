package org.toxsoft.skide.core.api;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Hard-coded constants used for SkIDE plugins and units parameters.
 *
 * @author hazard157
 */
public interface ISkidePluginHardConstants {

  /**
   * ID of {@link #OPDEF_SKIDE_UNIT_SEQ_NO}.
   */
  String OPID_SKIDE_UNIT_SEQ_NO = SKIDE_FULL_ID + ".PluginSeqNo"; //$NON-NLS-1$

  /**
   * Unit option: number defines position of the units in list - higher number is lower position.
   */
  IDataDef OPDEF_SKIDE_UNIT_SEQ_NO = DataDef.create( OPID_SKIDE_UNIT_SEQ_NO, EAtomicType.INTEGER, //
      TSID_DEFAULT_VALUE, avInt( 1000 ) //
  );

}
