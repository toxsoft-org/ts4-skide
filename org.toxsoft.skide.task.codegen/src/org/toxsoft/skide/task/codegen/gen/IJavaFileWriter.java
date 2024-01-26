package org.toxsoft.skide.task.codegen.gen;

import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base interface to create content and write Java public type file.
 *
 * @author hazard157
 */
public interface IJavaFileWriter {

  /**
   * Returns the created Java type.
   *
   * @return {@link ECodegenJavaType} - the Java type
   */
  ECodegenJavaType javaType();

  /**
   * Returns name of the created Java type, that is the name of the created file.
   *
   * @return String - the type (file) name
   */
  String typeName();

  /**
   * Returns Java package name for <code>package</code> instruction.
   *
   * @return String - package name
   */
  String packageName();

  /**
   * Returns full type names for <code>static import</code> clause.
   * <p>
   * Full type name is a type name with package name.
   *
   * @return aTypeNames {@link IStringList} - full type names or an empty string
   */
  IStringList getStaticImports();

  /**
   * Sets full type names for <code>static import</code> clause.
   *
   * @param aTypeFullNames {@link IStringList} - full type names or an empty string
   */
  void setStaticImports( IStringList aTypeFullNames );

  /**
   * Returns full type names for <code>import</code> clause.
   * <p>
   * Full type name is a type name with package name.
   *
   * @return {@link IStringList} - full type names or an empty string
   */
  IStringList getImports();

  /**
   * Sets full type names for <code>import</code> clause.
   *
   * @param aTypeFullNames {@link IStringList} - full type names or an empty string
   */
  void setImports( IStringList aTypeFullNames );

  /**
   * Returns comment to be added to the type declaration.
   *
   * @return String - type comment
   */
  String getTypeComment();

  /**
   * Sets comment to be added to the type declaration.
   *
   * @param aComment String - type comment
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setTypeComment( String aComment );

  /**
   * Returns type names for <code>implements</code> clause.
   *
   * @return aTypeNames {@link IStringList} - implemented type names or an empty string
   */
  IStringList getImplementsTypes();

  /**
   * Returns type names for <code>extends</code> clause.
   *
   * @return aTypeNames {@link IStringList} - extended type names or an empty string
   */
  IStringList getExtendsTypes();

  /**
   * Sets type names for <code>implements</code> clause.
   *
   * @param aTypeNames {@link IStringList} - implemented type names or an empty string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any type name is not an IDname
   */
  void setImplementsTypes( IStringList aTypeNames );

  /**
   * Sets type names for <code>extends</code> clause.
   *
   * @param aTypeNames {@link IStringList} - extended type names or an empty string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any type name is not an IDname
   */
  void setExtendsTypes( IStringList aTypeNames );

  /**
   * Writes created content to the Java file,
   */
  void writeFile();

}
