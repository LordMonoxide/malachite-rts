package malachite.engine.gfx.gui;

public class ControlList {
  private AbstractControl<? extends ControlEvents> _parent;
  private AbstractControl<? extends ControlEvents> _first;
  private AbstractControl<? extends ControlEvents> _last;
  private int _size;

  protected ControlList(AbstractControl<? extends ControlEvents> parent) {
    _parent = parent;
  }

  public int size() {
    return _size;
  }

  public void add(AbstractControl<? extends ControlEvents> control) {
    control._controlParent = _parent;
    control.setGUI(_parent._gui);

    if(_first != null) {
      control._controlNext = null;
      control._controlPrev = _first;
      _first._controlNext = control;
      _first = control;
    } else {
      control._controlNext = null;
      control._controlPrev = null;
      _first = control;
      _last  = control;
    }

    _size++;
  }

  public void remove(AbstractControl<? extends ControlEvents> control) {
    AbstractControl<? extends ControlEvents> c = control._controlNext;
    if(c != null) {
      c._controlPrev = control._controlPrev;

      if(c._controlPrev == null) {
        _last = c;
      }
    } else {
      c = control._controlPrev;
      if(c != null) {
        c._controlNext = null;
      }

      _first = c;
    }

    c = control._controlPrev;
    if(c != null) {
      c._controlNext = control._controlNext;

      if(c._controlNext == null) {
        _first = c;
      }
    } else {
      c = control._controlNext;

      if(c != null) {
        c._controlPrev = null;
      }

      _last = c;
    }

    _size--;
  }

  public void killFocus() {
    AbstractControl<? extends ControlEvents> c = _last;

    while(c != null) {
      c.setFocus(false);
      c._controlList.killFocus();
      c = c._controlNext;
    }
  }
  
  public void enable() {
    AbstractControl<? extends ControlEvents> c = _last;
    
    while(c != null) {
      c.enable();
      c = c._controlNext;
    }
  }
  
  public void disable() {
    AbstractControl<? extends ControlEvents> c = _last;
    
    while(c != null) {
      c.disable();
      c = c._controlNext;
    }
  }

  public AbstractControl<? extends ControlEvents> first() {
    return _first;
  }

  public AbstractControl<? extends ControlEvents> last() {
    return _last;
  }

  public void draw() {
    if(_last != null) {
      _last.draw();
    }
  }

  public void logic() {
    if(_last != null) {
      _last.logicControl();
    }
  }

  public void drawSelect() {
    if(_last != null) {
      _last.drawSelect();
    }
  }

  public AbstractControl<? extends ControlEvents> getSelectControl(int[] colour) {
    if(_last != null) {
      return _last.getSelectControl(colour);
    }
    
    return null;
  }
}
