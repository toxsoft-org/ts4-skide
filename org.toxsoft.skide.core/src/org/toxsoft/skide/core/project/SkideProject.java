package org.toxsoft.skide.core.project;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * {@link ISkideProject} implementation.
 *
 * @author hazard157
 */
class SkideProject
    implements ISkideProject {

  private final IOptionSetEdit projectInfo = new OptionSet();
  private final ITsProject     tsPorj;

  /**
   * Creates project bound to the {@link ITsProject} storage.
   *
   * @param aTsProj {@link ITsProject} - TS-project used as SkIDE project storage
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkideProject( ITsProject aTsProj ) {
    TsNullArgumentRtException.checkNulls( aTsProj );
    tsPorj = aTsProj;
  }

  // ------------------------------------------------------------------------------------
  // ISkideProject
  //

  @Override
  public IOptionSet projectInfo() {
    return projectInfo;
  }

  @Override
  public void setProjectInfo( IOptionSet aParams ) {
    projectInfo.setAll( aParams );
  }

  @Override
  public ISkConnection projConn() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IStridablesList<ISkideProjectUnit> units() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <U extends ISkideProjectUnit> U getUnit( String aUnitId, Class<U> aUnitClass ) {
    // TODO Auto-generated method stub
    return null;
  }

}
