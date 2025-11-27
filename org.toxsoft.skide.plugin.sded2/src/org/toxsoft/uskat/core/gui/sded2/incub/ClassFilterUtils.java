package org.toxsoft.uskat.core.gui.sded2.incub;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Helper methods to access Sk-class filters subsystem.
 *
 * @author hazard157
 */
public class ClassFilterUtils {

  public static final IClassFilterParamsBuilder createClassFilterParamsBuilder() {
    // TODO implement ClassFilterUtils.createClassFilterParamsBuilder()
    throw new TsUnderDevelopmentRtException( "ClassFilterUtils.createClassFilterParamsBuilder()" );
  }

  public static ITsFilter<ISkClassInfo> createClassInfoFilter() {
    // TODO implement ClassFilterUtils.createClassInfoFilter()
    throw new TsUnderDevelopmentRtException( "ClassFilterUtils.createClassInfoFilter()" );
  }

  public static ITsFilter<IDtoClassInfo> createDtoClassFilter() {
    // TODO implement ClassFilterUtils.createDtoClassFilter()
    throw new TsUnderDevelopmentRtException( "ClassFilterUtils.createDtoClassFilter()" );
  }

  /**
   * No subclasses.
   */
  private ClassFilterUtils() {
    // nop
  }

}
