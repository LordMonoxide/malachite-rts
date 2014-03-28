package malachite.engine.gfx.gui;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ControlEvents {
  private Deque<Draw>       _draw       = new ConcurrentLinkedDeque<>();
  private Deque<Mouse>      _mouse      = new ConcurrentLinkedDeque<>();
  private Deque<Key>        _key        = new ConcurrentLinkedDeque<>();
  private Deque<Click>      _click      = new ConcurrentLinkedDeque<>();
  private Deque<Scroll>     _scroll     = new ConcurrentLinkedDeque<>();
  private Deque<Hover>      _hover      = new ConcurrentLinkedDeque<>();
  private Deque<Focus>      _focus      = new ConcurrentLinkedDeque<>();
  private Deque<Resize>     _resize     = new ConcurrentLinkedDeque<>();
  private Deque<Visibility> _visibility = new ConcurrentLinkedDeque<>();

  public void addDrawHandler      (Draw   e)     { _draw  .add(e); }
  public void addMouseHandler     (Mouse  e)     { _mouse .add(e); }
  public void addKeyHandler       (Key    e)     { _key   .add(e); }
  public void addClickHandler     (Click  e)     { _click .add(e); }
  public void addScrollHandler    (Scroll e)     { _scroll.add(e); }
  public void addHoverHandler     (Hover  e)     { _hover .add(e); }
  public void addFocusHandler     (Focus  e)     { _focus .add(e); }
  public void addResizeHandler    (Resize e)     { _resize.add(e); }
  public void addVisibilityHandler(Visibility e) { _visibility.add(e); }

  protected AbstractControl<? extends ControlEvents> _control;

  protected ControlEvents(AbstractControl<? extends ControlEvents> c) {
    _control = c;
  }

  public void raiseDraw() {
    for(Draw e : _draw) {
      e._control = _control;
      e.draw();
    }
  }

  public void raiseMouseDown(int x, int y, int button) {
    for(Mouse e : _mouse) {
      e._control = _control;
      e.down(x, y, button);
    }
  }

  public void raiseMouseUp(int x, int y, int button) {
    for(Mouse e : _mouse) {
      e._control = _control;
      e.up(x, y, button);
    }
  }

  public void raiseMouseMove(int x, int y, int button) {
    for(Mouse e : _mouse) {
      e._control = _control;
      e.move(x, y, button);
    }
  }

  public void raiseMouseScroll(int delta) {
    for(Scroll e : _scroll) {
      e._control = _control;
      e.scroll(delta);
    }
  }

  public void raiseHoverEnter() {
    for(Hover e : _hover) {
      e._control = _control;
      e.enter();
    }
  }

  public void raiseHoverLeave() {
    for(Hover e : _hover) {
      e._control = _control;
      e.leave();
    }
  }

  public void raiseClick() {
    for(Click e : _click) {
      e._control = _control;
      e.click();
    }
  }

  public void raiseClickDbl() {
    for(Click e : _click) {
      e._control = _control;
      e.clickDbl();
    }
  }

  public void raiseKeyDown(int key) {
    for(Key e : _key) {
      e._control = _control;
      e.down(key);
    }
  }

  public void raiseKeyUp(int key) {
    for(Key e : _key) {
      e._control = _control;
      e.up(key);
    }
  }

  public void raiseKeyText(char key) {
    for(Key e : _key) {
      e._control = _control;
      e.text(key);
    }
  }

  public void raiseFocusGot() {
    for(Focus e : _focus) {
      e._control = _control;
      e.got();
    }
  }

  public void raiseFocusLost() {
    for(Focus e : _focus) {
      e._control = _control;
      e.lost();
    }
  }

  public void raiseResize() {
    for(Resize e : _resize) {
      e._control = _control;
      e.resize();
    }
  }

  public boolean raiseVisibilityShow() {
    for(Visibility e : _visibility) {
      e._control = _control;
      if(e.show()) {
        return true;
      }
    }
    
    return false;
  }

  public boolean raiseVisibilityHide() {
    for(Visibility e : _visibility) {
      e._control = _control;
      if(e.hide()) {
        return true;
      }
    }
    
    return false;
  }

  public static class Event {
    protected AbstractControl<? extends ControlEvents> _control;
    public AbstractControl<? extends ControlEvents> control() { return _control; }
  }

  public static abstract class Draw extends Event {
    public abstract void draw();
  }

  public static abstract class Mouse extends Event {
    public abstract void move(int x, int y, int button);
    public abstract void down(int x, int y, int button);
    public abstract void up  (int x, int y, int button);
  }

  public static abstract class Key extends Event {
    public abstract void down(int key);
    public abstract void up  (int key);
    public abstract void text(char key);
  }

  public static abstract class Click extends Event {
    public abstract void click();
    public abstract void clickDbl();
  }

  public static abstract class Scroll extends Event {
    public abstract void scroll(int delta);
  }

  public static abstract class Hover extends Event {
    public abstract void enter();
    public abstract void leave();
  }

  public static abstract class Focus extends Event {
    public abstract void got();
    public abstract void lost();
  }

  public static abstract class Resize extends Event {
    public abstract void resize();
  }
  
  public static abstract class Visibility extends Event {
    public abstract boolean show();
    public abstract boolean hide();
  }
}
