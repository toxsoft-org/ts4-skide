package org.toxsoft.skide.core.incub.rrs;

import java.io.*;
import java.net.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Recently used resource storage (RRS).
 * <p>
 * Designed to store last open files, projects, connection etc between application starts.
 * <p>
 * RRS identifier {@link #id()} used to store different RRS in the samge storage like {@link IAppPreferences}. Visuals
 * like {@link #nmName()} or {@link #iconId()} are used by helper GUI components. RRS {@link #params()} also contains
 * some tunung options like {@link IRecentResourceStorageConstants#OPDEF_MAX_STORED_RESOURCES}. Parameter options must
 * be specified at RRS creation.
 * <p>
 * Each stored resource is represented as pair "resource ID string" - "resource options". <br>
 * Resource <b>ID string</b> may be any unique identifier depending on resource kind. For example, for files it may be
 * {@link File#getAbsolutePath()}, for JDBC connections - {@link URI#toString()}, for internet sites -
 * {@link URL#toString()} and so on. <br>
 * Resource options may contain additional informations like visuals {@link IAvMetaConstants#TSID_NAME} or
 * resource-specific option. For example, for USkat connections connection arguments may be stored.
 * <p>
 * Generates {@link IGenericChangeListener#onGenericChangeEvent(Object)} on every change of {@link #itemsMap()}.
 * <p>
 * How and when RRS are stored depends on implementation.
 *
 * @author hazard157
 */
public interface IRecentResourceStorage
    extends IStridableParameterized, ITsClearable, IGenericChangeEventCapable {

  /**
   * Returns recent resources stored by this instance.
   * <p>
   * Returned map values are ordered by ascending adding time.
   *
   * @return {@link IStringMap}&lt;{@link IOptionSet}&gt; - map "resource ID string" - "resource options"
   */
  IStringMap<IOptionSet> itemsMap();

  /**
   * Adds recent used resource item to storage.
   * <p>
   * If {@link #itemsMap()} already contains resource with the same ID, previous item will be removed and new one will
   * be added at the {@link IStringMap#values()} end.
   *
   * @param aIdString String - resource unique ID string
   * @param aOptions {@link IOptionSet} - the reosurce options
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID string is a blank string
   */
  void addResourceItem( String aIdString, IOptionSet aOptions );

}
