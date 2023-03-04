package org.toxsoft.skide.core.mainconn_old;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.connection.*;

// TODO TRANSLATE

/**
 * Служба управления конфигруациями.
 * <p>
 * Ссылка на службу должна находится в контексте приложения.
 *
 * @author hazard157
 */
public interface ISkConnCfgService {

  /**
   * Возвращает изветсные конфигураций.
   *
   * @return {@link IStridablesList}&lt;{@link ISkConnCfg}&gt; - список конфигурации
   */
  IStridablesList<ISkConnCfg> configs();

  /**
   * Создает редактируемую конфигурацию.
   * <p>
   * Этот метод испольузется вместо конструктора, для контроля над идентификатором.
   *
   * @return {@link SkConnCfg} - пустая редактируемая конфигурация
   */
  SkConnCfg createConfig();

  /**
   * Добавляет новую или заменяет существующую конфигурацию.
   *
   * @param aCfg {@link ISkConnCfg} - добавляемая конфигурация
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void putConfig( ISkConnCfg aCfg );

  /**
   * Удаляет конфигурацию.
   *
   * @param aCfgId String - идентфикатор удаляемой конфигурации
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void removeConfig( String aCfgId );

  /**
   * Готовит контекст для открытия соединения.
   * <p>
   * В зависимости от опции поставщика {@link ISkConnCfgProvider#listOpDef()}, пользователь может захотеть дополнительно
   * инициализировать какие-либо поля.
   *
   * @param aConfigId String - идентификатор одного из конфигурации {@link #configs()}
   * @param aContext {@link ITsContext} - контекст, который будет подготовлен как аргумент
   *          {@link ISkConnection#open(ITsContextRo)}
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException соединение уже открыто
   * @throws TsItemNotFoundRtException нет такой конфигурации
   * @throws TsItemNotFoundRtException нет поставщика для этой кофигурации
   * @throws TsValidationFailedRtException не прошла проверка {@link ISkConnCfgProvider#validateOpValues(IOptionSet)}
   */
  void prepareConnectionArgs( String aConfigId, ITsContext aContext );

  /**
   * Возвращает заругистрированные поставщики.
   * <p>
   * Список поставщиков не сохраняется между запусками программы.
   *
   * @return {@link IStridablesList}&lt;{@link ISkConnCfgProvider}&gt; - заругистрированные постащики
   */
  IStridablesList<ISkConnCfgProvider> listProviders();

  /**
   * Реагистрирует поставщик конфигурации.
   *
   * @param aProvider {@link ISkConnCfgProvider} - поставщик конфигурации
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemAlreadyExistsRtException поставщик с таким идентификатором уже зарегистрирован
   */
  void registerConfigProvider( ISkConnCfgProvider aProvider );

}
