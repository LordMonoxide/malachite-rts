package malachite.engine.gfx.gui.control;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.gui.ControlEvents;

public class Scrollbar extends AbstractControl<Scrollbar.Events> {
  private Button _inc, _dec;
  private int _min, _max, _val;
  
  public Scrollbar() {
    _events = new Events(this);
    
    _inc = new Button();
    _inc.setText(">");
    _inc.events().addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        setVal(_val + 1);
      }
    });
    
    _dec = new Button();
    _dec.setText("<");
    _dec.events().addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        setVal(_val - 1);
      }
    });
    
    controls().add(_inc);
    controls().add(_dec);
  }
  
  public int getMin() { return _min; }
  public int getMax() { return _max; }
  public int getVal() { return _val; }
  
  public void setMin(int min) { _min = Math.min(min, _max); setVal(_val); }
  public void setMax(int max) { _max = Math.max(max, _min); setVal(_val); }
  public void setVal(int val) {
    int v = Math.max(_min, Math.min(val, _max));
    if(v != _val) {
      _val = v;
      events().raiseChange(_val);
    }
  }
  
  @Override protected void resize() {
    if(_w >= _h) {
      _dec.setXYWH(0, 0, _w / 2, _h);
      _inc.setXYWH(_dec.getW(), 0, _w - _dec.getW(), _h);
    } else {
      _dec.setXYWH(0, 0, _w, _h / 2);
      _inc.setXYWH(0, _dec.getH(), _w, _h - _dec.getH());
    }
  }

  public static class Events extends ControlEvents {
    private Deque<Change> _change = new ConcurrentLinkedDeque<>();
    
    public void addChangeHandler(Change e) { _change.add(e); }
    
    protected Events(AbstractControl<? extends ControlEvents> c) {
      super(c);
    }
    
    public void raiseChange(int val) {
      for(Change e : _change) {
        e.setControl(_control);
        e.change(val);
      }
    }
    
    public static abstract class Change extends Event {
      protected void setControl(AbstractControl<? extends ControlEvents> control) { _control = control; }
      public abstract void change(int val);
    }
  }
}