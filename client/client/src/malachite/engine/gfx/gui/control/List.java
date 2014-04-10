package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractScalable;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.textures.Texture;
import malachite.engine.gfx.textures.TextureBuilder;

public class List<T> extends AbstractControl<ControlEvents> {
  private Texture _itemBG = TextureBuilder.getInstance().getTexture("gui/listitem.png");
  
  private Frame _inner;
  
  private float[] _normalBorder = {160f / 0xFF, 147f / 0xFF, 111f / 0xFF, 1};
  private float[] _hoverBorder  = { 11f / 0xFF, 126f / 0xFF,   0f / 0xFF, 1};
  
  private Item _selected;
  
  public List() {
    super(
        InitFlags.REGISTER,
        InitFlags.WITH_BORDER,
        InitFlags.WITH_DEFAULT_EVENTS
    );
    
    events().addFocusHandler(new FocusHandler());
    
    _border.setColour(_normalBorder);
    
    _inner = new Frame();
    controls().add(_inner);
  }
  
  public void clear() {
    AbstractControl<? extends ControlEvents> c = _inner.controls().first();
    while(c != null) {
      _inner.controls().remove(c);
      c = c.controlPrev();
    }
  }
  
  public Item add(String name, T data) {
    Item i = new Item(name, data);
    i.setY(_inner.controls().size() * 20);
    i.setWH(_w, 20);
    _inner.controls().add(i);
    return i;
  }
  
  public Item selected() {
    return _selected;
  }
  
  @Override
  protected void resize() {
    _inner.setWH(_w, _h);
    
    AbstractControl<? extends ControlEvents> c = _inner.controls().first();
    while(c != null) {
      c.setW(_w);
      c = c.controlPrev();
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
  
  public class Item extends AbstractControl<ControlEvents> {
    private Label  _text;
    private T      _data;
    
    private Item(String text, T data) {
      super(
          InitFlags.REGISTER,
          InitFlags.ACCEPTS_FOCUS,
          InitFlags.WITH_BORDER,
          InitFlags.WITH_DEFAULT_EVENTS
      );
      
      _text = new Label();
      _text.setText(text);
      _text.setX(4);
      
      AbstractScalable s = AbstractContext.newScalable();
      _background = s;
      
      s.setTexture(_itemBG);
      s.setXY(-1, -1);
      s.setSize(
          new float[] {5, 5, 5, 5},
          new float[] {5, 5, 5, 5},
          11, 11, 1
      );
      
      _border.setColour(_normalBorder);
      
      _data = data;
      
      controls().add(_text);
      
      events().addFocusHandler(new FocusHandler());
      events().addClickHandler(new ControlEvents.Click() {
        @Override public void clickDbl() { }
        @Override public void click() {
          
        }
      });
    }
    
    public String getText() { return _text.getText(); }
    public T      getData() { return _data; }
    
    public void setText(String text) { _text.setText(text); }
    public void setData(T      data) { _data = data; }
    
    @Override
    protected void resize() {
      _text.setY(_h / 2);
    }
    
    private class FocusHandler extends ControlEvents.Focus {
      @SuppressWarnings("unchecked")
      @Override
      public void got() {
        _border.setColour(_hoverBorder);
        _border.createBorder();
        _selected = (List<T>.Item)_control;
      }

      @Override
      public void lost() {
        _border.setColour(_normalBorder);
        _border.createBorder();
      }
    }
  }
}