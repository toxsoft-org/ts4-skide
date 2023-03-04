package org.toxsoft.skide.core.mainconn_old;

import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.backend.metainf.*;
import org.toxsoft.uskat.core.connection.*;

// TODO TRANSLATE

/**
 * Поставщик способа конфигруации аргументов {@link ISkConnection#open(ITsContextRo)}.
 * <p>
 * Что делает поставщик: выдает список описаний опции {@link #listOpDef()} и имея значения опции, готовит аргумент
 * методом {@link #fillArgs(ITsContext, IOptionSet)} для открытия соединеия.
 * <p>
 * Зачем нужен поставщик: значения опции являются "текстовими" данными, могут быть сохранены между запусками программы,
 * в то время как аргумент {@link ITsContextRo} содержит ссылки, подготавливаемые на рантайме, и они не могут быть
 * сохранены между зупусками программы. Кроме того, наличие описания {@link #listOpDef()} позволяет унифицированно, с
 * помощтю единого GUI (например, {@link DialogOptionsEdit}) редактировать аргументы открытия соединения.
 *
 * @author hazard157
 */
public sealed interface ISkConnCfgProvider
    extends IStridable permits SkConnCfgProvider {

  /**
   * Returns the underlying backend meta-information.
   *
   * @return {@link ISkBackendMetaInfo} - information about backend
   */
  ISkBackendMetaInfo backendMetaInfo();

  /**
   * Перечисляет описания опции для приготовления аргумета открытия соединия.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - описания опции
   */
  IStridablesList<IDataDef> listOpDef();

  /**
   * Проверяет набор значений опции конфигурации на валидность.
   * <p>
   * Лишние (не перечисленные в {@link #listOpDef()}) значения игнорируются.
   *
   * @param aOpValues {@link IOptionSet} - значения опции, перечисленных в {@link #listOpDef()}
   * @return {@link ValidationResult} - результат проверки
   */
  ValidationResult validateOpValues( IOptionSet aOpValues );

  /**
   * Готовит аргумент для открытия соединения методом {@link ISkConnection#open(ITsContextRo)}.
   * <p>
   * В контекст-аргумент будут занесены все опции из {@link #listOpDef()}, но могут потребоваться дополнительные
   * аргументы, например, логин и пароль после их ввода в отдельный диалог.
   *
   * @param aSkConnArgs {@link ITsContext} - редактируемый контекст, который будет аргументо открытия соединения
   * @param aOtions {@link IOptionSet} - значения опции, перечисленных в {@link #listOpDef()}
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException не прошла проверка {@link #validateOpValues(IOptionSet)}
   */
  void fillArgs( ITsContext aSkConnArgs, IOptionSet aOtions );

}
