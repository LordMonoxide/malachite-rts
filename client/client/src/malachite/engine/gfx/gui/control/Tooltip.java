package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.AbstractScalable;
import malachite.engine.gfx.fonts.TextStream;
import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.textures.TextureBuilder;

public class Tooltip extends AbstractControl<ControlEvents> {
  private AbstractDrawable _body, _tip;
  
  private Label _text;
  
  public Tooltip() {
    TextureBuilder t = TextureBuilder.getInstance();
    AbstractScalable s = AbstractContext.newScalable();
    s.setTexture(t.getTexture("gui/title.png"));
    s.setSize(
        new float[] {2, 10, 2, 10},
        new float[] {2, 10, 2, 10},
        5, 21, 1
    );
    
    _body = s;
    _body.setX(7);
    _body.setH(21);
    _body.createQuad();
    
    _tip = AbstractContext.newDrawable();
    _tip.setTexture(t.getTexture("gui/tooltip.png"));
    _tip.createQuad();
    
    _text = new Label();
    _text.setX((int)_body.getX() + 4);
    _text.setTextColour(1, 1, 1, 1);
    _text.setAutoSize(true);
    _text.events().addResizeHandler(new ControlEvents.Resize() {
      @Override public void resize() {
        setW(_text.getX() + _text.getW());
      }
    });
    
    controls().add(_text);
    
    setWH(100, 21);
  }
  
  @Override protected void resize() {
    _body.setW(_w);
    _body.createQuad();
    
    _text.setY((int)(_body.getH() - _text.getH()) / 2);
  }
  
  public String getText() {
    return _text.getText();
  }
  
  public void setText(String text) {
    _text.setText(text);
  }
  
  public void setText(TextStream text) {
    _text.setText(text);
  }
  
  @Override public void draw() {
    if(drawBegin()) {
      _body.draw();
      _tip.draw();
    }
    
    drawEnd();
    drawNext();
  }
}