package org.toxsoft.skide.core.env;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ISkResources {

  /**
   * {@link ISkideProjectPropertiesConstants}
   */
  String STR_N_SPP_NAME        = Messages.getString( "STR_N_SPP_NAME" );        //$NON-NLS-1$
  String STR_D_SPP_NAME        = Messages.getString( "STR_D_SPP_NAME" );        //$NON-NLS-1$
  String STR_N_SPP_DESCRIPTION = Messages.getString( "STR_N_SPP_DESCRIPTION" ); //$NON-NLS-1$
  String STR_D_SPP_DESCRIPTION = Messages.getString( "STR_D_SPP_DESCRIPTION" ); //$NON-NLS-1$

  /**
   * {@link QuantSkideCoreMain}
   */
  String STR_N_LOCAL_CONN               = Messages.getString( "STR_N_LOCAL_CONN" );               //$NON-NLS-1$
  String STR_D_LOCAL_CONN               = Messages.getString( "STR_D_LOCAL_CONN" );               //$NON-NLS-1$
  String FMT_ERR_CANT_EDIT_FIXED_CONN   = Messages.getString( "FMT_ERR_CANT_EDIT_FIXED_CONN" );   //$NON-NLS-1$
  String FMT_WARN_NO_SUCH_CONN          = Messages.getString( "FMT_WARN_NO_SUCH_CONN" );          //$NON-NLS-1$
  String FMT_ERR_CANT_REMOVE_FIXED_CONN = Messages.getString( "FMT_ERR_CANT_REMOVE_FIXED_CONN" ); //$NON-NLS-1$

}
