package malachite.engine.gfx.gui;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.AbstractMatrix;
import malachite.engine.util.Time;

import org.lwjgl.input.Keyboard;

public abstract class AbstractControl<T extends ControlEvents> {
  protected AbstractMatrix _matrix = AbstractContext.getMatrix();

  protected AbstractDrawable _background;
  protected AbstractDrawable _border;
  protected int _x, _y;
  protected int _w, _h;
  protected HAlign _hAlign = HAlign.ALIGN_LEFT;
  protected VAlign _vAlign = VAlign.ALIGN_MIDDLE;

  protected int _padW = 2;
  protected int _padH = 2;

  protected boolean _needsUpdate;
  protected boolean _enabled = true;
  protected boolean _visible = true;
  protected boolean _focus;

  AbstractGUI _gui;

  ControlList _controlList = new ControlList(this);
  AbstractControl<? extends ControlEvents>  _controlParent;
  AbstractControl<? extends ControlEvents>  _controlNext;
  AbstractControl<? extends ControlEvents>  _controlPrev;

  private boolean _acceptsFocus;
  private double _lastClick;

  protected AbstractDrawable _selBox;
  protected int[] _selColour;

  protected ControlEvents _events;

  protected AbstractControl(InitFlags... flags) {
    for(InitFlags flag : flags) {
      switch(flag) {
        case WITH_BACKGROUND:
          _background = AbstractContext.newDrawable();
          _background.setColour(new float[] {0.5f, 0.5f, 0.5f, 1});

        case WITH_BORDER:
          _border = AbstractContext.newDrawable();
          _border.setColour(new float[] {0, 0, 0, 1});
          _border.setXY(-1, -1);
          break;

        case WITH_DEFAULT_EVENTS:
          _events = new ControlEvents(this);
          break;

        case ACCEPTS_FOCUS:
          _acceptsFocus = true;
          break;

        case REGISTER:
          _selBox = AbstractContext.newDrawable();
          _selColour = AbstractContext.getContext().getNextSelectColour();

          float[] floatColour = new float[4];

          for(int i = 0; i < floatColour.length; i++) {
            floatColour[i] = _selColour[i] / 255f;
          }

          _selBox.setColour(floatColour);
          break;
      }
    }
  }

  protected void setGUI(AbstractGUI gui) {
    _gui = gui;
  }

  public ControlList controls() {
    return _controlList;
  }

  @SuppressWarnings("unchecked")
  public T events() {
    return (T)_events;
  }

  public int getX() { return _x; }
  public int getY() { return _y; }
  public int getW() { return _w; }
  public int getH() { return _h; }

  public void setBackground(AbstractDrawable d) {
    _background = d;
  }

  public AbstractDrawable getBackground() {
    return _background;
  }

  public void setBackgroundColour(float r, float g, float b, float a) {
    _background.setColour(r, g, b, a);
  }

  public final boolean acceptsFocus() {
    return _acceptsFocus;
  }

  public final void setX(int x) {
    _x = x;
  }

  public final void setY(int y) {
    _y = y;
  }

  public final void setXY(int x, int y) {
    _x = x;
    _y = y;
  }

  public final void setW(int w) {
    _needsUpdate = true;
    _w = w;
  }

  public final void setH(int h) {
    _needsUpdate = true;
    _h = h;
  }

  public final void setWH(int w, int h) {
    _needsUpdate = true;
    _w = w;
    _h = h;
  }

  public final void setXYWH(int x, int y, int w, int h) {
    _needsUpdate = true;
    _x = x;
    _y = y;
    _w = w;
    _h = h;
  }

  public void setHAlign(HAlign align) {
    _hAlign = align;
  }

  public HAlign getHAlign() {
    return _hAlign;
  }

  public void setVAlign(VAlign align) {
    _vAlign = align;
  }

  public VAlign getVAlign() {
    return _vAlign;
  }

  public void enable() {
    _enabled = true;
  }

  public void disable() {
    _enabled = false;
  }

  public boolean isEnabled() {
    return _enabled;
  }

  public boolean isDisabled() {
    return !_enabled;
  }
  
  public boolean isTreeDisabled() {
    if(!_enabled) { return true; }
    
    AbstractControl<? extends ControlEvents> c = _controlParent;
    while(c != null) {
      if(!c._enabled) {
        return true;
      }
      
      c = c._controlParent;
    }
    
    return false;
  }

  public void show() {
    _visible = true;
  }

  public void hide() {
    if(!_visible) { return; }
    _visible = false;
    setFocus(false);
    _controlList.killFocus();
  }

  public boolean isVisible() {
    return _visible;
  }

  public boolean isShown() {
    return _visible;
  }

  public boolean isHidden() {
    return !_visible;
  }

  public void setFocus(boolean focus) {
    if(_focus != focus) {
      if(focus) {
        _gui.setFocus(this);
        _focus = true;
        handleGotFocus();
      } else {
        _gui.setFocus(null);
        _focus = false;
        handleLostFocus();
      }
    }
  }

  public void handleKeyDown(int key) {
    if(key == Keyboard.KEY_TAB) {
      AbstractControl<? extends ControlEvents> c = _controlNext;
      if(c == null) {
        if(_controlParent != null) {
          c = _controlParent._controlList.last();
        }
      }

      while(c != null) {
        if(c == this) { break; }

        if(c.acceptsFocus()) {
          c.setFocus(true);
          break;
        }
        
        c = c._controlNext;
        if(c == null) {
          if(_controlParent != null) {
            c = _controlParent._controlList.last();
          }
        }
      }
    }

    if(_events == null) { return; }
    _events.raiseKeyDown(key);
  }

  public void handleKeyUp(int key) {
    if(_events == null) { return; }
    _events.raiseKeyUp(key);
  }

  public void handleCharDown(char key) {
    if(_events == null) { return; }
    _events.raiseKeyText(key);
  }

  public void handleMouseDown(int x, int y, int button) {
    if(_events == null) { return; }
    _events.raiseMouseDown(x, y, button);
  }

  public void handleMouseUp(int x, int y, int button) {
    if(_events == null) { return; }
    _events.raiseMouseUp(x, y, button);

    if(Time.get() - _lastClick <= 250) {
      _events.raiseClickDbl();
    } else {
      _events.raiseClick();
      _lastClick = Time.get();
    }
  }

  public void handleMouseMove(int x, int y, int button) {
    if(_events == null) { return; }
    _events.raiseMouseMove(x, y, button);
  }

  public void handleMouseWheel(int delta) {
    if(_events == null) { return; }
    _events.raiseMouseScroll(delta);
  }

  public void handleMouseEnter() {
    if(_events == null) { return; }
    _events.raiseHoverEnter();
  }

  public void handleMouseLeave() {
    if(_events == null) { return; }
    _events.raiseHoverLeave();
  }

  public void handleGotFocus() {
    if(_events == null) { return; }
    _events.raiseFocusGot();
  }

  public void handleLostFocus() {
    if(_events == null) { return; }
    _events.raiseFocusLost();
  }

  private void updateSize() {
    if(_selBox != null) {
      _selBox.setWH(_w, _h);
      _selBox.createQuad();
    }

    if(_background != null) {
      _background.setWH(_w - _background.getX() * 2,
                        _h - _background.getY() * 2);
      _background.createQuad();
    }

    if(_border != null) {
      _border.setWH(_w - _border.getX() * 2,
                    _h - _border.getY() * 2);
      _border.createBorder();
    }

    resize();

    if(_events != null) {
      _events.raiseResize();
    }

    _needsUpdate = false;
  }

  protected abstract void resize();

  protected final boolean drawBegin() {
    if(_visible) {
      if(_needsUpdate) {
        updateSize();
      }

      _matrix.push();
      _matrix.translate(_x, _y);

      if(_background != null) {
        _background.draw();
      }

      return true;
    }

    return false;
  }

  protected final void drawEnd() {
    if(_visible) {
      _controlList.draw();

      if(_border != null) {
        _border.draw();
      }

      _matrix.pop();
    }

    if(_controlNext != null) {
      _controlNext.draw();
    }
  }

  public void draw() {
    drawBegin();
    drawEnd();
  }

  public void logic() { }
  public void logicControl() {
    logic();
    _controlList.logic();

    if(_controlNext != null) {
      _controlNext.logicControl();
    }
  }

  public void drawSelect() {
    if(_visible && _enabled) {
      if(_needsUpdate) {
        updateSize();
      }

      _matrix.push();
      _matrix.translate(_x, _y);

      if(_selBox != null) {
        _selBox.draw();
      }

      _controlList.drawSelect();

      _matrix.pop();
    }

    if(_controlNext != null) {
      _controlNext.drawSelect();
    }
  }

  public AbstractControl<? extends ControlEvents> getSelectControl(int[] colour) {
    if(_selBox != null && colour[0] == _selColour[0] && colour[1] == _selColour[1] && colour[2] == _selColour[2]) {
      return this;
    }
    
    AbstractControl<? extends ControlEvents> control = _controlList.getSelectControl(colour);
    if(control != null) {
      return control;
    }
    
    if(_controlNext != null) {
      return _controlNext.getSelectControl(colour);
    }

    return null;
  }

  public enum InitFlags {
    WITH_BACKGROUND,
    WITH_BORDER,
    WITH_DEFAULT_EVENTS,
    ACCEPTS_FOCUS,
    REGISTER
  }
}
