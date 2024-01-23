package org.toxsoft.skide.task.codegen.gen;

import static org.toxsoft.skide.task.codegen.gen.l10n.ISkCodegenSharedResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The kind of the Java types.
 * <p>
 * The {@link #id() exactly matches respective Java keyword.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ECodegenJavaType
    implements IStridable {

  INTERFACE( "interface", STR_CG_JT_INTERFACE, STR_CG_JT_INTERFACE_D ), //$NON-NLS-1$

  CLASS( "class", STR_CG_JT_CLASS, STR_CG_JT_CLASS_D ), //$NON-NLS-1$

  ENUM( "enum", STR_CG_JT_ENUM, STR_CG_JT_ENUM_D ), //$NON-NLS-1$

  RECORD( "record", STR_CG_JT_RECORD, STR_CG_JT_RECORD_D ), //$NON-NLS-1$

  ;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "ECodegenJavaType"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ECodegenJavaType> KEEPER = new StridableEnumKeeper<>( ECodegenJavaType.class );

  private static IStridablesListEdit<ECodegenJavaType> list = null;

  private final String id;
  private final String name;
  private final String description;

  private ITsValidator<String> nameValidator = null;

  ECodegenJavaType( String aId, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the Java type name validator for this kind of Java type.
   *
   * @return {@link ITsValidatable}&lt;String&gt; - Java type name validator
   */
  public ITsValidator<String> nameValidator() {
    if( nameValidator == null ) {
      nameValidator = new ValidatorJavaTypeName( this );
    }
    return nameValidator;
  }

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ECodegenJavaType} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ECodegenJavaType> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ECodegenJavaType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ECodegenJavaType getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ECodegenJavaType} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ECodegenJavaType findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ECodegenJavaType item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ECodegenJavaType} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ECodegenJavaType getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
