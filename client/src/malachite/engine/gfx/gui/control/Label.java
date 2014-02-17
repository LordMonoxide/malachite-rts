package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.gui.*;

public class Label extends AbstractControl<ControlEvents> {
  private Font _font = FontBuilder.getInstance().getDefault();
  private String _text;
  private float[] _textColour = {1, 1, 1, 1};
  private int _textX, _textY;
  private int _textW, _textH;

  public Label(AbstractGUI gui) {
    super(gui,
        InitFlags.WITH_BACKGROUND,
        InitFlags.WITH_BORDER
    );
  }

  public void setText(String text) {
    _font.events().addLoadHandler(() -> {
      _needsUpdate = true;
      _text = text;
      _textW = _font.getW(_text);
      _textH = _font.getH();
    });
  }

  public String getText() {
    return _text;
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
      _font.draw(_textX, _textY, _text, _textColour);
    }

    drawEnd();
  }
}
