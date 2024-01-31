package org.toxsoft.skide.core.gui.m5;

import static org.toxsoft.skide.core.api.ucateg.ISkideUnitCategoryConstants.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.skide.core.api.*;

/**
 * Groups units {@link ISkideUnit} by provider plugins {@link IStridableParameterized}.
 * <p>
 * Note: additionally requests plugins from {@link ISkidePluginsRegistrator#listRegisteredPlugins()} to display
 * <b>all</b> plugins, including uninitialized plugins and plugins without contributed units.
 *
 * @author hazard157
 */
public class SkideUnitM5TreeMakerByCategory
    implements ITsTreeMaker<ISkideUnit> {

  static final ITsNodeKind<IStridableParameterized> NK_CATEGORY = new TsNodeKind<>( "category", //$NON-NLS-1$
      IStridableParameterized.class, true );

  static final ITsNodeKind<ISkideUnit> NK_UNIT = new TsNodeKind<>( "unit", //$NON-NLS-1$
      ISkideUnit.class, false );

  /**
   * Constructor.
   */
  public SkideUnitM5TreeMakerByCategory() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static IStringMap<DefaultTsNode<IStridableParameterized>> makeCategoryNodesMap( ITsNode aRootNode ) {
    IStringMapEdit<DefaultTsNode<IStridableParameterized>> map = new StringMap<>();
    for( IStridableParameterized p : ALL_UNIT_CATEGORIES ) {
      DefaultTsNode<IStridableParameterized> node = new DefaultTsNode<>( NK_CATEGORY, aRootNode, p );
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
    IStringMap<DefaultTsNode<IStridableParameterized>> rootNodes = makeCategoryNodesMap( aRootNode );
    for( ISkideUnit u : aItems ) {
      String categId = OPDEF_SKIDE_UNIT_CATEGORY.getValue( u.params() ).asString();
      if( !ALL_UNIT_CATEGORIES.hasKey( categId ) ) {
        categId = OPDEF_SKIDE_UNIT_CATEGORY.defaultValue().asString();
      }
      DefaultTsNode<IStridableParameterized> parentNode = rootNodes.getByKey( categId );
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
