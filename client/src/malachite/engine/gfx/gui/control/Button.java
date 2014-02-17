package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractScalable;
import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.HAlign;
import malachite.engine.gfx.textures.TextureBuilder;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Button extends AbstractControl<ControlEvents> {
  private Font _font = FontBuilder.getInstance().getDefault();
  private String  _text;
  private float[] _textColour = {1, 1, 1, 1};
  private int     _textX, _textY;
  private int     _textW, _textH;

  public Button(AbstractGUI gui) {
    super(gui,
      InitFlags.WITH_DEFAULT_EVENTS,
      InitFlags.ACCEPTS_FOCUS,
      InitFlags.REGISTER
    );

    _hAlign = HAlign.ALIGN_CENTER;

    AbstractScalable s = AbstractContext.newScalable();
    s.setTexture(TextureBuilder.getInstance().getTexture("gui/button.png"));
    s.setColour(new float[] {0x3F / 255f, 0xCF / 255f, 0, 1});
    s.setSize(new float[] {2, 2, 2, 2},
        new float[] {2, 2, 2, 2},
        5, 5, 1
    );

    _background = s;
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
