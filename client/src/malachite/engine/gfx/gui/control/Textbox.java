package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Textbox extends AbstractControl<Textbox.Events> {
  private Font _font = FontBuilder.getInstance().getDefault();
  private String _text;
  private float[] _textColour = {1, 1, 1, 1};
  private int _textX, _textY;
  private int _textW, _textH;
  private TextHAlign _textHAlign = TextHAlign.ALIGN_LEFT;
  private TextVAlign _textVAlign = TextVAlign.ALIGN_MIDDLE;

  private AbstractDrawable _caret;

  public Textbox(AbstractGUI gui) {
    super(gui,
      InitFlags.WITH_BACKGROUND,
      InitFlags.WITH_BORDER,
      InitFlags.ACCEPTS_FOCUS,
      InitFlags.REGISTER
    );

    _caret = AbstractContext.newDrawable();
    _caret.setColour(new float[] {1, 1, 1, 1});
  }

  public void setText(String text) {
    _font.events().addLoadHandler(() -> {
      _needsUpdate = true;
      _text = text;
      _textW = _font.getW(_text);
      _textH = _font.getH();

      _caret.setWH(1, _font.getH());
      _caret.createQuad();
    });
  }

  public String getText() {
    return _text;
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
    switch(_textHAlign) {
      case ALIGN_LEFT:   _textX = 0; break;
      case ALIGN_CENTER: _textX = (_w - _textW) / 2; break;
      case ALIGN_RIGHT:  _textX =  _w - _textW; break;
    }

    switch(_textVAlign) {
      case ALIGN_TOP:    _textY = 0; break;
      case ALIGN_MIDDLE: _textY = (_h - _textH) / 2; break;
      case ALIGN_BOTTOM: _textY =  _h - _textH; break;
    }
  }

  @Override
  public void draw() {
    if(drawBegin()) {
      _font.draw(_textX, _textY, _text, _textColour);

      if(_focus) {
        _caret.draw();
      }
    }

    drawEnd();
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

    }

    @Override
    public void up(int key) {

    }

    @Override
    public void text(char key) {

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
