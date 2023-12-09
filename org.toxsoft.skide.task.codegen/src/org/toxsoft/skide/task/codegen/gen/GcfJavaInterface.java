package org.toxsoft.skide.task.codegen.gen;

import java.nio.channels.*;

import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Java <code>interface</code> generator.
 *
 * @author hazard157
 */
public class GcfJavaInterface
    extends GcfAbstractJavaFile {

  /**
   * Constructor.
   *
   * @param aJavaTypeName String - Java type name, will be the created file name also
   * @param aEnv {@link IllegalChannelGroupException} - commen settings and environment
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed validation by {@link ValidatorJavaTypeName#VALIDATOR}
   */
  GcfJavaInterface( String aJavaTypeName, ICodeGenerationEnvironment aEnv ) {
    super( aJavaTypeName, aEnv );
  }

  // ------------------------------------------------------------------------------------
  // GcfAbstractJavaFile
  //

  @Override
  protected String doGetTypeSpecification() {
    return "interface"; //$NON-NLS-1$
  }

  @Override
  protected void doWriteTypeBody() {

    // TODO Auto-generated method stub
    cw().printComment( "Yahoo!", "TODO the interface body" );
  }

}
