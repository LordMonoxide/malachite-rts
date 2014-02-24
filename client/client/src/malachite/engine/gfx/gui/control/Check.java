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

  private AbstractDrawable _check;
  private Texture _check_unchecked, _check_checked;
  private boolean _checked;

  public Check() {
    super(
        InitFlags.ACCEPTS_FOCUS,
        InitFlags.REGISTER
    );

    _events = new Events(this);
    _events.addClickHandler(new ControlEvents.Click() {
      @Override public void click() {
        toggle();
      }

      @Override public void clickDbl() { }
    });

    _padW = 0;
    _padH = 0;

    TextureBuilder t = TextureBuilder.getInstance();
    _check_checked = t.getTexture("gui/check_checked.png");
    _check_unchecked = t.getTexture("gui/check_unchecked.png");

    _check = AbstractContext.newDrawable();
    _check.setTexture(_check_unchecked);

    _check_unchecked.events().addLoadHandler(new Texture.Events.Event() {
      @Override
      public void run() {
        _check.setTWH(14, 14);
        _check.setWH(14, 14);
        _check.createQuad();
        _needsUpdate = true;
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

    if(_checked) {
      _check.setTexture(_check_checked);
      _check.createQuad();
    } else {
      _check.setTexture(_check_unchecked);
      _check.createQuad();
    }

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
    if(_w < _check.getW() + _textW + 2) {
      setW((int)_check.getW() + _textW + 2);
    }

    if(_h < _check.getH()) {
      setH((int)_check.getH());
    }

    switch(_hAlign) {
      case ALIGN_LEFT:   _check.setX(_padW); break;
      case ALIGN_CENTER: _check.setX(_w - (_textW + _check.getW()) / 2); break;
      case ALIGN_RIGHT:  _check.setX(_w -  _textW - _check.getW() - _padW); break;
    }

    switch(_vAlign) {
      case ALIGN_TOP:    _textY = _padH; break;
      case ALIGN_MIDDLE: _textY = (_h - _textH) / 2; break;
      case ALIGN_BOTTOM: _textY =  _h - _textH - _padH; break;
    }

    _textX = (int)(_check.getX() + _check.getW()) + 2;
    _check.setY((int)(_textY + (_textH - _check.getH()) / 2));
  }

  @Override
  public void draw() {
    if(drawBegin()) {
      _check.draw();
      _font.draw(_textX, _textY, _text, _textColour);
    }

    drawEnd();
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
