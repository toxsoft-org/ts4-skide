package org.toxsoft.skide.core.project;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * SkIDE project management utilisy methods.
 *
 * @author hazard157
 */
public class SkideProjectUtils {

  /**
   * Creates an empty project.
   *
   * @param aFile {@link File} - the file used to store SkIDE project
   * @return {@link ISkideProject} - empty SkIDE project with
   */
  public static ISkideProject createAsTextFile( File aFile ) {

    // TODO реализовать SkideProjectUtils.createAsTextFile()
    throw new TsUnderDevelopmentRtException( "SkideProjectUtils.createAsTextFile()" );
  }

  /**
   * No subclassing.
   */
  private SkideProjectUtils() {
    // nop
  }

}
