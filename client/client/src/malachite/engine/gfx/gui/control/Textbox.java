package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.AbstractScalable;
import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.fonts.TextStream;
import malachite.engine.gfx.gui.*;
import malachite.engine.gfx.textures.Texture;
import malachite.engine.gfx.textures.TextureBuilder;
import malachite.engine.util.Time;

import org.lwjgl.input.Keyboard;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Textbox extends AbstractControl<Textbox.Events> {
  private static final String EMPTY = ""; //$NON-NLS-1$
  
  private Font _font = FontBuilder.getInstance().getDefault();
  private TextStream _textPlaceholderStream = new TextStream();
  private TextStream.Text _textPlaceholder = new TextStream.Text();
  private TextStream.Colour _textPlaceholderColour = new TextStream.Colour(65f / 255, 52f / 255, 8f / 255, 0.5f);
  private String[] _text = {EMPTY, EMPTY, EMPTY};
  private TextStream _textStream = new TextStream();
  private TextStream.Text _textFull = new TextStream.Text();
  private TextStream.Colour _textColour = new TextStream.Colour(65f / 255, 52f / 255, 8f / 255, 1);
  private int _mask;
  private int _textX, _textY;
  private int[] _textW = new int[3];
  private int _textH;
  
  private TextureBuilder _textures = TextureBuilder.getInstance();
  private Texture _textureNormal = _textures.getTexture("gui/textbox.png");
  private Texture _textureHover = _textures.getTexture("gui/textbox_hover.png");
  
  private float[] _normalBorder = {160f / 0xFF, 147f / 0xFF, 111f / 0xFF, 1};
  private float[] _hoverBorder  = { 11f / 0xFF, 126f / 0xFF,   0f / 0xFF, 1};

  private AbstractDrawable _caret;
  private double _caretPulse;
  
  private boolean _shift;
  private int _selectDirection;

  public Textbox() {
    super(
      InitFlags.ACCEPTS_FOCUS,
      InitFlags.REGISTER,
      InitFlags.WITH_BORDER
    );
    
    _textPlaceholderStream.insert(_textPlaceholderColour);
    _textPlaceholderStream.insert(_textPlaceholder);
    _textStream.insert(_textColour);
    _textStream.insert(_textFull);
    
    _events = new Events(this);
    _events.addKeyHandler(new KeyHandler());
    _events.addFocusHandler(new FocusHandler());
    _events.addHoverHandler(new HoverHandler());

    _caret = AbstractContext.newDrawable();
    _caret.setColour(new float[] {_textColour.getColour()[0], _textColour.getColour()[1], _textColour.getColour()[2], 1});

    _font.events().addLoadHandler(() -> {
      _caret.setWH(1, _font.regular().getH());
      _caret.createQuad();
      resize();
    });
    
    _border.setColour(_normalBorder);

    AbstractScalable s = AbstractContext.newScalable();
    _background = s;
    
    s.setTexture(_textureNormal);
    s.setXY(-5, -5);
    s.setSize(new float[] {12, 12, 12, 12},
        new float[] {12, 12, 12, 12},
        25, 25, 1
    );
  }

  @Override
  protected void setGUI(AbstractGUI gui) {
    super.setGUI(gui);
  }

  public void setText(String text) {
    if(_font.loaded()) {
      updateText(text == null ? EMPTY : text, EMPTY, EMPTY);
      _selectDirection = 0;
    } else {
      _font.events().addLoadHandler(() -> {
        updateText(text == null ? EMPTY : text, EMPTY, EMPTY);
        _selectDirection = 0;
      });
    }
  }

  private void updateText(String... text) {
    _text[0] = text[0];
    _text[1] = text[1];
    _text[2] = text[2];
    resize();
    resetCaretAlpha();
  }

  public String getText() {
    return _textFull.getText();
  }

  public void setTextPlaceholder(String text) {
    _textPlaceholder.setText(text);
  }

  public String getTextPlaceholder() {
    return _textPlaceholder.getText();
  }

  public void setMasked(boolean masked) {
    _mask = masked ? 0x2022 : 0;
  }

  public boolean getMasked() {
    return _mask != 0;
  }

  @Override
  protected void resize() {
    String temp = EMPTY;
    for(int i = 0; i < _text.length; i++) {
      if(!_text[i].isEmpty()) {
        temp += _text[i];
      }
    }
    
    _textFull.setText(temp);

    _textW[0] = _font.regular().getW(_text[0], _mask);
    _textW[1] = _font.regular().getW(_text[1], _mask);
    _textW[2] = _font.regular().getW(_text[2], _mask);

    _textH = _font.regular().getH();
    _caret.setX(_textW[0]);
    
    if(_textW[1] == 0) {
      _caret.setW(1);
    } else {
      _caret.setW(_textW[1]);
    }
    
    _caret.createQuad();

    int totalW = _textW[0] + _textW[1] + _textW[2];

    switch(_hAlign) {
      case ALIGN_LEFT:   _textX = _padW; break;
      case ALIGN_CENTER: _textX = (_w - totalW) / 2; break;
      case ALIGN_RIGHT:  _textX =  _w - totalW - _padW; break;
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
      _matrix.push();
      _matrix.translate(_textX, _textY);

      if(!_textFull.getText().isEmpty()) {
        _font.draw(0, 0, _textStream, _mask);
      } else {
        if(_textPlaceholder.getText() != null && !_textPlaceholder.getText().isEmpty()) {
          _font.draw(0, 0, _textPlaceholderStream);
        }
      }

      if(_focus) {
        _caret.draw();
      }

      _matrix.pop();
    }

    drawEnd();
    drawNext();
  }

  @Override
  public void logic() {
    decrementCaretAlpha();

    if(_caretPulse <= Time.get()) {
      resetCaretAlpha();
    }
  }

  private void decrementCaretAlpha() {
    if(_caret.getColour()[3] > 0) {
      _caret.getColour()[3] -= 0.01;
    } else {
      _caret.getColour()[3] = 0;
    }
  }

  private void resetCaretAlpha() {
    _caret.getColour()[3] = 1;
    _caretPulse = Time.get() + Time.MSToTicks(1000);
  }

  private class KeyHandler extends ControlEvents.Key {
    @Override
    public void down(int key, boolean repeat) {
      switch(key) {
        case Keyboard.KEY_LSHIFT:
        case Keyboard.KEY_RSHIFT:
          _shift = true;
          break;
          
        case Keyboard.KEY_BACK:
          if(!_text[1].isEmpty()) {
            updateText(_text[0], EMPTY, _text[2]);
          } else {
            if(!_text[0].isEmpty()) {
              updateText(_text[0].substring(0, _text[0].length() - 1), EMPTY, _text[2]);
            }
          }

          _selectDirection = 0;
          break;
          
        case Keyboard.KEY_DELETE:
          if(!_text[1].isEmpty()) {
            updateText(_text[0], EMPTY, _text[2]);
          } else {
            if(!_text[2].isEmpty()) {
              updateText(_text[0], EMPTY, _text[2].substring(1));
            }
          }
          
          _selectDirection = 0;
          break;
          
        case Keyboard.KEY_HOME:
          updateText(EMPTY, EMPTY, _text[0] + _text[1] + _text[2]);
          _selectDirection = 0;
          break;
          
        case Keyboard.KEY_END:
          updateText(_text[0] + _text[1] + _text[2], EMPTY, EMPTY);
          _selectDirection = 0;
          break;
          
        case Keyboard.KEY_LEFT:
          if(!_shift) {
            if(_selectDirection == 0) {
              String[] s = new String[] {_text[0] + _text[1], _text[2]};
              if(!s[0].isEmpty()) {
                s[1] = s[0].substring(s[0].length() - 1) + s[1];
                s[0] = s[0].substring(0, s[0].length() - 1);
                updateText(s[0], EMPTY, s[1]);
              }
            } else {
              updateText(_text[0], EMPTY, _text[1] + _text[2]);
              _selectDirection = 0;
            }
          } else {
            if(!_text[0].isEmpty()) {
              switch(_selectDirection) {
                case 0:
                  _selectDirection = -1;
                  // Yes, fall-through is intended
                  
                case -1:
                  updateText(_text[0].substring(0, _text[0].length() - 1), _text[0].substring(_text[0].length() - 1) + _text[1], _text[2]);
                  break;
                  
                case 1:
                  updateText(_text[0], _text[1].substring(0, _text[1].length() - 1), _text[1].substring(_text[1].length() - 1) + _text[2]);
                  
                  if(_text[1].isEmpty()) {
                    _selectDirection = 0;
                  }
                  
                  break;
              }
            }
          }
          
          break;
          
        case Keyboard.KEY_RIGHT:
          if(!_shift) {
            if(_selectDirection == 0) {
              String[] s = new String[] {_text[0] + _text[1], _text[2]};
              if(!s[1].isEmpty()) {
                s[0] += s[1].substring(0, 1);
                s[1] = s[1].substring(1);
                updateText(s[0], EMPTY, s[1]);
              }
            } else {
              updateText(_text[0] + _text[1], EMPTY, _text[2]);
              _selectDirection = 0;
            }
          } else {
            if(!_text[2].isEmpty()) {
              switch(_selectDirection) {
                case 0:
                  _selectDirection = 1;
                  // Yes, fall-through is intended
                  
                case 1:
                  updateText(_text[0], _text[1] + _text[2].substring(0, 1), _text[2].substring(1));
                  break;
                  
                case -1:
                  updateText(_text[0] + _text[1].substring(0, 1), _text[1].substring(1), _text[2]);
                  
                  if(_text[1].isEmpty()) {
                    _selectDirection = 0;
                  }
                  
                  break;
              }
            }
          }
          
          break;
      }
    }

    @Override
    public void up(int key) {
      switch(key) {
        case Keyboard.KEY_LSHIFT:
        case Keyboard.KEY_RSHIFT:
          _shift = false;
          break;
      }
    }

    @Override
    public void text(char key) {
      updateText(_text[0] + key, EMPTY, _text[2]);
      _selectDirection = 0;
      events().raiseChange();
    }
  }
  
  private class FocusHandler extends ControlEvents.Focus {
    @Override
    public void got() {
      _border.setColour(_hoverBorder);
      _border.createBorder();
      resetCaretAlpha();
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
      _background.setTexture(_textureHover);
    }

    @Override
    public void leave() {
      _background.setTexture(_textureNormal);
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
