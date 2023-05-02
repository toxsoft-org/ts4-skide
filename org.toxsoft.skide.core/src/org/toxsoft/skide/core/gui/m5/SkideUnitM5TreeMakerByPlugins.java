package org.toxsoft.skide.core.gui.m5;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skide.core.api.*;

/**
 * Groups units {@link ISkideUnit} by provider plugins {@link AbstractSkidePlugin}.
 * <p>
 * Note: additionally requests plugins from {@link ISkidePluginsRegistrator#listRegisteredPlugins()} to display
 * <b>all</b> plugins, including uninititlizaed plugins and plugins without contributed units.
 *
 * @author hazard157
 */
public class SkideUnitM5TreeMakerByPlugins
    implements ITsTreeMaker<ISkideUnit> {

  static final ITsNodeKind<AbstractSkidePlugin> NK_PLUGIN = new TsNodeKind<>( "plugin", //$NON-NLS-1$
      AbstractSkidePlugin.class, true );

  static final ITsNodeKind<ISkideUnit> NK_UNIT = new TsNodeKind<>( "unit", //$NON-NLS-1$
      ISkideUnit.class, false );

  private final ISkidePluginsRegistrator plugReg;

  /**
   * Constructor.
   *
   * @param aPlugReg {@link ISkidePluginsRegistrator} - the plugins registrator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkideUnitM5TreeMakerByPlugins( ISkidePluginsRegistrator aPlugReg ) {
    TsNullArgumentRtException.checkNull( aPlugReg );
    plugReg = aPlugReg;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IStringMap<DefaultTsNode<AbstractSkidePlugin>> makePluginNodesMap( ITsNode aRootNode ) {
    IStringMapEdit<DefaultTsNode<AbstractSkidePlugin>> map = new StringMap<>();
    for( AbstractSkidePlugin p : plugReg.listRegisteredPlugins() ) {
      DefaultTsNode<AbstractSkidePlugin> node = new DefaultTsNode<>( NK_PLUGIN, aRootNode, p );
      node.setName( p.nmName() );
      node.setIconId( p.iconId() );
      map.put( p.id(), node );
    }
    return map;
  }

  // ------------------------------------------------------------------------------------
  // ITsTreeMaker
  //

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<ISkideUnit> aItems ) {
    IStringMap<DefaultTsNode<AbstractSkidePlugin>> rootNodes = makePluginNodesMap( aRootNode );
    for( ISkideUnit u : aItems ) {
      DefaultTsNode<AbstractSkidePlugin> parentNode = rootNodes.getByKey( u.skidePlugin().id() );
      DefaultTsNode<ISkideUnit> node = new DefaultTsNode<>( NK_UNIT, parentNode, u );
      node.setName( u.nmName() );
      node.setIconId( u.iconId() );
      parentNode.addNode( node );
    }
    return (IList)rootNodes.values();
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return aNode.kind() == NK_UNIT;
  }

}
