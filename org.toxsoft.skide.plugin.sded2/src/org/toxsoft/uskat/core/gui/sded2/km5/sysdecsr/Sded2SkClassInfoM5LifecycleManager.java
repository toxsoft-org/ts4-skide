package org.toxsoft.uskat.core.gui.sded2.km5.sysdecsr;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.uskat.core.gui.km5.sded.IKM5SdedConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.impl.dto.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * LM for {@link Sded2SkClassInfoM5Model}.
 * <p>
 * Allows to enumerate classes, create and class without properties such as attributes, links, etc.
 *
 * @author hazard157
 */
class Sded2SkClassInfoM5LifecycleManager
    extends M5LifecycleManager<ISkClassInfo, ISkConnection>
    implements ISkConnected {

  public Sded2SkClassInfoM5LifecycleManager( IM5Model<ISkClassInfo> aModel, ISkConnection aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static IDtoClassInfo makeDtoClassInfo( IM5Bunch<ISkClassInfo> aValues ) {
    String id = aValues.getAsAv( FID_CLASS_ID ).asString();
    String parentId = aValues.getAs( FID_PARENT_ID, String.class );
    IOptionSetEdit params = new OptionSet();
    if( aValues.originalEntity() != null ) {
      params.setAll( aValues.originalEntity().params() );
    }
    params.setStr( FID_NAME, aValues.getAsAv( FID_NAME ).asString() );
    params.setStr( FID_DESCRIPTION, aValues.getAsAv( FID_DESCRIPTION ).asString() );
    DtoClassInfo cinf = new DtoClassInfo( id, parentId, params );
    // only when editing, class child properties are copied from original entity
    if( aValues.originalEntity() != null ) {
      cinf.attrInfos().addAll( aValues.originalEntity().attrs().list() );
      cinf.rtdataInfos().addAll( aValues.originalEntity().rtdata().list() );
      cinf.cmdInfos().addAll( aValues.originalEntity().cmds().list() );
      cinf.eventInfos().addAll( aValues.originalEntity().events().list() );
      cinf.linkInfos().addAll( aValues.originalEntity().links().list() );
      cinf.cmdInfos().addAll( aValues.originalEntity().cmds().list() );
      cinf.clobInfos().addAll( aValues.originalEntity().clobs().list() );
    }
    return cinf;
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<ISkClassInfo> aValues ) {
    IDtoClassInfo dtoClassInfo = makeDtoClassInfo( aValues );
    return skSysdescr().svs().validator().canCreateClass( dtoClassInfo );
  }

  @Override
  protected ISkClassInfo doCreate( IM5Bunch<ISkClassInfo> aValues ) {
    IDtoClassInfo dtoClassInfo = makeDtoClassInfo( aValues );
    return skSysdescr().defineClass( dtoClassInfo );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ISkClassInfo> aValues ) {
    IDtoClassInfo dtoClassInfo = makeDtoClassInfo( aValues );
    return skSysdescr().svs().validator().canEditClass( dtoClassInfo, aValues.originalEntity() );
  }

  @Override
  protected ISkClassInfo doEdit( IM5Bunch<ISkClassInfo> aValues ) {
    IDtoClassInfo dtoClassInfo = makeDtoClassInfo( aValues );
    return skSysdescr().defineClass( dtoClassInfo );
  }

  @Override
  protected ValidationResult doBeforeRemove( ISkClassInfo aEntity ) {
    return skSysdescr().svs().validator().canRemoveClass( aEntity.id() );
  }

  @Override
  protected void doRemove( ISkClassInfo aEntity ) {
    skSysdescr().removeClass( aEntity.id() );
  }

  @Override
  protected IList<ISkClassInfo> doListEntities() {
    return master().coreApi().sysdescr().listClasses();
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return master();
  }

}
