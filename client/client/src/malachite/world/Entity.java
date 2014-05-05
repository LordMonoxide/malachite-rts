package malachite.world;

import malachite.api.Lang;
import malachite.api.Lang.GameKeys;
import malachite.engine.gfx.fonts.Font.FONT_FACE;
import malachite.engine.gfx.fonts.TextStream;
import malachite.engine.physics.Movable;

public abstract class Entity extends Movable {
  public final Source source;
  
  private TextStream _name;
  
  private int _w, _h;
  
  public Entity(Source source, int w, int h) { this(source, 0, 0, w, h); }
  public Entity(Source source, float x, float y, int w, int h) {
    this.source = source;
    _x = x;
    _y = y;
    _w = w;
    _h = h;
    
    _name = new TextStream();
    _name.insert(_name.face(FONT_FACE.BOLD), _name.text(Lang.Game.get(source.name())));
  }
  
  public TextStream name() {
    return _name;
  }
  
  public int getW() { return _w; }
  public int getH() { return _h; }
  
  public boolean isBeside(Entity e) {
    return Math.hypot(_x - e._x, _y - e._y) <= 20;
  }
  
  @Override
  public String toString() {
    return "Entity @(" + _x + ", " + _y + ") " + _w + "x" + _h + " (" + super.toString() + ')'; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
  }
  
  public interface Source {
    public Entity createEntity();
    public GameKeys name();
  }
}