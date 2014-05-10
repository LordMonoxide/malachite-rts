package malachite.engine.gfx.gui.control;

import org.lwjgl.input.Keyboard;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.AbstractScalable;
import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.fonts.TextStream;
import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.HAlign;
import malachite.engine.gfx.textures.TextureBuilder;

public class Button extends AbstractControl<ControlEvents> {
  private Font _font = FontBuilder.getInstance().getDefault();
  private TextStream _textStream = new TextStream();
  private TextStream.Text  _text = new TextStream.Text();
  private TextStream.Colour _textColour = new TextStream.Colour(1, 1, 1, 1);
  private int _textX, _textY;
  private int _textW, _textH;
  
  private float[] _normalColour  = {0x3F / 255f, 0xCF / 255f, 0, 1};
  private float[] _hoverColour   = {0x46 / 255f, 0xE6 / 255f, 0, 1};
  private float[] _pressedColour = {0x28 / 255f, 0x82 / 255f, 0, 1};
  
  private float[] _normalBorder = {39f / 0xFF, 129f / 0xFF, 0f / 0xFF, 1};
  private float[] _hoverBorder  = { 7f / 0xFF,  85f / 0xFF, 0f / 0xFF, 1};
  
  private boolean _pressed;
  private boolean _hovered;

  public Button() {
    super(
      InitFlags.WITH_DEFAULT_EVENTS,
      InitFlags.ACCEPTS_FOCUS,
      InitFlags.REGISTER,
      InitFlags.WITH_BORDER
    );
    
    _textStream.insert(_textColour);
    _textStream.insert(_text);
    
    _events.addHoverHandler(new HoverHandler());
    _events.addMouseHandler(new MouseHandler());
    _events.addFocusHandler(new FocusHandler());
    _events.addKeyHandler(new ControlEvents.Key() {
      @Override public void text(char key) { }
      @Override public void down(int key, boolean repeat) {
        switch(key) {
          case Keyboard.KEY_RETURN:
          case Keyboard.KEY_SPACE:
            _events.raiseMouseDown(0, 0, -1);
            break;
        }
      }
      
      @Override public void up(int key) {
        switch(key) {
          case Keyboard.KEY_RETURN:
          case Keyboard.KEY_SPACE:
            _events.raiseMouseUp(0, 0, -1);
            _events.raiseClick();
            break;
        }
      }
    });

    _hAlign = HAlign.ALIGN_CENTER;

    AbstractScalable s = AbstractContext.newScalable();
    s.setTexture(TextureBuilder.getInstance().getTexture("gui/button.png"));
    s.setSize(new float[] {2, 2, 2, 2},
        new float[] {2, 2, 2, 2},
        5, 5, 1
    );

    _background = s;
    
    setBackgroundColour(_normalColour);
    _border.setColour(_normalBorder);
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
      _text.setText(text);
      _textW = _font.regular().getW(text);
      _textH = _font.regular().getH();
    });
  }

  public String getText() {
    return _text.getText();
  }

  public void setTextColour(float r, float g, float b, float a) {
    _textColour.setColour(r, g, b, a);
  }

  public float[] getTextColour() {
    return _textColour.getColour();
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
    
    _border.setXYWH(0, 0, _w, _h);
    _border.createBorder();
  }

  @Override
  public void draw() {
    if(drawBegin()) {
      _font.draw(_textX, _textY, _textStream);
    }

    drawEnd();
    drawNext();
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
  
  private class FocusHandler extends ControlEvents.Focus {
    @Override
    public void got() {
      _border.setColour(_hoverBorder);
      _border.createBorder();
    }

    @Override
    public void lost() {
      _border.setColour(_normalBorder);
      _border.createBorder();
    }
  }
}
