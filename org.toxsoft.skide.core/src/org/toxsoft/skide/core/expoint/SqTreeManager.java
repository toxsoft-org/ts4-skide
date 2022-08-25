package org.toxsoft.skide.core.expoint;

import org.toxsoft.core.tslib.bricks.qnodes.helpers.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ISqTreeManager} implementation.
 *
 * @author hazard157
 */
public class SqTreeManager
    implements ISqTreeManager {

  private final IStringMapEdit<IListEdit<IQNodeChildsContributor>> nodeContibs  = new StringMap<>();
  private final IStringMapEdit<IListEdit<ISqTreeNodeGuiProvider>>  nodeGuiProvs = new StringMap<>();
  private final IListEdit<ISqTreeNodeGuiProvider>                  allGuiProvs  = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public SqTreeManager() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void registerSpecificGuiProvider( String aNodeKindId, ISqTreeNodeGuiProvider aProvider ) {
    // no specific provider if it is already registred for all nodes
    if( allGuiProvs.hasElem( aProvider ) ) {
      return;
    }
    // ensure list for node
    IListEdit<ISqTreeNodeGuiProvider> ll = nodeGuiProvs.findByKey( aNodeKindId );
    if( ll == null ) {
      ll = new ElemArrayList<>();
      nodeGuiProvs.put( aNodeKindId, ll );
    }
    // add provider if not already registered for node
    if( !ll.hasElem( aProvider ) ) {
      ll.add( aProvider );
    }
  }

  private void registerAllNodeGuiProvider( ISqTreeNodeGuiProvider aProvider ) {
    // add provider only if not already registered
    if( allGuiProvs.hasElem( aProvider ) ) {
      return;
    }
    allGuiProvs.add( aProvider );
    // remove this provider fro node specific lists
    for( IListEdit<ISqTreeNodeGuiProvider> ll : nodeGuiProvs ) {
      ll.remove( aProvider );
    }
  }

  // ------------------------------------------------------------------------------------
  // ISqTreeManager
  //

  @Override
  public void registerNodeChildsContibutor( String aNodeKindId, IQNodeChildsContributor aContributor ) {
    StridUtils.checkValidIdPath( aNodeKindId );
    TsNullArgumentRtException.checkNull( aContributor );
    IListEdit<IQNodeChildsContributor> ll = nodeContibs.findByKey( aNodeKindId );
    if( ll == null ) {
      ll = new ElemArrayList<>();
      nodeContibs.put( aNodeKindId, ll );
    }
    if( !ll.hasElem( aContributor ) ) {
      ll.add( aContributor );
    }
  }

  @Override
  public IList<IQNodeChildsContributor> listNodeContributors( String aNodeKindId ) {
    IListEdit<IQNodeChildsContributor> ll = nodeContibs.findByKey( aNodeKindId );
    if( ll == null ) {
      return IList.EMPTY;
    }
    return ll;
  }

  @Override
  public IStringList listNodeKindIdsWithContributors() {
    return nodeContibs.keys();
  }

  @Override
  public void registerNodeGuiProvider( String aNodeKindId, ISqTreeNodeGuiProvider aProvider ) {
    TsNullArgumentRtException.checkNulls( aNodeKindId, aProvider );
    if( aNodeKindId.isEmpty() ) {
      registerAllNodeGuiProvider( aProvider );
      return;
    }
    StridUtils.checkValidIdPath( aNodeKindId );
    registerSpecificGuiProvider( aNodeKindId, aProvider );
  }

  @Override
  public IList<ISqTreeNodeGuiProvider> listNodeGuiProviders( String aNodeKindId ) {
    IList<ISqTreeNodeGuiProvider> ll = nodeGuiProvs.findByKey( aNodeKindId );
    if( ll == null ) {
      return IList.EMPTY;
    }
    return ll;
  }

  @Override
  public IList<ISqTreeNodeGuiProvider> listAllNodeGuiProviders() {
    return allGuiProvs;
  }

  @Override
  public IStringList listNodeKindIdsWithGuiProviders() {
    return nodeGuiProvs.keys();
  }

}
