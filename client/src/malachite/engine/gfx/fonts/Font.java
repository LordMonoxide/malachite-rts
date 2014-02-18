package malachite.engine.gfx.fonts;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.AbstractMatrix;
import malachite.engine.gfx.textures.Texture;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Font {
  private AbstractMatrix _matrix = AbstractContext.getMatrix();
  private Texture _texture;

  private Events _events = new Events(this);
  private boolean _loaded;

  private int _h;

  private Glyph[] _glyph;

  Font() { }

  public Events events() { return _events; }
  public boolean loaded() { return _loaded; }

  void load(int h, Glyph[] glyphs, Texture texture) {
    _h = h;
    _glyph = glyphs;
    _texture = texture;

    for(Glyph glyph : _glyph) {
      if(glyph != null) {
        glyph.create(_texture);
      }
    }

    _loaded = true;
    _events.raiseLoad();
  }

  public int getW(String text) { return getW(text, 0); }
  public int getW(String text, int mask) {
    if(text == null) { return 0; }

    int w = 0;
    for(int i = 0; i < text.length(); i++) {
      int n = mask == 0 ? text.codePointAt(i) : mask;
      if(_glyph[n] != null) {
        w += _glyph[n].w;
      }
    }

    return w;
  }

  public int getH() {
    return _h;
  }

  public void draw(int x, int y, String text, float[] c) {
    draw(x, y, text, c, 0);
  }

  public void draw(int x, int y, String text, float[] c, int mask) {
    if(text == null)   { return; }
    if(_glyph == null) { return; }

    _matrix.push();
    _matrix.translate(x, y);

    for(int i = 0; i < text.length(); i++) {
      Glyph glyph = _glyph[mask == 0 ? text.codePointAt(i) : mask];

      if(glyph != null) {
        //TODO: This is just a hack to temporarily get font colour working
        glyph.setColour(c);
        glyph.draw();
        _matrix.translate(glyph.w, 0);
      } else {
        _matrix.translate(4, 0);
      }
    }

    _matrix.pop();
  }

  protected static class Glyph {
    protected AbstractDrawable sprite;
    protected int  w,  h;
    protected int tx, ty;
    protected int tw, th;

    protected void create(Texture texture) {
      sprite = AbstractContext.newDrawable();
      sprite.setTexture(texture);
      sprite.setWH(tw, th);
      sprite.setTXYWH(tx, ty, tw, th);
      sprite.createQuad();
    }

    protected void setColour(float[] c) {
      sprite.setColour(c);
      sprite.createQuad();
    }

    public void draw() {
      sprite.draw();
    }
  }

  public static class Events {
    private Deque<Event> _load = new ConcurrentLinkedDeque<>();

    private Font _this;

    public Events(Font font) {
      _this = font;
    }

    public void addLoadHandler(Event e) {
      _load.add(e);

      if(_this._loaded) {
        raiseLoad();
      }
    }

    public void raiseLoad() {
      Event e = null;
      while((e = _load.poll()) != null) {
        e.run();
      }
    }

    public interface Event {
      void run();
    }
  }
}
