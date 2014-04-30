package malachite.world;

public class Entity {
  public final Source source;
  
  private float  _x, _y;
  private int    _w, _h;
  
  public Entity(Source source, float x, float y, int w, int h) {
    this.source = source;
    _x = x;
    _y = y;
    _w = w;
    _h = h;
  }
  
  public float getX() { return _x; }
  public float getY() { return _y; }
  public int   getW() { return _w; }
  public int   getH() { return _h; }
  
  @Override
  public String toString() {
    return "Entity @(" + _x + ", " + _y + ") " + _w + "x" + _h + " (" + super.toString() + ')'; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
  }
  
  public interface Source {
    public Entity createEntity();
  }
}