package org.toxsoft.skide.core.env;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.env.ISkideProjectPropertiesConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.core.txtproj.lib.workroom.*;

/**
 * {@link ISkideEnvironment} implementation.
 *
 * @author hazard157
 */
public final class SkideEnvironment
    implements ISkideEnvironment, ITsGuiContextable {

  private static final String STORAGE_OPID_PROPECT_PROPS = SKIDE_FULL_ID + ".ProjectProperties"; //$NON-NLS-1$

  /**
   * {@link ISkideEnvironment#projectProperties()} implemantation.
   *
   * @author hazard157
   */
  class ProjectProperties
      implements ISkideProjectProperties {

    private final GenericChangeEventer eventer;
    private final IOptionSetEdit       params = new OptionSet();

    public ProjectProperties() {
      eventer = new GenericChangeEventer( this );
    }

    @Override
    public IOptionSet params() {
      return params;
    }

    @Override
    public void setProperties( IOptionSet aProperties ) {
      TsNullArgumentRtException.checkNull( aProperties );
      for( IDataDef dd : ALL_SPP_OPS ) {
        IAtomicValue av = aProperties.getValue( dd.id(), null );
        if( av != null ) {
          if( !dd.validator().validate( av ).isError() ) {
            params.setValue( dd, av );
          }
        }
      }
      ITsWorkroom wr = tsContext().get( ITsWorkroom.class );
      IKeepablesStorage ks = wr.getApplicationStorage().ktorStorage();
      ks.writeItem( STORAGE_OPID_PROPECT_PROPS, params, OptionSetKeeper.KEEPER_INDENTED );
      eventer.fireChangeEvent();
    }

    void loadProperties() {
      ITsWorkroom wr = tsContext().get( ITsWorkroom.class );
      IKeepablesStorage ks = wr.getApplicationStorage().ktorStorage();
      params.setAll( ks.readItem( STORAGE_OPID_PROPECT_PROPS, OptionSetKeeper.KEEPER_INDENTED, IOptionSet.NULL ) );
    }

    @Override
    public IGenericChangeEventer genericChangeEventer() {
      return eventer;
    }

  }

  private final ISkidePluginsRegistrator pluginsRegistrator = new SkidePluginsRegistrator();
  private final ProjectProperties        projectProperties  = new ProjectProperties();

  private ITsGuiContext tsContext = null;

  /**
   * Constructor.
   */
  public SkideEnvironment() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    TsIllegalStateRtException.checkNull( tsContext );
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // class API (to be used only by SkIDE core
  //

  @SuppressWarnings( "javadoc" )
  public void initWin( ITsGuiContext aContext ) {
    TsIllegalStateRtException.checkNoNull( tsContext );
    tsContext = aContext;
    projectProperties.loadProperties();
  }

  // ------------------------------------------------------------------------------------
  // ISkideEnvironment
  //

  @Override
  public ISkidePluginsRegistrator pluginsRegistrator() {
    return pluginsRegistrator;
  }

  @Override
  public ISkideProjectProperties projectProperties() {
    return projectProperties;
  }

}
