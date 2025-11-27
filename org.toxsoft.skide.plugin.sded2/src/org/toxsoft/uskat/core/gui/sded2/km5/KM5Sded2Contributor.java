package org.toxsoft.uskat.core.gui.sded2.km5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.gui.sded2.km5.skobj.*;
import org.toxsoft.uskat.core.gui.sded2.km5.sysdecsr.*;

/**
 * Contributes M5-models for templates entities.
 *
 * @author dima
 */
public class KM5Sded2Contributor
    extends KM5AbstractContributor {

  /**
   * Creator singleton.
   */
  public static final IKM5ContributorCreator CREATOR = KM5Sded2Contributor::new;

  private IStringListEdit lastCreatedodelIds = new StringArrayList();

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @param aDomain {@link IM5Domain} - connection domain
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public KM5Sded2Contributor( ISkConnection aConn, IM5Domain aDomain ) {
    super( aConn, aDomain );
  }

  // ------------------------------------------------------------------------------------
  // KM5AbstractContributor
  //

  @Override
  protected IStringList papiCreateModels() {
    IStringListEdit llModelIds = new StringLinkedBundleList();
    // ISkClassInfo model
    M5Model<ISkClassInfo> skClassModel = new Sded2SkClassInfoM5Model( skConn() );
    m5().addModel( skClassModel );
    llModelIds.add( skClassModel.id() );
    // ISkObject model
    M5Model<ISkObject> skObjectModel = new Sded2SkObjectM5Model( skConn() );
    m5().addModel( skObjectModel );
    llModelIds.add( skObjectModel.id() );
    return llModelIds;
  }

}
