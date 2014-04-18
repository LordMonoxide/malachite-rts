package malachite.engine.gfx.fonts;

import java.util.ArrayList;
import java.util.Iterator;

import malachite.engine.gfx.fonts.Font.Glyph;

public class TextStream implements Iterable<TextStreamable> {
  private ArrayList<TextStreamable> _stream = new ArrayList<>();
  
  public void insert(String data) {
    insert(new Text(data));
  }
  
  public void insert(TextStreamable data) {
    _stream.add(data);
  }
  
  public static class Text implements TextStreamable {
    String _text;
    
    public Text() { }
    
    public Text(String text) {
      _text = text;
    }
    
    public String getText() {
      return _text;
    }
    
    public void setText(String text) {
      _text = text;
    }
    
    @Override
    public void render(FontRenderState state) {
      if(_text == null) { return; }
      for(int i = 0; i < _text.length(); i++) {
        Glyph glyph = state.font._glyph[state.mask == 0 ? _text.codePointAt(i) : state.mask];
        
        switch(glyph.code) {
          case '\n':
            state.newLine();
            break;
          
          case ' ':
            state.x += 4;
            if(state.x >= state.w && state.w != 0) {
              state.newLine();
            } else {
              state.matrix.translate(4, 0);
            }
            
            break;
          
          default:
            state.x += glyph.w;
            if(state.x >= state.w && state.w != 0) {
              state.newLine();
            }
            
            //TODO: This is just a hack to temporarily get font colour working
            glyph.setColour(state.c);
            glyph.draw();
            state.matrix.translate(glyph.w, 0);
        }
        
        //TODO: Don't think this is necessary
        /*if(state.x >= state.w && state.w != 0) {
          state.matrix.pop();
          state.matrix.push();
        }*/
      }
    }
  }
  
  public static class Colour implements TextStreamable {
    private float[] _c = new float[] {1, 1, 1, 1};
    
    public Colour(float[] c) {
      setColour(c);
    }
    
    public Colour(float r, float g, float b, float a) {
      setColour(r, g, b, a);
    }
    
    public float[] getColour() {
      return _c;
    }
    
    public void setColour(float[] c) {
      _c = c;
    }
    
    public void setColour(float r, float g, float b, float a) {
      _c[0] = r;
      _c[1] = g;
      _c[2] = b;
      _c[3] = a;
    }
    
    @Override
    public void render(FontRenderState state) {
      state.c = _c;
    }
  }
  
  @Override
  public Iterator<TextStreamable> iterator() {
    return _stream.iterator();
  }
}