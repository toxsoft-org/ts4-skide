package org.toxsoft.uskat.core.gui.sded2.km5.skobj;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.impl.dto.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * LM class for this model.
 * <p>
 *
 * @author dima
 */
public class Sded2SkObjectM5LifecycleManager
    extends M5LifecycleManager<ISkObject, ISkConnection>
    implements ISkConnected {

  private String classId;

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - the model
   * @param aMaster &lt;{@link ISkConnection}&gt; - master object, may be <code>null</code>
   * @param aClassId String - ID of class to enumerate by {@link #itemsProvider()}
   * @throws TsNullArgumentRtException model is <code>null</code>
   */
  public Sded2SkObjectM5LifecycleManager( IM5Model<ISkObject> aModel, ISkConnection aMaster, String aClassId ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
    StridUtils.checkValidIdPath( aClassId );
    classId = aClassId;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IDtoObject makeDtoObject( IM5Bunch<ISkObject> aValues ) {
    String id = aValues.getAsAv( ISkHardConstants.AID_STRID ).asString();
    Skid skid = new Skid( classId, id );
    DtoObject dtoObject = DtoObject.createDtoObject( skid, coreApi() );
    dtoObject.attrs().setValue( ISkHardConstants.AID_NAME, aValues.getAsAv( ISkHardConstants.AID_NAME ) );
    dtoObject.attrs().setValue( ISkHardConstants.AID_DESCRIPTION, aValues.getAsAv( ISkHardConstants.AID_DESCRIPTION ) );
    return dtoObject;

  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return master();
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ISkObject doCreate( IM5Bunch<ISkObject> aValues ) {
    IDtoObject dtoObject = makeDtoObject( aValues );
    return skObjServ().defineObject( dtoObject );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ISkObject> aValues ) {
    IDtoObject dtoObject = makeDtoObject( aValues );
    return skObjServ().svs().validator().canEditObject( dtoObject, aValues.originalEntity() );
  }

  @Override
  protected ISkObject doEdit( IM5Bunch<ISkObject> aValues ) {
    IDtoObject dtoObject = makeDtoObject( aValues );
    return skObjServ().defineObject( dtoObject );
  }

  @Override
  protected ValidationResult doBeforeRemove( ISkObject aEntity ) {
    return skObjServ().svs().validator().canRemoveObject( aEntity.skid() );
  }

  @Override
  protected void doRemove( ISkObject aEntity ) {
    skObjServ().removeObject( aEntity.skid() );
  }

  @Override
  protected IList<ISkObject> doListEntities() {
    return skObjServ().listObjs( classId, false );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns class ID of objects to be listed.
   *
   * @return String - class ID
   */
  public String getClassId() {
    return classId;
  }

  /**
   * Sets class ID of objects to be listed.
   *
   * @param aClassId String - ID of class to enumerate by {@link #itemsProvider()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public void setClassId( String aClassId ) {
    StridUtils.checkValidIdPath( aClassId );
    classId = aClassId;
  }

}
