package malachite.engine.gfx;

import malachite.engine.gfx.gui.GUIManager;
import malachite.engine.gfx.gui.control.Label;
import malachite.engine.util.Time;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class AbstractContext {
  protected static AbstractContext _context;
  protected static AbstractMatrix  _matrix;
  protected static Class<? extends AbstractVertex>   _vertex;
  protected static Class<? extends AbstractDrawable> _drawable;
  protected static Class<? extends AbstractScalable> _scalable;

  public static AbstractContext getContext()  { return _context;  }
  public static AbstractMatrix  getMatrix()   { return _matrix;   }

  public static AbstractVertex newVertex() {
    try {
      return _vertex.newInstance();
    } catch(Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static AbstractDrawable newDrawable() {
    try {
      return _drawable.newInstance();
    } catch(Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static AbstractScalable newScalable() {
    try {
      return _scalable.newInstance();
    } catch(Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  private ContextListener _listener;

  GUIManager _gui = new GUIManager();
  private Loader _loader = new Loader();
  private Logic  _logic  = new Logic(this);
  private ConcurrentLinkedDeque<Loader.Callback> _loaderCallbacks = new ConcurrentLinkedDeque<>();

  private Thread _renderThread;
  private Thread _loaderThread;

  private int _w, _h;
  private float[] _backColour = {1.0f, 1.0f, 1.0f, 1.0f};
  private int[] _selectColour = {1, 0, 0, 255};

  private int _mouseX = 0;
  private int _mouseY = 0;
  private int _mouseButton = -1;

  private int _fpsTarget;
  private double _lastSPF;
  private double _spf;

  private volatile boolean _running;

  public GUIManager GUIs() { return _gui; }
  public String  getTitle()     { return Display.getTitle(); }
  public boolean getResizable() { return Display.isResizable(); }
  public int     getW()         { return _w; }
  public int     getH()         { return _h; }
  public int     getFPSTarget() { return _fpsTarget; }

  public void setTitle    (String title)      { Display.setTitle(title); }
  public void setResizable(boolean resizable) { Display.setResizable(resizable); }

  public void setWH(int w, int h) {
    _w = w;
    _h = h;

    if(Display.isCreated()) {
      updateSize();
    }

    _gui.resize();
  }

  public void setFPSTarget(int fpsTarget) { _fpsTarget = fpsTarget; }
  public void setContextListener(ContextListener listener) { _listener = listener; }

  protected abstract void createDisplay() throws LWJGLException;
  protected abstract void createInstances();
  protected abstract void updateSize();

  protected final boolean create() {
    if(!Display.isCreated()) {
      try {
        Display.setInitialBackground(_backColour[0], _backColour[1], _backColour[2]);
        Display.setDisplayMode(new DisplayMode(_w, _h));
        createDisplay();
      } catch(LWJGLException e) {
        e.printStackTrace();
        return false;
      }
    }

    System.out.println("Creating context " + Display.getTitle());
    System.out.println("Display adapter: " + Display.getAdapter());
    System.out.println("Driver version:  " + Display.getVersion());
    System.out.println("OpenGL version:  " + GL11.glGetString(GL11.GL_VERSION));

    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

    _context = this;
    createInstances();

    if(_matrix   == null) { System.err.println("!! Matrix is null !!"); }
    if(_vertex   == null) { System.err.println("!! Vertex is null !!"); }
    if(_drawable == null) { System.err.println("!! Drawable is null !!"); }
    if(_scalable == null) { System.err.println("!! Scalable is null !!"); }

    updateSize();

    if(_listener != null) {
      _listener.onCreate();
    }

    _renderThread = Thread.currentThread();
    _loaderThread = _loader._thread;

    return true;
  }

  public void destroy() {
    if(_running) {
      _running = false;

      _logic.stop();
      _loader.stop();
    } else {
      Display.destroy();

      while(!_loader.isFinished()) {
        try {
          synchronized(_loaderThread) {
            _loaderThread.wait(100);
          }
        } catch(InterruptedException e) {
          e.printStackTrace();
        }
      }

      _gui.destroy();

      if(_listener != null) {
        _listener.onClosed();
      }
    }
  }

  public void run() {
    _running = true;

    _logic.start();
    _loader.start();

    if(_listener != null) {
      _listener.onRun();
    }

    _lastSPF = Time.get();

    while(_running) {
      checkContext();
      checkLoader();
      clearContext();
      drawScene();
      updateContext();
      updateFrameRate();
      mouse();
      syncContext();
    }

    destroy();
  }

  private void checkContext() {
    if(Display.isCloseRequested()) {
      if(_listener == null || _listener.onClosing() == ContextListener.ShouldClose.SHOULD_CLOSE) {
        destroy();
      }
    }

    if(Display.wasResized()) {
      setWH(Display.getWidth(), Display.getHeight());

      if(_listener != null) {
        _listener.onResize();
      }
    }
  }

  private void checkLoader() {
    Loader.Callback cb = _loaderCallbacks.poll();
    if(cb != null) { cb.load(); }
  }

  private void clearContext() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
  }

  public void clear(float[] c) {
    GL11.glClearColor(c[0], c[1], c[2], c[3]);
    clearContext();
    GL11.glClearColor(_backColour[0], _backColour[1], _backColour[2], _backColour[3]);
  }

  private void updateContext() {
    Display.update();
  }

  private void syncContext() {
    Display.sync(_fpsTarget);
  }

  private void drawScene() {
    _gui.draw();
  }

  private void updateFrameRate() {
    _spf = Sys.getTime() - _lastSPF;
    _lastSPF = Sys.getTime();
  }

  private void mouse() {
    if(_mouseX != Mouse.getX() || _mouseY != _h - Mouse.getY()) {
      _mouseX = Mouse.getX();
      _mouseY = _h - Mouse.getY();
      _gui.mouseMove(_mouseX, _mouseY);
    }

    if(Mouse.next()) {
      if(Mouse.getEventButton() != -1) {
        if(Mouse.getEventButtonState()) {
          _mouseButton = Mouse.getEventButton();
          _gui.mouseDown(_mouseX, _mouseY, _mouseButton);
        } else {
          _mouseButton = -1;
          _gui.mouseUp(_mouseX, _mouseY, Mouse.getEventButton());
        }
      }

      if(Mouse.getEventDWheel() != 0) {
        _gui.mouseWheel(Mouse.getEventDWheel());
      }
    }
  }

  public int[] getPixel(int x, int y) {
    ByteBuffer pixels = BufferUtils.createByteBuffer(3);
    GL11.glReadPixels(x, _h - y, 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, pixels);
    byte[] b = {pixels.get(0), pixels.get(1), pixels.get(2)};
    return new int[] {b[0] >= 0 ? b[0] : 256 + b[0], b[1] >= 0 ? b[1] : 256 + b[1], b[2] >= 0 ? b[2] : 256 + b[2]};
  }

  public int[] getNextSelectColour() {
    int[] colour = {_selectColour[0], _selectColour[1], _selectColour[2], _selectColour[3]};

    _selectColour[0]++;
    if(_selectColour[0] == 255) {
      _selectColour[0] = 0;
      _selectColour[1]++;
      if(_selectColour[1] == 255) {
        _selectColour[1] = 0;
        _selectColour[2]++;
      }
    }

    return colour;
  }

  public void addLoadCallback(Loader.LoaderThread thread, Loader.Callback callback) {
    switch(thread) {
      case GRAPHICS:
        if(Thread.currentThread() == _renderThread) {
          callback.load();
        } else {
          _loaderCallbacks.add(callback);
        }

        break;

      case OFFLOAD:
        if(Thread.currentThread() == _loaderThread) {
          callback.load();
        } else {
          _loader.add(callback);
        }
    }
  }
}
