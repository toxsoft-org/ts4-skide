package org.toxsoft.skide.core.mainconn_old;

import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактируемая реализация {@link ISkConnCfg}.
 *
 * @author goga
 */
public final class SkConnCfg
    extends StridableParameterized
    implements ISkConnCfg {

  private static final String OPID_PROVIDER_ID = TS_ID + ".internal.SkConnCfg.ProviderId"; //$NON-NLS-1$

  /**
   * Синглтон хранителя.
   */
  @SuppressWarnings( "hiding" )
  static final IEntityKeeper<ISkConnCfg> KEEPER =
      new AbstractEntityKeeper<>( ISkConnCfg.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ISkConnCfg aEntity ) {
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.values() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
        }

        @Override
        protected ISkConnCfg doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet values = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          return new SkConnCfg( id, values, params );
        }
      };

  private IOptionSetEdit values = new OptionSet();

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор
   * @param aValues {@link IOptionSet} - начальные значения {@link #values()}
   * @param aParams {@link IOptionSet} - начальные значения {@link #params()}
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException название - пустая строка
   * @throws TsIllegalArgumentRtException аргумент не ИД-путь
   */
  SkConnCfg( String aId, IOptionSet aValues, IOptionSet aParams ) {
    super( aId, aParams );
    values.setAll( aValues );
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link ISkConnCfg} - источник
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public SkConnCfg( ISkConnCfg aSource ) {
    super( aSource.id(), aSource.params() );
    values.setAll( aSource.values() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ISkConnCfg
  //

  @Override
  public String providerId() {
    return params().getStr( OPID_PROVIDER_ID, IStridable.NONE_ID );
  }

  @Override
  public IOptionSetEdit values() {
    return values;
  }

  // ------------------------------------------------------------------------------------
  // API реадктирования
  //

  /**
   * Задает название конфигурации.
   *
   * @param aName String - название
   * @param aDescription String - описание
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  @Override
  public void setNameAndDescription( String aName, String aDescription ) {
    super.setNameAndDescription( aName, aDescription );
  }

  /**
   * Задает идентификатор поставщика.
   *
   * @param aProviderId String - идентификатор поставщика
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException аргумент не ИД-путь
   */
  public void setProviderId( String aProviderId ) {
    String providerId = StridUtils.checkValidIdPath( aProviderId );
    params().setStr( OPID_PROVIDER_ID, providerId );
  }

}
