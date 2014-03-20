package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.AbstractControl;

public class List<T> extends AbstractControl<ControlEvents> {
  private Frame _inner;
  
  public List() {
    super(InitFlags.REGISTER);
    
    _inner = new Frame();
  }
  
  public Item add(String name, T data) {
    Item i = new Item(name, data);
    i.setWH(_w, 20);
    _inner.controls().add(i);
    return i;
  }
  
  @Override
  protected void resize() {
    _inner.setWH(_w, _h);
    
    AbstractControl<?> c = _inner.controls().first();
    while(c != null) {
      c.setW(_w);
      c = c.controlNext();
    }
  }
  
  @Override
  public void draw() {
    if(drawBegin()) {
      _inner.draw();
    }
    
    drawEnd();
  }
  
  public class Item extends AbstractControl<ControlEvents> {
    private Label  _text;
    private T      _data;
    
    private Item(String text, T data) {
      _text = new Label();
      _text.setText(text);
      _text.setX(4);
      _data = data;
      
      controls().add(_text);
    }
    
    public String getText() { return _text.getText(); }
    public T      getData() { return _data; }
    
    public void setText(String text) { _text.setText(text); }
    public void setData(T      data) { _data = data; }
    
    @Override
    protected void resize() {
      _text.setY(_h / 2);
    }
  }
}