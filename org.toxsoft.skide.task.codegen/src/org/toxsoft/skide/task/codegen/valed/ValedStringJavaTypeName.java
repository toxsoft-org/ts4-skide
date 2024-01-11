package org.toxsoft.skide.task.codegen.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;
import static org.toxsoft.skide.task.codegen.valed.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.skide.task.codegen.gen.*;

/**
 * VELED to enter Java type name as {@link String}.
 *
 * @author hazard157
 */
public class ValedStringJavaTypeName
    extends AbstractValedControl<String, Text> {

  /**
   * ID of option {@link #OPDEF_CODEGEN_JAVA_TYPE}.
   */
  public static final String OPID_CODEGEN_JAVA_TYPE = VALED_OPID_PREFIX + ".JavaTypeName.TypeKeyword"; //$NON-NLS-1$

  /**
   * Option contains one of the {@link ECodegenJavaType} constants determining Java type kind.
   * <p>
   * This option determines what kind of warnings will be invoked by the VALED. While validation errors are determined
   * by the Java programming language specification, warnings are determined by the rules used by TS platform
   * developers.
   */
  public static final IDataDef OPDEF_CODEGEN_JAVA_TYPE = DataDef.create( OPID_CODEGEN_JAVA_TYPE, VALOBJ, //
      TSID_NAME, STR_OP_CODEGEN_JAVA_TYPE, //
      TSID_DESCRIPTION, STR_OP_CODEGEN_JAVA_TYPE_D, //
      TSID_KEEPER_ID, ECodegenJavaType.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ECodegenJavaType.INTERFACE ) //
  );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".StringJavaTypeName"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<String> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedStringJavaTypeName( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( String.class );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private final ECodegenJavaType codegenJavaType;

  private Text text = null;

  private ValedStringJavaTypeName( ITsGuiContext aTsContext ) {
    super( aTsContext );
    codegenJavaType = OPDEF_CODEGEN_JAVA_TYPE.getValue( tsContext().params() ).asValobj();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void internalSetValueToWidget( String aValue ) {
    setSelfEditing( true );
    try {
      text.setText( aValue );
    }
    finally {
      setSelfEditing( false );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Text doCreateControl( Composite aParent ) {
    text = new Text( aParent, SWT.BORDER );
    text.setToolTipText( getTooltipText() );
    text.addModifyListener( notificationModifyListener );
    text.addFocusListener( notifyEditFinishedOnFocusLostListener );
    text.setEditable( isEditable() );
    return text;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( isWidget() ) {
      text.setEditable( isEditable() );
    }
  }

  @Override
  protected String doGetUnvalidatedValue() {
    return text.getText();
  }

  @Override
  public ValidationResult canGetValue() {
    String name = text.getText();
    return codegenJavaType.nameValidator().validate( name );
  }

  @Override
  protected void doSetUnvalidatedValue( String aValue ) {
    internalSetValueToWidget( aValue );
  }

  @Override
  protected void doClearValue() {
    internalSetValueToWidget( EMPTY_STRING );
  }

}
