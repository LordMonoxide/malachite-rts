package malachite.gui;

import org.lwjgl.input.Keyboard;

import malachite.Game.GameProxy;
import malachite.api.Lang;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.control.Button;
import malachite.engine.gfx.gui.control.Window;

public class PauseMenu extends AbstractGUI {
  private Window _wndMenu;
  private Button _btnReturnToGame;
  private Button _btnQuit;
  
  private GameProxy _proxy;
  
  public PauseMenu(GameProxy proxy) {
    _proxy = proxy;
    ready();
  }
  
  @Override protected void load() {
    _wndMenu = new Window();
    _wndMenu.setText(Lang.Game.get(Lang.GameKeys.MENU_PAUSE_TITLE));
    _wndMenu.setWH(200, 80);
    _wndMenu.events().addResizeHandler(new ControlEvents.Resize() {
      @Override public void resize() {
        _btnReturnToGame.setW(_wndMenu.getContentW() - _btnReturnToGame.getX() * 2);
        _btnQuit        .setW(_wndMenu.getContentW() - _btnQuit        .getX() * 2);
      }
    });
    
    _wndMenu.events().addCloseHandler(new Window.Events.Close() {
      @Override public void close() {
        pop();
      }
    });
    
    _btnReturnToGame = new Button();
    _btnReturnToGame.setText(Lang.Game.get(Lang.GameKeys.MENU_PAUSE_RETURN));
    _btnReturnToGame.setH(20);
    _btnReturnToGame.setXY(8, 8);
    _btnReturnToGame.events().addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        pop();
      }
    });
    
    _btnQuit = new Button();
    _btnQuit.setText(Lang.Game.get(Lang.GameKeys.MENU_PAUSE_QUIT));
    _btnQuit.setH(20);
    _btnQuit.setXY(_btnReturnToGame.getX(), _btnReturnToGame.getY() + _btnReturnToGame.getH() + 8);
    _btnQuit.events().addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        _proxy.quit();
      }
    });
    
    _wndMenu.controls().add(_btnReturnToGame);
    _wndMenu.controls().add(_btnQuit);
    
    controls().add(_wndMenu);
  }
  
  @Override public void destroy() {
    
  }
  
  @Override protected void resize() {
    _wndMenu.setXY((_context.getW() - _wndMenu.getW()) / 2, (_context.getH() - _wndMenu.getH()) / 2);
  }
  
  @Override protected void draw() {
    
  }
  
  @Override protected boolean logic() {
    return true;
  }
  
  @Override public void push() {
    resize();
    super.push();
  }
  
  @Override protected boolean handleMouseDown (int x, int y, int button) { return true; }
  @Override protected boolean handleMouseUp   (int x, int y, int button) { return true; }
  @Override protected boolean handleMouseMove (int x, int y, int button) { return true; }
  @Override protected boolean handleMouseWheel(int delta)                { return true; }
  @Override protected boolean handleKeyDown   (int key)  { return true; }
  @Override protected boolean handleCharDown  (char key) { return true; }
  @Override protected boolean handleKeyUp(int key) {
    switch(key) {
      case Keyboard.KEY_ESCAPE:
        pop();
        break;
    }
    
    return true;
  }
}