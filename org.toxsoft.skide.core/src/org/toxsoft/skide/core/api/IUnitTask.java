package org.toxsoft.skide.core.api;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.gentask.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

public interface IUnitTask
    extends IGenericTask {

  IStridablesList<IDataDef> opDefs();

  IOptionSetEdit opValues();

}
