package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.gui.*;

public class Label extends AbstractControl<ControlEvents> {
  private Font _font = FontBuilder.getInstance().getDefault();
  private String _text;
  private float[] _textColour = {65f / 255, 52f / 255, 8f / 255, 1};
  private int _textX, _textY;
  private int _textW, _textH;
  private boolean _autoSize;

  @Override
  protected void setGUI(AbstractGUI gui) {
    super.setGUI(gui);
  }

  public void setText(String text) {
    _font.events().addLoadHandler(() -> {
      _needsUpdate = true;
      _text = text;
      _textW = _font.getW(_text);
      _textH = _font.getH();
      
      if(_autoSize) {
        setWH(_textW + _padW * 2 + 1, _textH + _padH * 2);
      }
    });
  }

  public String getText() {
    return _text;
  }
  
  public void setTextColour(float[] c) {
    _textColour = c;
  }
  
  public void setTextColour(float r, float g, float b, float a) {
    _textColour[0] = r;
    _textColour[1] = g;
    _textColour[2] = b;
    _textColour[3] = a;
  }
  
  public float[] getTextColour() {
    return _textColour;
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
      _font.draw(_textX, _textY, _w - _padW * 2, _h - _padH * 2, _text, _textColour);
    }

    drawEnd();
  }
}
