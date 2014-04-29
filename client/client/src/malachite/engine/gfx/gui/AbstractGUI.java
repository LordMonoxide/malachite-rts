package malachite.engine.gfx.gui;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractMatrix;
import malachite.engine.gfx.Loader;
import malachite.engine.gfx.textures.TextureBuilder;

import org.lwjgl.input.Keyboard;

public abstract class AbstractGUI {
  private static final float[] _clearColour = {0.0f, 0.0f, 0.0f, 1.1f};

  protected AbstractMatrix _matrix = AbstractContext.getMatrix();
  protected TextureBuilder _textures = TextureBuilder.getInstance();

  protected Events  _events;
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
    _control = new AbstractControl<ControlEvents>() {
      @Override protected void resize() { }
    };
    _control._gui = this;
    
    _events = new Events();

    _context.addLoadCallback(Loader.LoaderThread.OFFLOAD, () -> {
      this.load();
      _loaded = true;
      _events.raiseLoad();
    });
  }
  
  public Events events() {
    return _events;
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
  public    abstract void destroy();
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
    }
    
    return null;
  }

  protected final boolean mouseDown(int x, int y, int button) {
    boolean handled = false;
    
    _selectButton = button;

    drawSelect();

    int[] pixel = _context.getPixel(x, y);

    if(pixel[0] != 0 || pixel[1] != 0  || pixel[2] != 0) {
      _selectControl = getSelectControl(pixel);

      if(_selectControl != null) {
        if(_selectControl.acceptsFocus()) {
          _selectControl.setFocus(true);
        }

        _selectControl.handleMouseDown(x - _selectControl.getAllX(), y - _selectControl.getAllY(), button);
        handled = true;
      } else {
        System.err.println("Found no controls of this colour"); //$NON-NLS-1$
      }
    }

    return handleMouseDown(x, y, button) || handled;
  }

  protected final boolean mouseUp(int x, int y, int button) {
    boolean handled = false;
    
    _selectButton = -1;

    if(_selectControl != null) {
      _selectControl.handleMouseUp(x - _selectControl.getAllX(), y - _selectControl.getAllY(), button);
      _selectControl = null;
      handled = true;
    }

    return handleMouseUp(x, y, button) || handled;
  }

  protected final boolean mouseMove(int x, int y) {
    boolean handled = false;
    
    _mouseX = x;
    _mouseY = y;

    if(_selectControl != null) {
      _selectControl.handleMouseMove(x - _selectControl.getAllX(), y - _selectControl.getAllY(), _selectButton);
      
      handled = true;
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
          _selectControl.handleMouseMove(x - _selectControl.getAllX(), y - _selectControl.getAllY(), _selectButton);
          _selectControl = null;
          
          handled = true;
        }
      } else {
        if(_selectControlMove != null) {
          _selectControlMove.handleMouseLeave();
          _selectControlMove = null;
        }
      }
    }

    return handleMouseMove(x, y, _selectButton) || handled;
  }

  protected final boolean mouseWheel(int delta) {        
    boolean handled = false;

    drawSelect();

    int[] pixel = _context.getPixel(_mouseX, _mouseY);

    if(pixel[0] != 0 || pixel[1] != 0  || pixel[2] != 0) {
      _selectControl = getSelectControl(pixel);

      if(_selectControl != null) {
        _selectControl.handleMouseWheel(delta);
        _selectControl = null;
        
        handled = true;
      }
    }

    return handleMouseWheel(delta) || handled;
  }

  protected final boolean keyDown(int key) {
    if(key == Keyboard.KEY_F12) {
      _forceSelect = !_forceSelect;

      if(_forceSelect) {
        System.out.println("Switching GUI render mode to select"); //$NON-NLS-1$
      } else {
        System.out.println("Switching GUI render mode to normal"); //$NON-NLS-1$
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

  public class Events {
    private Deque<Event> _load = new ConcurrentLinkedDeque<>();

    private Events() {
    }

    public void addLoadHandler(Event e) {
      _load.add(e);

      if(_loaded) {
        raiseLoad();
      }
    }

    public void raiseLoad() {
      Event e = null;
      while((e = _load.poll()) != null) {
        e.run();
      }
    }
  }
  
  public interface Event {
    void run();
  }
}
