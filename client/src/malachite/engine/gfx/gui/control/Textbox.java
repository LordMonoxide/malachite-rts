package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.AbstractScalable;
import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.textures.TextureBuilder;
import org.lwjgl.input.Keyboard;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Textbox extends AbstractControl<Textbox.Events> {
  private Font _font = FontBuilder.getInstance().getDefault();
  private String[] _text = new String[3];
  private String _textFull;
  private float[] _textColour = {65f / 255, 52f / 255, 8f / 255, 1};
  private int _textX, _textY;
  private int[] _textW = new int[3];
  private int _textH;
  private TextHAlign _textHAlign = TextHAlign.ALIGN_LEFT;
  private TextVAlign _textVAlign = TextVAlign.ALIGN_MIDDLE;

  private int _padW = 2;
  private int _padH = 2;

  private AbstractDrawable _caret;
  private double _caretPulse;

  public Textbox(AbstractGUI gui) {
    super(gui,
      InitFlags.ACCEPTS_FOCUS,
      InitFlags.REGISTER
    );

    _events = new Events(this);
    _events.addKeyHandler(new KeyHandler());

    _caret = AbstractContext.newDrawable();
    _caret.setColour(new float[] {_textColour[0], _textColour[1], _textColour[2], 1});

    _font.events().addLoadHandler(() -> {
      _caret.setWH(1, _font.getH());
      _caret.createQuad();
      resize();
    });

    AbstractScalable s = AbstractContext.newScalable();
    s.setTexture(TextureBuilder.getInstance().getTexture("gui/textbox.png"));
    s.setColour(new float[] {1, 1, 1, 1});
    s.setXY(-5, -5);
    s.setSize(new float[] {12, 12, 12, 12},
        new float[] {12, 12, 12, 12},
        25, 25, 1
    );

    _background = s;
  }

  public void setText(String text) {
    if(_font.loaded()) {
      updateText(text, null, null);
    } else {
      _font.events().addLoadHandler(() -> {
        updateText(text, null, null);
      });
    }
  }

  private void updateText(String... text) {
    _text[0] = text[0];
    _text[1] = text[1];
    _text[2] = text[2];
    resize();
  }

  public String getText() {
    return _textFull;
  }

  public void setTextHAlign(TextHAlign align) {
    _textHAlign = align;
  }

  public TextHAlign getTextHAlign() {
    return _textHAlign;
  }

  public void setTextVAlign(TextVAlign align) {
    _textVAlign = align;
  }

  public TextVAlign getTextVAlign() {
    return _textVAlign;
  }

  @Override
  protected void resize() {
    _textFull = "";
    for(int i = 0; i < _text.length; i++) {
      if(_text[i] != null) {
        _textFull += _text[i];
      }
    }

    _textW[0] = _font.getW(_text[0]);
    _textW[1] = _font.getW(_text[1]);
    _textW[2] = _font.getW(_text[2]);

    _textH = _font.getH();
    _caret.setX(_textW[0]);

    int totalW = _textW[0] + _textW[1] + _textW[2];

    switch(_textHAlign) {
      case ALIGN_LEFT:   _textX = _padW; break;
      case ALIGN_CENTER: _textX = (_w - totalW) / 2; break;
      case ALIGN_RIGHT:  _textX =  _w - totalW - _padW; break;
    }

    switch(_textVAlign) {
      case ALIGN_TOP:    _textY = _padH; break;
      case ALIGN_MIDDLE: _textY = (_h - _textH) / 2; break;
      case ALIGN_BOTTOM: _textY =  _h - _textH - _padH; break;
    }
  }

  @Override
  public void draw() {
    if(drawBegin()) {
      _matrix.push();
      _matrix.translate(_textX, _textY);

      _font.draw(0, 0, _textFull, _textColour);

      if(_focus) {
        _caret.draw();
      }

      _matrix.pop();
    }

    drawEnd();
  }

  @Override
  public void logic() {
    
  }

  public enum TextHAlign {
    ALIGN_LEFT,
    ALIGN_CENTER,
    ALIGN_RIGHT
  }

  public enum TextVAlign {
    ALIGN_MIDDLE,
    ALIGN_TOP,
    ALIGN_BOTTOM
  }

  private class KeyHandler extends ControlEvents.Key {
    @Override
    public void down(int key) {
      switch(key) {
        case Keyboard.KEY_BACK:
          if(_text[1] != null) {
            updateText(_text[0], null, _text[2]);
          } else {
            if(_text[0] != null && !_text[0].isEmpty()) {
              updateText(_text[0].substring(0, _text[0].length() - 1), _text[1], _text[2]);
            }
          }

          break;
      }
    }

    @Override
    public void up(int key) {

    }

    @Override
    public void text(char key) {
      if(_text[0] != null) {
        updateText(_text[0] + key, null, _text[2]);
      } else {
        updateText(Character.toString(key), null, _text[2]);
      }

      events().raiseChange();
    }
  }

  public static class Events extends ControlEvents {
    private Deque<Change> _change = new ConcurrentLinkedDeque<>();

    public void addChangeHandler(Change e) { _change.add(e); }

    protected Events(AbstractControl<? extends ControlEvents> c) {
      super(c);
    }

    public void raiseChange() {
      for(Change e : _change) {
        e.setControl(_control);
        e.change();
      }
    }

    public static abstract class Change extends Event {
      protected void setControl(AbstractControl<? extends ControlEvents> control) { _control = control; }
      public abstract void change();
    }
  }
}
