package org.toxsoft.skide.core.e4.statline;

import static org.toxsoft.skide.core.ISkideCoreConstants.*;
import static org.toxsoft.skide.core.e4.ISkideCoreE4SharedResources.*;

import javax.annotation.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.skide.core.env.*;

/**
 * Status line tool control displays SkIDE project name.
 *
 * @author hazard157
 */
public class StatLineProjName
    implements ITsMouseInputListener {

  // TODO on hower show Project description tooltip

  TsUserInputEventsBinder mouseHelper;
  ITsE4Helper             e4Helper;
  ISkideEnvironment       skideEnv;
  Label                   label;

  @PostConstruct
  void init( Composite aParent, ITsE4Helper aE4Helper, ISkideEnvironment aSkideEnv ) {
    mouseHelper = new TsUserInputEventsBinder( this );
    e4Helper = aE4Helper;
    skideEnv = aSkideEnv;
    skideEnv.projectProperties().genericChangeEventer().addListener( s -> updateOnProjPropsChange() );
    label = new Label( aParent, SWT.BORDER );
    mouseHelper.bindToControl( label, TsUserInputEventsBinder.BIND_ALL_MOUSE_EVENTS );
    mouseHelper.addTsMouseInputListener( this );
    updateOnProjPropsChange();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void updateOnProjPropsChange() {
    String text = String.format( FMT_L_STAT_LINE_PROJ_NAME, skideEnv.projectProperties().name() );
    text = ' ' + text + ' '; // to make status item slightly wider
    String tooltip = skideEnv.projectProperties().description();
    label.setText( text );
    label.setToolTipText( tooltip );
    label.getParent().layout();
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
      Control aWidget ) {
    e4Helper.execCmd( CMDID_EDIT_PROJPROPS );
    return true;
  }

}
