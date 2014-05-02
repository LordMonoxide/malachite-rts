package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractScalable;
import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.ControlList;
import malachite.engine.gfx.textures.TextureBuilder;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Window extends AbstractControl<Window.Events> {
  private Image _title;
  private Label _text;
  private Button _close;
  private Image _content;

  public Window() {
    super(InitFlags.REGISTER);

    _events = new Events(this);

    TextureBuilder t = TextureBuilder.getInstance();

    AbstractScalable s = AbstractContext.newScalable();
    s.setTexture(t.getTexture("gui/background.png"));
    s.setSize(
        new float[] {2, 2, 2, 2},
        new float[] {2, 2, 2, 2},
        260, 260, 256
    );

    _background = s;

    s = AbstractContext.newScalable();
    s.setTexture(t.getTexture("gui/title.png"));
    s.setSize(
        new float[] {2, 10, 2, 10},
        new float[] {2, 10, 2, 10},
        5, 21, 1
    );

    _title = new Image(InitFlags.WITH_DEFAULT_EVENTS, InitFlags.REGISTER);
    _title.setBackground(s);
    _title.setY(-20);
    _title.setH(21);
    _title.events().addMouseHandler(new ControlEvents.Mouse() {
      int _x, _y;

      @Override
      public void move(int x, int y, int button) {
        if(button == 0) {
          setXY(getX() + x - _x, getY() + y - _y);
        }
      }

      @Override
      public void down(int x, int y, int button) {
        _x = x;
        _y = y;
      }

      @Override public void up(int x, int y, int button) { }
    });

    _text = new Label();
    _text.setX(4);
    _text.setTextColour(1, 1, 1, 1);
    _text.setAutoSize(true);
    _title.controls().add(_text);

    _close = new Button();
    _close.setBackground(AbstractContext.newDrawable());
    _close.getBackground().setTexture(t.getTexture("gui/close.png"));
    _close.getBackground().setTWH(13, 13);
    _close.setBackgroundColour(new float[] {0.8f, 0.8f, 0.8f, 1});
    _close.setY(4);
    _close.setWH(13, 13);
    _close.events().addClickHandler(new ControlEvents.Click() {
      @Override public void click() {
        events().raiseClose();
      }

      @Override public void clickDbl() { }
    });

    _title.controls().add(_close);

    s = AbstractContext.newScalable();
    s.setTexture(t.getTexture("gui/foreground.png"));
    s.setXY(-7, -7);
    s.setSize(
        new float[] {21, 21, 21, 21},
        new float[] {21, 21, 21, 21},
        43, 43, 1
    );

    _content = new Image();
    _content.setBackground(s);
    _content.setXY(8, 8);

    super.controls().add(_title);
    super.controls().add(_content);
  }

  @Override
  protected void setGUI(AbstractGUI gui) {
    super.setGUI(gui);
    _title.setGUI(gui);
    _close.setGUI(gui);
    _content.setGUI(gui);
  }

  @Override
  public ControlList controls() {
    return _content.controls();
  }

  @Override
  protected void resize() {
    _title.setW(_w);
    _close.setX(_w - _close.getW() - _close.getY());
    _content.setWH(
      _w - _content.getX() * 2,
      _h - _content.getY() * 2
    );
  }
  
  public String getText() {
    return _text.getText();
  }
  
  public void setText(String text) {
    _text.setText(text);
    _text.setY((_title.getH() - _text.getH()) / 2);
  }
  
  public int getContentW() {
    return _content.getW();
  }
  
  public int getContentH() {
    return _content.getH();
  }
  
  public void showCloseButton() {
    _close.show();
  }
  
  public void hideCloseButton() {
    _close.hide();
  }
  
  public static class Events extends ControlEvents {
    private Deque<Close> _close = new ConcurrentLinkedDeque<>();

    public void addCloseHandler(Close e) { _close.add(e); }

    protected Events(AbstractControl<? extends ControlEvents> c) {
      super(c);
    }

    public void raiseClose() {
      for(Close e : _close) {
        e.setControl(_control);
        e.close();
      }
    }

    public static abstract class Close extends Event {
      protected void setControl(AbstractControl<? extends ControlEvents> control) { _control = control; }
      public abstract void close();
    }
  }
}
