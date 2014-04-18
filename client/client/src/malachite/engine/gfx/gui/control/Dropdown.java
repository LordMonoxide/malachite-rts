package malachite.engine.gfx.gui.control;

import java.util.ArrayList;
import java.util.List;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractScalable;
import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.fonts.TextStream;
import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.textures.Texture;
import malachite.engine.gfx.textures.TextureBuilder;

public class Dropdown<T> extends AbstractControl<ControlEvents> {
  private Font _font = FontBuilder.getInstance().getDefault();
  
  private TextureBuilder _textures = TextureBuilder.getInstance();
  private Texture _textureNormal = _textures.getTexture("gui/textbox.png");
  private Texture _textureHover = _textures.getTexture("gui/textbox_hover.png");
  
  private float[] _normalBorder = {160f / 0xFF, 147f / 0xFF, 111f / 0xFF, 1};
  private float[] _hoverBorder  = { 11f / 0xFF, 126f / 0xFF,   0f / 0xFF, 1};
  
  private TextStream.Colour _textColour = new TextStream.Colour(65f / 255, 52f / 255, 8f / 255, 1);
  
  private List<Item> _items = new ArrayList<>();
  private int _selected = -1;
  
  private Drop _drop;
  
  public Dropdown() {
    super(
      InitFlags.ACCEPTS_FOCUS,
      InitFlags.REGISTER,
      InitFlags.WITH_BORDER,
      InitFlags.WITH_DEFAULT_EVENTS
    );
    
    _drop = new Drop();
    
    _events.addFocusHandler(new FocusHandler());
    _events.addHoverHandler(new HoverHandler());
    _events.addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        _drop.push();
        _drop.resize();
        _drop._frame.setXY(getAllX(), getAllY());
      }
    });
    
    _border.setColour(_normalBorder);

    AbstractScalable s = AbstractContext.newScalable();
    _background = s;
    
    s.setTexture(_textureNormal);
    s.setXY(-5, -5);
    s.setSize(
      new float[] {12, 12, 12, 12},
      new float[] {12, 12, 12, 12},
      25, 25, 1
    );
  }

  @Override
  protected void setGUI(AbstractGUI gui) {
    super.setGUI(gui);
  }
  
  @Override
  protected void resize() {
    
  }
  
  public void clear() {
    _selected = -1;
    _items = new ArrayList<>();
  }
  
  public Item add(String text, T data) {
    Item item = new Item(text, data);
    _items.add(item);
    
    if(_selected == -1) {
      setSelected(0);
    }
    
    return item;
  }
  
  public int getSelected() {
    return _selected;
  }

  public void setSelected(int index) {
    _selected = Math.max(0, Math.min(index, _items.size() - 1));
  }
  
  public String getText() {
    if(_selected != -1) {
      return _items.get(_selected)._text.getText();
    }
    
    return null;
  }
  
  public T getData() {
    if(_selected != -1) {
      return _items.get(_selected)._data;
    }
    
    return null;
  }
  
  @Override
  public void draw() {
    if(drawBegin()) {
      if(_selected != -1) {
        _font.draw(0, 0, _items.get(_selected)._textStream);
      }
    }

    drawEnd();
  }
  
  public class Item {
    private TextStream _textStream = new TextStream();
    private TextStream.Text _text = new TextStream.Text();
    private T      _data;
    
    private Item(String text, T data) {
      _textStream.insert(_textColour, _text);
      _text.setText(text);
      _data = data;
    }
    
    public String getText() { return _text.getText(); }
    public T      getData() { return _data; }
    
    public void setText(String text) { _text.setText(text); }
    public void setData(T      data) { _data = data; }
  }
  
  private class Drop extends AbstractGUI {
    private Frame _frame;
    
    @Override
    protected void load() {
      _frame = new Frame();
      _frame.events().addDrawHandler(new ControlEvents.Draw() {
        @Override public void draw() {
          int y = 0;
          
          for(Item item : _items) {
            _font.draw(0, y, item._textStream);
            y += _font.regular().getH();
          }
        }
      });
      
      _frame.events().addMouseHandler(new ControlEvents.Mouse() {
        @Override public void up  (int x, int y, int button) { }
        @Override public void move(int x, int y, int button) { }
        @Override public void down(int x, int y, int button) {
          setSelected(y / _font.regular().getH());
        }
      });
      
      controls().add(_frame);
    }

    @Override
    protected void destroy() {
      
    }

    @Override
    protected void resize() {
      _frame.setWH(_w, _items.size() * _font.regular().getH());
    }

    @Override
    protected void draw() {
      
    }

    @Override
    protected boolean logic() {
      return false;
    }
    
    @Override
    protected boolean handleMouseDown(int x, int y, int button) {
      pop();
      return true;
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
}