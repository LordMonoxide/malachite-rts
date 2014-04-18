package malachite.engine.gfx.fonts;

import java.util.ArrayList;

import malachite.engine.gfx.fonts.Font.Glyph;

public class TextStream {
  private ArrayList<Object> _stream = new ArrayList<>();
  
  public void insert(String data) {
    _stream.add(data);
  }
  
  public void insert(TextStreamable data) {
    _stream.add(data);
  }
  
  public static class Text implements TextStreamable {
    String text;
    
    @Override
    public void render(FontRenderState state) {
      for(int i = 0; i < text.length(); i++) {
        Glyph glyph = state.font._glyph[state.mask == 0 ? text.codePointAt(i) : state.mask];
        
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
    @Override
    public void render(FontRenderState state) {
      
    }
  }
}