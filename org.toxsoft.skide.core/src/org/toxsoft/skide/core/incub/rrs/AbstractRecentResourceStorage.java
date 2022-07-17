package org.toxsoft.skide.core.incub.rrs;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skide.core.incub.rrs.IRecentResourceStorageConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation of {@link IRecentResourceStorage}.
 * <p>
 * Subclass must save {@link #itemsMap()} in method {@link #doSaveItemsMap()}, and that is important it must load
 * {@link #itemsMap()} (visible as editable map) only once, in the constructor.
 *
 * @author hazard157
 */
public abstract class AbstractRecentResourceStorage
    extends StridableParameterized
    implements IRecentResourceStorage {

  /**
   * Value of maximal recent resources will be fit in this range.
   */
  public static final IntRange MAX_COUNT_RANGE = new IntRange( 4, 99 );

  private final GenericChangeEventer       eventer;
  private final IStringMapEdit<IOptionSet> itemsMap = new StringMap<>();

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public AbstractRecentResourceStorage( String aId, IOptionSet aParams ) {
    super( aId, aParams );
    eventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private int getMaxCount() {
    int maxCount = OPDEF_MAX_STORED_RESOURCES.getValue( params() ).asInt();
    return MAX_COUNT_RANGE.inRange( maxCount );
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return params().getStr( TSID_ICON_ID, null );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    if( !itemsMap.isEmpty() ) {
      itemsMap.clear();
      eventer.fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IRecentResourceStorage
  //

  @Override
  public IStringMapEdit<IOptionSet> itemsMap() {
    return itemsMap;
  }

  @Override
  public void addResourceItem( String aIdString, IOptionSet aOptions ) {
    TsErrorUtils.checkNonBlank( aIdString );
    TsNullArgumentRtException.checkNull( aOptions );
    // add item at the end
    itemsMap.removeByKey( aIdString );
    itemsMap.put( aIdString, aOptions );
    // reduce items quantity if needed
    int maxCount = getMaxCount();
    while( itemsMap.size() >= maxCount ) {
      itemsMap.removeByKey( itemsMap.keys().first() );
    }
    // fire event
    eventer.fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass must save {@link #itemsMap()} in the permanent storage.
   */
  protected abstract void doSaveItemsMap();

}
