package malachite.engine.physics;

import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.engine.util.Time;
import malachite.pathfinding.Point;

public class Sandbox implements Runnable {
  private ConcurrentLinkedDeque<Movable> _obj = new ConcurrentLinkedDeque<>();
  
  private Thread _thread;
  
  private boolean _running;
  
  private int _fps;
  
  public int fps() {
    return _fps;
  }
  
  public void add(Movable m) {
    _obj.add(m);
  }
  
  public void remove(Movable m) {
    _obj.remove(m);
  }
  
  public void start() {
    if(_thread != null) { return; }
    _running = true;
    _thread = new Thread(this);
    _thread.start();
    
    System.out.println("Physics thread started."); //$NON-NLS-1$
  }
  
  public void stop() {
    _running = false;
  }
  
  @Override
  public void run() {
    _fps = 120;
    
    double timeout = Time.HzToTicks(_fps);
    double timer   = Time.get();
    
    double fpsTimeout   = Time.HzToTicks(1);
    double fpsTimer     = Time.get() + fpsTimeout;
    int fpsCount = 0;
    
    while(_running) {
      if(timer <= Time.get()) {
        timer += timeout;
        
        for(Movable m : _obj) {
          Point p = m.nextDest();
          
          if(p != null) {
            double th = Math.atan2(p.y - m._y, p.x - m._x);
            System.out.println(th + '\t' + m._bear);
            
            if(m._bear != th) {
              //double arc = Math.PI - Math.abs(Math.abs(m._bear - th) - 180);
              m._bear = th;
            }
            
            if(m.isStopped()) {
              m.startMoving();
            }
          }
          
          if(m._velTarget != 0) {
            if(m._vel < m._velTerm) {
              m.setVel(m._vel + m._acc);
              if(m._vel > m._velTerm) {
                m.setVel(m._velTerm);
              }
            }
          } else {
            if(m._vel > 0) {
              m.setVel(m._vel - m._dec);
              if(m._vel < 0) {
                m.setVel(0);
              }
            }
          }
          
          if(m._vel != 0) {
            m.setX((float)(m._x + Math.cos(m._bear) * m._vel));
            m.setY((float)(m._y + Math.sin(m._bear) * m._vel));
          }
        }
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
    
    System.out.println("Physics thread finished."); //$NON-NLS-1$
  }
}