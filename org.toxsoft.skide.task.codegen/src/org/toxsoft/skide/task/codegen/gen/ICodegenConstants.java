package org.toxsoft.skide.task.codegen.gen;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;

/**
 * Code generation constants.
 *
 * @author hazard157
 */
public interface ICodegenConstants {

  /**
   * Extension of created Java files (without a leading dot).
   */
  String JAVA_FILE_EXT = "java"; //$NON-NLS-1$

  /**
   * Extension of created Java files (with a leading dot).
   */
  String JAVA_FILE_DOT_EXT = ".java"; //$NON-NLS-1$

  /**
   * Task input reference: code generation environment.
   */
  ITsContextRefDef<ICodegenEnvironment> REFDEF_CODEGEN_ENV = TsContextRefDef.create( ICodegenEnvironment.class, //
      TSID_IS_MANDATORY, AV_TRUE //
  );

}
