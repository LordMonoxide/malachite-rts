package malachite.engine.gfx.fonts;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.AbstractMatrix;
import malachite.engine.gfx.fonts.TextStream.Colour;
import malachite.engine.gfx.textures.Texture;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Font {
  private static Colour white = new Colour(new float[] {1, 1, 1, 1});
  
  private AbstractMatrix _matrix = AbstractContext.getMatrix();
  private Face _regular, _bold, _italic;
  
  private Events _events = new Events(this);
  private boolean _loaded;
  
  Font() { }
  
  public Face regular() { return _regular; }
  public Face bold   () { return _bold;    }
  public Face italic () { return _italic;  }
  
  public Events events() { return _events; }
  public boolean loaded() { return _loaded; }
  
  void load(Face regular, Face bold, Face italic) {
    _regular = regular;
    _bold    = bold;
    _italic  = italic;
    
    _regular.load();
    _bold   .load();
    _italic .load();
    
    _loaded = true;
    _events.raiseLoad();
  }
  
  public void draw(int x, int y, TextStream text) {
    draw(x, y, 0, 0, text, 0);
  }
  
  public void draw(int x, int y, TextStream text, int mask) {
    draw(x, y, 0, 0, text, mask);
  }
  
  public void draw(int x, int y, int w, int h, TextStream text) {
    draw(x, y, w, h, text, 0);
  }
  
  public void draw(int x, int y, int w, int h, TextStream text, int mask) {
    if(text == null)     { return; }
    if(_regular == null) { return; }
    
    _matrix.push();
    _matrix.translate(x, y);
    _matrix.push();
    
    FontRenderState state = new FontRenderState(_regular, 0, 0, w, h, mask, _matrix);
    
    white.render(state);
    
    for(TextStreamable s : text) {
      s.render(state);
    }
    
    _matrix.pop();
    _matrix.pop();
  }
  
  public static class Face {
    Font    _font;
    Texture _texture;
    Glyph[] _glyph;
    int     _h;
    
    Face() { }
    
    void load() {
      for(Glyph glyph : _glyph) {
        if(glyph != null) {
          glyph.create(_texture);
        }
      }
    }
    
    public int getW(TextStream text) {
      if(text == null) { return 0; }
      
      int w = 0;
      for(TextStreamable ts : text) {
        if(ts instanceof TextStream.Text) {
          w += getW(((TextStream.Text)ts).getText());
        }
      }
      
      return w;
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
    
    public int getCharAtX(String text, int x) { return getCharAtX(text, 0, x); }
    public int getCharAtX(String text, int mask, int x) {
      if(text == null) { return 0; }
      
      int w = 0;
      for(int i = 0; i < text.length(); i++) {
        int n = mask == 0 ? text.codePointAt(i) : mask;
        if(_glyph[n] != null) {
          if(x <= w + _glyph[n].w / 2) {
            return i;
          }
          
          w += _glyph[n].w;
        }
      }
      
      return text.length();
    }
  }

  protected static class Glyph {
    protected AbstractDrawable sprite;
    protected int code;
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
    
    protected void setColour(float r, float g, float b, float a) {
      sprite.setColour(r, g, b, a);
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
  
  public enum FONT_FACE {
    REGULAR, BOLD, ITALIC;
  }
}
