package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.textures.Texture;
import malachite.engine.gfx.textures.TextureBuilder;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Check extends AbstractControl<Check.Events> {
  private Font _font = FontBuilder.getInstance().getDefault();
  private String _text;
  private float[] _textColour = {65f / 255, 52f / 255, 8f / 255, 1};
  private int _textX, _textY;
  private int _textW, _textH;
  
  private float[] _normalBorder = {160f / 0xFF, 147f / 0xFF, 111f / 0xFF, 1};
  private float[] _hoverBorder  = { 11f / 0xFF, 126f / 0xFF,   0f / 0xFF, 1};
  
  private TextureBuilder _textures = TextureBuilder.getInstance();
  private Texture _textureNormal = _textures.getTexture("gui/check.png");
  private Texture _textureHover = _textures.getTexture("gui/check_hover.png");

  private AbstractDrawable _bg, _check;
  private boolean _checked;

  public Check() {
    super(
        InitFlags.ACCEPTS_FOCUS,
        InitFlags.REGISTER,
        InitFlags.WITH_BORDER
    );

    _events = new Events(this);
    _events.addFocusHandler(new FocusHandler());
    _events.addHoverHandler(new HoverHandler());
    _events.addClickHandler(new ControlEvents.Click() {
      @Override public void click() {
        toggle();
      }

      @Override public void clickDbl() { }
    });

    _padW = 0;
    _padH = 0;
    
    _border.setColour(_normalBorder);
    
    _bg = AbstractContext.newDrawable();
    _bg.setTexture(_textureNormal);
    
    Texture check = _textures.getTexture("gui/check_check.png");
    _check = AbstractContext.newDrawable();
    _check.setTexture(check);
    _check.setVisible(false);

    _textureNormal.events().addLoadHandler(new Texture.Events.Event() {
      @Override
      public void run() {
        _bg.setTWH(14, 14);
        _bg.setWH(14, 14);
        _bg.createQuad();
        _needsUpdate = true;
      }
    });

    check.events().addLoadHandler(new Texture.Events.Event() {
      @Override
      public void run() {
        _check.setTWH(14, 14);
        _check.setWH(14, 14);
        _check.createQuad();
        resize();
      }
    });
  }

  @Override
  protected void setGUI(AbstractGUI gui) {
    super.setGUI(gui);
  }

  public void toggle() {
    setChecked(!_checked);
  }

  public void check() {
    setChecked(true);
  }

  public void uncheck() {
    setChecked(false);
  }

  public void setChecked(boolean checked) {
    if(checked == _checked) { return; }
    _checked = checked;
    _check.setVisible(_checked);

    events().raiseChange(_checked);
  }

  public boolean isChecked() {
    return _checked;
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
    if(_w < _bg.getW() + _textW + 2) {
      setW((int)_bg.getW() + _textW + 2);
    }

    if(_h < _bg.getH()) {
      setH((int)_bg.getH());
    }

    switch(_hAlign) {
      case ALIGN_LEFT:   _bg.setX(_padW); break;
      case ALIGN_CENTER: _bg.setX(_w - (_textW + _bg.getW()) / 2);    break;
      case ALIGN_RIGHT:  _bg.setX(_w -  _textW - _bg.getW() - _padW); break;
    }

    switch(_vAlign) {
      case ALIGN_TOP:    _bg.setY(_padH); _textY = _padH; break;
      case ALIGN_MIDDLE: _bg.setY((_h - _bg.getH()) / 2); _textY = (_h - _textH) / 2;    break;
      case ALIGN_BOTTOM: _bg.setY( _h - _bg.getH());      _textY =  _h - _textH - _padH; break;
    }
    
    _check.setXY(_bg.getX(), _bg.getY());
    _border.setXYWH(_bg.getX() + 1, _bg.getY() + 1, 12, 12);
    _border.createBorder();

    _textX = (int)(_bg.getX() + _bg.getW()) + 2;
  }

  @Override
  public void draw() {
    if(drawBegin()) {
      _bg.draw();
      _check.draw();
      _font.draw(_textX, _textY, _text, _textColour);
    }

    drawEnd();
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
  
  private class HoverHandler extends ControlEvents.Hover {
    @Override
    public void enter() {
      _bg.setTexture(_textureHover);
    }

    @Override
    public void leave() {
      _bg.setTexture(_textureNormal);
    }
  }

  public static class Events extends ControlEvents {
    private Deque<Change> _change = new ConcurrentLinkedDeque<>();

    public void addChangeHandler(Change e) { _change.add(e); }

    protected Events(AbstractControl<? extends ControlEvents> c) {
      super(c);
    }

    public void raiseChange(boolean checked) {
      for(Change e : _change) {
        e.setControl(_control);
        e.change(checked);
      }
    }

    public static abstract class Change extends Event {
      protected void setControl(AbstractControl<? extends ControlEvents> control) { _control = control; }
      public abstract void change(boolean checked);
    }
  }
}
