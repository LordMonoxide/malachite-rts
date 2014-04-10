package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.AbstractScalable;
import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.HAlign;
import malachite.engine.gfx.textures.TextureBuilder;

public class Button extends AbstractControl<ControlEvents> {
  private Font _font = FontBuilder.getInstance().getDefault();
  private String  _text;
  private float[] _textColour = {1, 1, 1, 1};
  private int     _textX, _textY;
  private int     _textW, _textH;
  
  private float[] _normalColour  = {0x3F / 255f, 0xCF / 255f, 0, 1};
  private float[] _hoverColour   = {0x46 / 255f, 0xE6 / 255f, 0, 1};
  private float[] _pressedColour = {0x28 / 255f, 0x82 / 255f, 0, 1};
  
  private boolean _pressed;
  private boolean _hovered;

  public Button() {
    super(
      InitFlags.WITH_DEFAULT_EVENTS,
      InitFlags.ACCEPTS_FOCUS,
      InitFlags.REGISTER
    );
    
    _events.addHoverHandler(new HoverHandler());
    _events.addMouseHandler(new MouseHandler());

    _hAlign = HAlign.ALIGN_CENTER;

    AbstractScalable s = AbstractContext.newScalable();
    s.setTexture(TextureBuilder.getInstance().getTexture("gui/button.png"));
    s.setSize(new float[] {2, 2, 2, 2},
        new float[] {2, 2, 2, 2},
        5, 5, 1
    );

    _background = s;
    
    setBackgroundColour(_normalColour);
  }
  
  @Override
  public void setBackground(AbstractDrawable d) {
    super.setBackground(d);
    _normalColour = d.getColour();
    
    for(int i = 0; i < 2; i++) {
      _hoverColour[i]   = _normalColour[i] + _normalColour[i] * (1 - _normalColour[i]);
      _pressedColour[i] = _normalColour[i] + _normalColour[i] * (_normalColour[i] - 1);
    }
    
    _hoverColour[3]   = _normalColour[3];
    _pressedColour[3] = _normalColour[3];
  }
  
  @Override
  public void setBackgroundColour(float r, float g, float b, float a) {
    setBackgroundColour(new float[] {r, g, b, a});
  }
  
  @Override
  public void setBackgroundColour(float[] c) {
    super.setBackgroundColour(c);
    _normalColour = c;
    
    for(int i = 0; i < 2; i++) {
      _hoverColour[i]   = _normalColour[i] + _normalColour[i] * (1 - _normalColour[i]);
      _pressedColour[i] = _normalColour[i] + _normalColour[i] * (_normalColour[i] - 1);
    }
    
    _hoverColour[3]   = _normalColour[3];
    _pressedColour[3] = _normalColour[3];
  }

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
    });
  }

  public String getText() {
    return _text;
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
  
  private class HoverHandler extends ControlEvents.Hover {
    @Override
    public void enter() {
      _hovered = true;

      if(!_pressed) {
        _background.setColour(_hoverColour);
        _background.createQuad();
      }
    }

    @Override
    public void leave() {
      _hovered = false;

      if(!_pressed) {
        _background.setColour(_normalColour);
        _background.createQuad();
      }
    }
  }
  
  private class MouseHandler extends ControlEvents.Mouse {
    @Override
    public void move(int x, int y, int button) {
      
    }

    @Override
    public void down(int x, int y, int button) {
      _pressed = true;
      _background.setColour(_pressedColour);
      _background.createQuad();
    }

    @Override
    public void up(int x, int y, int button) {
      _pressed = false;
      _background.setColour(_hovered ? _hoverColour : _normalColour);
      _background.createQuad();
    }
  }
}
