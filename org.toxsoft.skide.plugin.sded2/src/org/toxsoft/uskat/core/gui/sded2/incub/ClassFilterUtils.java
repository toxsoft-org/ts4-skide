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

  /**
   * Creates {@link ITsFilter} instance to filter out {@link ISkClassInfo} entities.
   * <p>
   * Created filter throws an exception on <code>null</code> input.
   *
   * @param aFilterParams {@link ITsCombiFilterParams} - the filter parameters
   * @return {@link ITsFilter}&lt;{@link ISkClassInfo}&gt; - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument contains not applicable parameters
   */
  public static ITsFilter<ISkClassInfo> createClassInfoFilter( ITsCombiFilterParams aFilterParams ) {
    TsNullArgumentRtException.checkNull( aFilterParams );

    // TODO implement ClassFilterUtils.createClassInfoFilter()
    throw new TsUnderDevelopmentRtException( "ClassFilterUtils.createClassInfoFilter()" );
  }

  /**
   * Creates {@link ITsFilter} instance to filter out {@link IDtoClassInfo} entities.
   * <p>
   * Created filter throws an exception on <code>null</code> input.
   *
   * @param aFilterParams {@link ITsCombiFilterParams} - the filter parameters
   * @return {@link ITsFilter}&lt;{@link IDtoClassInfo}&gt; - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument contains not applicable parameters
   */
  public static ITsFilter<IDtoClassInfo> createDtoClassFilter( ITsCombiFilterParams aFilterParams ) {
    TsNullArgumentRtException.checkNull( aFilterParams );

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
