package org.toxsoft.tool.sfv.gui.e4.main;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * SFV tool helper methods.
 *
 * @author hazard157
 */
public class SfvToolUtils {

  /**
   * Reads all sections from the file.
   *
   * @param aFile {@link File} - the file to read
   * @return {@link IList}&lt;{@link ISfvSection}&gt; - read sections
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error reading file
   * @throws StrioRtException invalid file format
   */
  public static IList<ISfvSection> openFile( File aFile ) {
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( aFile ) ) {
      IStrioReader sr = new StrioReader( chIn );
      TsNullArgumentRtException.checkNull( sr );
      IListEdit<ISfvSection> sects = new ElemLinkedBundleList<>();
      while( sr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) != CHAR_EOF ) {
        String sectId = sr.readIdPath();
        sr.ensureChar( CHAR_EQUAL );
        String content = StrioUtils.readInterbaceContent( sr );
        ISfvSection ss = new SfvSection( sectId, content );
        sects.add( ss );
      }
      return sects;
    }
  }

  /**
   * Writes sections to the file (existing file will be ovwerwritten).
   *
   * @param aFile {@link File} - file to write
   * @param aSections {@link IList}&lt;{@link ISfvSection}&gt; - sections to write
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error writing file
   */
  public static void saveFile( File aFile, IList<ISfvSection> aSections ) {
    TsNullArgumentRtException.checkNull( aSections );
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( aFile ) ) {
      IStrioWriter sw = new StrioWriter( chOut );
      IStringMapEdit<String> smap = new StringMap<>();
      for( ISfvSection ss : aSections ) {
        smap.put( ss.sectionId(), ss.sectionContent() );
      }
      StrioUtils.writeSections( sw, smap );
    }
  }

  /**
   * No subclassing.
   */
  private SfvToolUtils() {
    // nop
  }

}
