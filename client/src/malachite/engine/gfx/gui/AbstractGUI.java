package malachite.engine.gfx.gui;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractMatrix;
import malachite.engine.gfx.Loader;
import malachite.engine.gfx.textures.TextureBuilder;
import org.lwjgl.input.Keyboard;

public abstract class AbstractGUI {
  private static final float[] _clearColour = {0.0f, 0.0f, 0.0f, 1.1f};

  protected AbstractMatrix _matrix = AbstractContext.getMatrix();
  protected TextureBuilder _textures = TextureBuilder.getInstance();

  protected boolean _loaded;

  private boolean _visible = true;

  protected AbstractContext _context;
  private AbstractControl<? extends ControlEvents> _control;
  private AbstractControl<? extends ControlEvents> _focus;

  private AbstractControl<? extends ControlEvents> _keyDownControl;
  private AbstractControl<? extends ControlEvents> _selectControl;
  private AbstractControl<? extends ControlEvents> _selectControlMove;
  private int _selectButton = -1;
  private int _mouseX, _mouseY;

  private boolean _forceSelect;

  protected AbstractGUI() {
    _context = AbstractContext.getContext();
    _control = new AbstractControl<ControlEvents>(this) {
      @Override protected void resize() { }
    };

    _context.addLoadCallback(Loader.LoaderThread.OFFLOAD, () -> {
      this.load();
      _loaded = true;
    });
  }

  public boolean getVisible() {
    return _visible;
  }

  public void setVisible(boolean visible) {
    _visible = visible;
  }

  protected AbstractControl<? extends ControlEvents> getFocus() {
    return _focus;
  }

  public ControlList controls() {
    return _control._controlList;
  }

  protected void setFocus(AbstractControl<? extends ControlEvents> control) {
    if(_selectControl == _focus) {
      _selectControl = null;
    }

    if(_focus != null) {
      AbstractControl<? extends ControlEvents> focus = _focus;
      _focus = null;
      focus.setFocus(false);
    }

    _focus = control;
  }

  protected void setWH(int w, int h) {
    _control.setWH(w, h);
  }

  protected abstract void load();
  protected abstract void destroy();
  protected abstract void resize();
  protected abstract void draw();
  protected abstract boolean logic();

  protected final boolean logicGUI() {
    boolean b = logic();
    _control.logicControl();
    return b;
  }

  protected final void drawGUI() {
    draw();
    drawControls();
  }

  protected final void drawSelect() {
    _context.clear(_clearColour);
    _control.drawSelect();
  }

  protected final void drawControls() {
    if(_visible) {
      _matrix.push();
      _matrix.reset();

      if(!_forceSelect) {
        _control.draw();
      } else {
        drawSelect();
      }

      _matrix.pop();
    }
  }

  public void push() {
    _context.GUIs().push(this);
  }

  public void pop() {
    _context.GUIs().pop(this);
  }

  private AbstractControl<? extends ControlEvents> getSelectControl(int[] colour) {
    if(_control != null) {
      return _control.getSelectControl(colour);
    } else {
      return null;
    }
  }

  protected final int getAllX(AbstractControl<? extends ControlEvents> control) {
    int x = control.getX();

    if(control._controlParent != null) {
      x += getAllX(control._controlParent);
    }

    return x;
  }

  protected final int getAllY(AbstractControl<? extends ControlEvents> control) {
    int y = control.getY();

    if(control._controlParent != null) {
      y += getAllY(control._controlParent);
    }

    return y;
  }

  protected final boolean mouseDown(int x, int y, int button) {
    _selectButton = button;

    drawSelect();

    int[] pixel = _context.getPixel(x, y);

    if(pixel[0] != 0 || pixel[1] != 0  || pixel[2] != 0) {
      _selectControl = getSelectControl(pixel);

      if(_selectControl != null) {
        if(_selectControl.acceptsFocus()) {
          _selectControl.setFocus(true);
        }

        _selectControl.handleMouseDown(x - getAllX(_selectControl), y - getAllY(_selectControl), button);
      } else {
        System.err.println("Found no controls of this colour");
      }
    }

    return handleMouseDown(x, y, button);
  }

  protected final boolean mouseUp(int x, int y, int button) {
    _selectButton = -1;

    if(_selectControl != null) {
      _selectControl.handleMouseUp(x - getAllX(_selectControl), y - getAllY(_selectControl), button);
      _selectControl = null;
      return true;
    }

    return handleMouseUp(x, y, button);
  }

  protected final boolean mouseMove(int x, int y) {
    _mouseX = x;
    _mouseY = y;

    if(_selectControl != null) {
      _selectControl.handleMouseMove(x - getAllX(_selectControl), y - getAllY(_selectControl), _selectButton);
    } else {
      drawSelect();

      int[] pixel = _context.getPixel(x, y);

      if(pixel[0] != 0 || pixel[1] != 0 || pixel[2] != 0) {
        _selectControl = getSelectControl(pixel);

        if(_selectControl != _selectControlMove) {
          if(_selectControlMove != null) { _selectControlMove.handleMouseLeave(); }
          if(_selectControl     != null) { _selectControl.handleMouseEnter(); }
          _selectControlMove = _selectControl;
        }

        if(_selectControl != null) {
          _selectControl.handleMouseMove(x - getAllX(_selectControl), y - getAllY(_selectControl), _selectButton);
          _selectControl = null;
        }
      } else {
        if(_selectControlMove != null) {
          _selectControlMove.handleMouseLeave();
          _selectControlMove = null;
        }
      }
    }

    return handleMouseMove(x, y, _selectButton);
  }

  protected final boolean mouseWheel(int delta) {
    drawSelect();

    int[] pixel = _context.getPixel(_mouseX, _mouseY);

    if(pixel[0] != 0 || pixel[1] != 0  || pixel[2] != 0) {
      _selectControl = getSelectControl(pixel);

      if(_selectControl != null) {
        _selectControl.handleMouseWheel(delta);
        _selectControl = null;
      }
    }

    return handleMouseWheel(delta);
  }

  protected final boolean keyDown(int key) {
    if(key == Keyboard.KEY_F12) {
      _forceSelect = !_forceSelect;

      if(_forceSelect) {
        System.out.println("Switching GUI render mode to select");
      } else {
        System.out.println("Switching GUI render mode to normal");
      }
    }

    if(_focus != null) {
      _keyDownControl = _focus;
      _focus.handleKeyDown(key);
    }

    return handleKeyDown(key);
  }

  protected final boolean keyUp(int key) {
    if(_keyDownControl != null) {
      _keyDownControl.handleKeyUp(key);
    }

    return handleKeyUp(key);
  }

  protected final boolean charDown(char key) {
    if(_focus != null) {
      _focus.handleCharDown(key);
    }

    return handleCharDown(key);
  }

  protected boolean handleMouseDown (int x, int y, int button) { return false; }
  protected boolean handleMouseUp   (int x, int y, int button) { return false; }
  protected boolean handleMouseMove (int x, int y, int button) { return false; }
  protected boolean handleMouseWheel(int delta)                { return false; }
  protected boolean handleKeyDown   (int key)  { return false; }
  protected boolean handleKeyUp     (int key)  { return false; }
  protected boolean handleCharDown  (char key) { return false; }
}
