package malachite.engine.gfx;

import malachite.engine.util.Time;

import java.util.concurrent.ConcurrentLinkedDeque;

public class Loader implements Runnable {
  final Thread _thread = new Thread(this);

  private volatile boolean _running;
  private boolean _finished = true;

  private int _fps;

  private ConcurrentLinkedDeque<Callback> _cb = new ConcurrentLinkedDeque<>();

  public boolean isRunning() { return _running; }
  public boolean isFinished() { return _finished; }
  public int getFPS() { return _fps; }

  public void start() {
    _running = true;
    _thread.setPriority(Thread.MIN_PRIORITY);
    _thread.start();

    System.out.println("Loader thread started."); //$NON-NLS-1$
  }

  public void stop() {
    _running = false;
    synchronized(_thread) {
      _thread.notifyAll();
    }
  }

  public void add(Callback cb) {
    _cb.add(cb);

    synchronized(_thread) {
      _thread.notifyAll();
    }
  }

  @Override
  public void run() {
    _fps = 120;

    double fpsTimeout = Time.HzToTicks(1);
    double fpsTimer   = Time.get() + fpsTimeout;
    int fpsCount = 0;

    while(_running) {
      Callback cb;

      if((cb = _cb.poll()) != null) {
        cb.load();
      }

      if(fpsTimer <= Time.get()) {
        fpsTimer = Time.get() + fpsTimeout;
        _fps = fpsCount;
        fpsCount = 0;
      }

      try {
        if(_cb.isEmpty()) {
          synchronized(_thread) {
            _thread.wait(1000);
          }
        } else {
          synchronized(_thread) {
            _thread.wait(10);
          }
        }
      } catch(InterruptedException e) { }
    }

    _finished = true;
    synchronized(_thread) {
      _thread.notifyAll();
    }

    System.out.println("Loader thread finished."); //$NON-NLS-1$
  }

  public interface Callback { void load(); }

  public enum LoaderThread {
    GRAPHICS,
    OFFLOAD
  }
}
