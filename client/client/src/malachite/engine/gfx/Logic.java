package malachite.engine.gfx;

import malachite.engine.util.Time;
import org.lwjgl.input.Keyboard;

class Logic implements Runnable {
  private AbstractContext _context;

  private Thread _thread;

  private boolean _running;
  private boolean _finished;

  private boolean[] _keyDown = new boolean[256];

  private int _fps;

  Logic(AbstractContext context) {
    _context = context;
  }

  public void start() {
    if(_thread != null) { return; }
    _running = true;
    _thread = new Thread(this);
    _thread.start();

    System.out.println("Logic thread started.");
  }

  public void stop() {
    _running = false;
  }

  @Override
  public void run() {
    _fps = 120;
    double logicTimeout = Time.HzToTicks(_fps);
    double logicTimer   = Time.get();

    double inputTimeout = Time.HzToTicks(60);
    double inputTimer   = Time.get();

    double fpsTimeout   = Time.HzToTicks(1);
    double fpsTimer     = Time.get() + fpsTimeout;
    int fpsCount = 0;

    while(_running) {
      if(inputTimer <= Time.get()) {
        inputTimer += inputTimeout;
        keyboard();
      }

      if(logicTimer <= Time.get()) {
        logicTimer += logicTimeout;
        _context._gui.logic();
        fpsCount++;
      }

      if(fpsTimer <= Time.get()) {
        fpsTimer = Time.get() + fpsTimeout;
        _fps = fpsCount;
        fpsCount = 0;
      }

      try {
        Thread.sleep(1);
      } catch(InterruptedException e) { }
    }

    _finished = true;

    System.out.println("Logic thread finished.");
  }

  protected void keyboard() {
    if(Keyboard.next()) {
      if(Keyboard.getEventKeyState()) {
        if(!_keyDown[Keyboard.getEventKey()]) {
          _keyDown[Keyboard.getEventKey()] = true;
          _context._gui.keyDown(Keyboard.getEventKey());
        }

        if(Keyboard.getEventCharacter() != 0) {
          switch(Keyboard.getEventCharacter()) {
            case  8: case  9:
            case 13: case 27:
              break;

            default:
              _context._gui.charDown(Keyboard.getEventCharacter());
          }
        }
      } else {
        _keyDown[Keyboard.getEventKey()] = false;
        _context._gui.keyUp(Keyboard.getEventKey());
      }
    }
  }
}
