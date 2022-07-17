package org.toxsoft.skide.core.incub.rrs;

import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * An {@link IRecentResourceStorage} implementation using {@link IAppPreferences} as storage backend.
 *
 * @author hazard157
 */
public class RecentResourceStorageInAppPrefs
    extends AbstractRecentResourceStorage {

  private static final String PB_ID_START         = TS_ID + ".rrs."; //$NON-NLS-1$
  private static final String ITEM_PREF_ID_PREFIX = "Recent";        //$NON-NLS-1$

  private final IPrefBundle prefBundle;

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} values
   * @param aAppPrefs {@link IAppPreferences} - application prefeences to store recent resources
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public RecentResourceStorageInAppPrefs( String aId, IOptionSet aParams, IAppPreferences aAppPrefs ) {
    super( aId, aParams );
    TsNullArgumentRtException.checkNull( aAppPrefs );
    String pbId = makeBundleIdFromRrsId( aId );
    prefBundle = aAppPrefs.getBundle( pbId );
    internalLoadItemsMap( prefBundle, itemsMap() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static String makeBundleIdFromRrsId( String aRrsId ) {
    return PB_ID_START + aRrsId;
  }

  private static void internalLoadItemsMap( IPrefBundle aPrefBundle, IStringMapEdit<IOptionSet> aItemsMap ) {
    aItemsMap.clear();
    for( int i = 0; i < MAX_COUNT_RANGE.maxValue(); i++ ) {
      String prefId = ITEM_PREF_ID_PREFIX + (i + 1);
      if( aPrefBundle.prefs().hasKey( prefId ) ) {
        String prefValue = aPrefBundle.prefs().getStr( prefId );
        Pair<String, IOptionSet> p = internalExtractPrefValue( prefValue );
        if( p != null ) {
          aItemsMap.put( p.left(), p.right() );
        }
      }
    }
  }

  private static String internalMakePrefValue( String aResourceIdString, IOptionSet aResourceOptions ) {
    StringBuilder sb = new StringBuilder();
    IStrioWriter sw = new StrioWriter( new CharOutputStreamAppendable( sb ) );
    sw.writeQuotedString( aResourceIdString );
    sw.writeSeparatorChar();
    sw.writeSpace();
    OptionSetKeeper.KEEPER.write( sw, aResourceOptions );
    return sb.toString();
  }

  private static Pair<String, IOptionSet> internalExtractPrefValue( String aPreferenceValue ) {
    try {
      IStrioReader sr = new StrioReader( new CharInputStreamString( aPreferenceValue ) );
      String resourceIdString = sr.readQuotedString();
      sr.ensureSeparatorChar();
      IOptionSet resourceOptions = OptionSetKeeper.KEEPER.read( sr );
      return new Pair<>( resourceIdString, resourceOptions );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      return null;
    }
  }

  private static void internalSaveItemsMap( IPrefBundle aPrefBundle, IStringMap<IOptionSet> aItemsMap ) {
    IOptionSetEdit prefs = new OptionSet();
    for( int i = 0; i < aItemsMap.size(); i++ ) {
      String resourceIdString = aItemsMap.keys().get( i );
      IOptionSet resourceOptions = aItemsMap.values().get( i );
      String prefValue = internalMakePrefValue( resourceIdString, resourceOptions );
      String prefId = ITEM_PREF_ID_PREFIX + (i + 1);
      prefs.setStr( prefId, prefValue );
    }
    aPrefBundle.prefs().setAll( prefs );
  }

  // ------------------------------------------------------------------------------------
  // AbstractRecentResourceStorage
  //

  @Override
  protected void doSaveItemsMap() {
    internalSaveItemsMap( prefBundle, itemsMap() );
  }

}
