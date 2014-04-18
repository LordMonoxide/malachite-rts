package malachite.engine.gfx.gui.builtin;

import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.VAlign;
import malachite.engine.gfx.gui.control.Label;
import malachite.engine.gfx.gui.control.Window;

public class Message extends AbstractGUI {
  public static Message wait(String title, String text) {
    Message m = new Message();
    m._initTitle = title;
    m._initText  = text;
    return m;
  }
  
  private String _initTitle;
  private String _initText;
  private Window _wndMessage;
  private Label  _lblText;
  
  @Override
  protected void load() {
    _lblText = new Label();
    _lblText.setVAlign(VAlign.ALIGN_TOP);
    _lblText.setText(_initText);
    
    _wndMessage = new Window();
    _wndMessage.setWH(300, 100);
    _wndMessage.setXY((_context.getW() - _wndMessage.getW()) / 2, (_context.getH() - _wndMessage.getH()) / 2);
    _wndMessage.setText(_initTitle);
    _wndMessage.controls().add(_lblText);
    _wndMessage.events().addResizeHandler(new ControlEvents.Resize() {
      @Override public void resize() {
        _lblText.setWH(_wndMessage.getContentW() - _lblText.getX() * 2, _wndMessage.getContentH() - _lblText.getY() * 2);
      }
    });
    
    controls().add(_wndMessage);
  }
  
  public String getTitle() {
    return _wndMessage.getText();
  }
  
  public void setTitle(String title) {
    _wndMessage.setText(title);
  }
  
  public String getText() {
    return _lblText.getText();
  }
  
  public void setText(String text) {
    _lblText.setText(text);
  }

  @Override
  protected void destroy() {
    
  }

  @Override
  protected void resize() {
    
  }

  @Override
  protected void draw() {
    
  }

  @Override
  protected boolean logic() {
    return false;
  }
}