package org.toxsoft.skide.core.project;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * The SkIDE project main and the only access point.
 *
 * @author hazard157
 */
public interface ISkideProject {

  /**
   * Returns the information about project.
   *
   * @return {@link IOptionSet} - the project info
   */
  IOptionSet projectInfo();

  /**
   * Changes project info? any option except project ID.
   *
   * @param aParams {@link IOptionSet} - options to be changed
   */
  void setProjectInfo( IOptionSet aParams );

  /**
   * Return connection to the project content.
   *
   * @return {@link ISkConnection} - project content
   */
  ISkConnection projConn();

  /**
   * Returns project units.
   *
   * @return {@link IStridablesList}&lt;{@link ISkideProjectUnit}&gt; - units list
   */
  IStridablesList<ISkideProjectUnit> units();

  /**
   * Returns the project unit of expected kind by specified ID.
   *
   * @param <U> - expected type of unit
   * @param aUnitId String - unit ID
   * @param aUnitClass &lt;U&gt; - expected type of unit
   * @return &lt;U&gt; - found unit or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws ClassCastException found unit is not of expected type
   */
  <U extends ISkideProjectUnit> U getUnit( String aUnitId, Class<U> aUnitClass );

}
