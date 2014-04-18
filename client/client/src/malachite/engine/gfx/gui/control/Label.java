package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.fonts.TextStream;
import malachite.engine.gfx.gui.*;

public class Label extends AbstractControl<ControlEvents> {
  private Font _font = FontBuilder.getInstance().getDefault();
  private TextStream _textStream = new TextStream();
  private TextStream.Text _text = new TextStream.Text();
  private TextStream.Colour _textColour = new TextStream.Colour(65f / 255, 52f / 255, 8f / 255, 1);
  private int _textX, _textY;
  private int _textW, _textH;
  private boolean _autoSize;
  
  public Label() {
    _textStream.insert(_textColour);
    _textStream.insert(_text);
  }
  
  @Override
  protected void setGUI(AbstractGUI gui) {
    super.setGUI(gui);
  }
  
  public void setTextStream(TextStream ts) {
    _textStream = ts;
  }
  
  public void setText(String text) {
    _font.events().addLoadHandler(() -> {
      _needsUpdate = true;
      _text.setText(text);
      _textW = _font.getW(text);
      _textH = _font.getH();
      
      if(_autoSize) {
        setWH(_textW + _padW * 2 + 1, _textH + _padH * 2);
      }
    });
  }

  public String getText() {
    return _text.getText();
  }
  
  public void setTextColour(float[] c) {
    _textColour.setColour(c);
  }
  
  public void setTextColour(float r, float g, float b, float a) {
    _textColour.setColour(r, g, b, a);
  }
  
  public float[] getTextColour() {
    return _textColour.getColour();
  }
  
  public boolean getAutoSize() {
    return _autoSize;
  }
  
  public void setAutoSize(boolean autoSize) {
    _autoSize = autoSize;
  }
  
  @Override
  protected void resize() {
    switch(_hAlign) {
      case ALIGN_LEFT:   _textX = _padW; break;
      case ALIGN_CENTER: _textX = (_w - _textW) / 2; break;
      case ALIGN_RIGHT:  _textX =  _w - _textW - _padW; break;
    }

    switch(_vAlign) {
      case ALIGN_TOP:    _textY = _padH; break;
      case ALIGN_MIDDLE: _textY = (_h - _textH) / 2; break;
      case ALIGN_BOTTOM: _textY =  _h - _textH - _padH; break;
    }
  }

  @Override
  public void draw() {
    if(drawBegin()) {
      _font.draw(_textX, _textY, _w - _padW * 2, _h - _padH * 2, _textStream);
    }

    drawEnd();
  }
}
