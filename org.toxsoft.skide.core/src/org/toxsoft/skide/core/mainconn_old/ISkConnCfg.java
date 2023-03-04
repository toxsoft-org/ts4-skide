package org.toxsoft.skide.core.mainconn_old;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.uskat.core.connection.*;

// TODO TRANSLATE

/**
 * Конфигурация подключения: значения опции для приготовления аргумента {@link ISkConnection#open(ITsContextRo)}.
 *
 * @author hazard157
 */
public sealed interface ISkConnCfg
    extends IStridableParameterized permits SkConnCfg {

  /**
   * Возвращает идентификатор поставщка конфигурации, идин из {@link ISkConnCfgService#listProviders()}.
   *
   * @return String - идентификатор поставщика
   */
  String providerId();

  /**
   * Возвращает значения опции конфигурации.
   *
   * @return {@link IOptionSet} - значения опции
   */
  IOptionSet values();

}
