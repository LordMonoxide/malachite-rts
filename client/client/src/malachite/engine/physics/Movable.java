package malachite.engine.physics;

import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.pathfinding.Point;

public class Movable {
  protected float _x, _y;
  protected float _acc = 0.3f;
  protected float _dec = 0.1f;
  protected float _vel;
  protected float _velTerm = 3.0f;
  protected float _velTarget;
  protected double _bear;
  
  private ConcurrentLinkedDeque<Point> _path = new ConcurrentLinkedDeque<>();
  
  private Callback _onReachDestination;
  private boolean  _updatingPath;
  
  public final float getAcc() {
    return _acc;
  }
  
  public final void setAcc(float acc) {
    _acc = acc;
  }
  
  public final float getDec() {
    return _dec;
  }
  
  public final void setDec(float dec) {
    _dec = dec;
  }
  
  public final float getVel() {
    return _vel;
  }
  
  public void setVel(float vel) {
    _vel = vel;
  }
  
  public final float getVelTerm() {
    return _velTerm;
  }
  
  public final void setVelTerm(float velTerm) {
    _velTerm = velTerm;
  }
  
  public final float getVelTarget() {
    return _velTarget;
  }
  
  public final void setVelTarget(float velTarget) {
    _velTarget = velTarget;
  }
  
  public double getBear() {
    return _bear;
  }
  
  public void setBear(double bear) {
    _bear = bear;
  }
  
  public float getX() {
    return _x;
  }
  
  public void setX(float x) {
    _x = x;
  }
  
  public float getY() {
    return _y;
  }
  
  public void setY(float y) {
    _y = y;
  }
  
  public void startMoving() {
    _velTarget = _velTerm;
    System.out.println("Starting");
  }
  
  public void stopMoving() {
    _velTarget = 0;
    System.out.println("Stopping");
  }
  
  public boolean isMoving() {
    return _velTarget != 0;
  }
  
  public boolean isStopped() {
    return _velTarget == 0;
  }
  
  public void moveAlong(Point[] path, Callback onReachDestination) {
    _updatingPath = true;
    _path.clear();
    
    for(Point p : path) {
      _path.add(p);
    }
    
    _onReachDestination = onReachDestination;
    _updatingPath = false;
  }
  
  public Point nextDest() {
    if(_updatingPath) { return null; }
    
    if(_path.isEmpty()) {
      if(_onReachDestination != null) {
        _onReachDestination.execute();
        _onReachDestination = null;
      }
      
      return null;
    }
    
    return _path.getFirst();
  }
  
  public boolean removeDest(Point p) {
    return _path.remove(p);
  }
  
  public interface Callback {
    public void execute();
  }
}