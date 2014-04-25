package malachite.gui;

import malachite.Game;
import malachite.Game.MessageInterface;
import malachite.api.Lang;
import malachite.api.Lang.MenuKeys;
import malachite.engine.gfx.fonts.TextStream;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.VAlign;
import malachite.engine.gfx.gui.builtin.Message;
import malachite.engine.gfx.gui.control.*;
import malachite.engine.gfx.textures.Texture;

public class MainMenu extends AbstractGUI implements Game.MenuInterface {
  private Game.MenuProxy _proxy;
  
  private Image[]   _imgBackground = new Image[15];
  private Window    _wndLogin;
  private Textbox   _txtEmail;
  private Textbox   _txtPass;
  private Check     _chkRemember;
  private Button    _btnLogin;
  private Button    _btnRegister;
  private Frame     _fraInfo;
  private Label     _lblInfo;
  
  private Window    _wndRegister;
  private Textbox   _txtRegisterEmail;
  private Textbox[] _txtRegisterPass = new Textbox[2];
  private Textbox[] _txtRegisterSecurityQuestion = new Textbox[3];
  private Textbox[] _txtRegisterSecurityAnswer = new Textbox[3];
  
  private Window _wndMainMenu;
  private Button _btnPlay;
  private Button _btnLogout;
  
  public MainMenu(Game.MenuProxy proxy) {
    _proxy = proxy;
  }
  
  @Override
  protected void load() {
    for(int i = 0; i < _imgBackground.length; i++) {
      Texture t = _textures.getTexture("gui/menu/" + i + ".png"); //$NON-NLS-1$ //$NON-NLS-2$
      _imgBackground[i] = new Image();
      _imgBackground[i].setTexture(t);

      final int n = i;
      t.events().addLoadHandler(() -> {
        _imgBackground[n].setXY(n % 5 * _imgBackground[n].getW(), n / 5 * _imgBackground[n].getH());
      });

      controls().add(_imgBackground[i]);
    }

    _wndLogin = new Window();
    _wndLogin.setWH(400, 300);
    _wndLogin.setXY((_context.getW() - _wndLogin.getW()) / 2, (_context.getH() - _wndLogin.getH()) / 2);
    _wndLogin.setText(Lang.Menu.get(MenuKeys.LOGIN_TITLE));
    _wndLogin.hide();
    _wndLogin.events().addResizeHandler(new ControlEvents.Resize() {
      @Override
      public void resize() {
        _txtEmail.setW(_wndLogin.getContentW() - _txtEmail.getX() * 2);
        _txtPass.setW(_wndLogin.getContentW() - _txtPass.getX() * 2);
        _btnLogin.setX(_wndLogin.getContentW() - _btnLogin.getW() - _txtEmail.getX());
        _btnRegister.setX(_btnLogin.getX() - _btnRegister.getW() - 4);
        _fraInfo.setY(_chkRemember.getY() + _btnLogin.getH() + 8);
        _fraInfo.setWH(_wndLogin.getContentW() - _fraInfo.getX() * 2, _wndLogin.getContentH() - _fraInfo.getY() - _fraInfo.getX());
        _lblInfo.setWH(_fraInfo.getW() - _lblInfo.getX() * 2, _fraInfo.getH() - _lblInfo.getY() * 2);
      }
    });

    _txtEmail = new Textbox();
    _txtEmail.setXY(4, 4);
    _txtEmail.setH(20);
    _txtEmail.setTextPlaceholder(Lang.Menu.get(MenuKeys.LOGIN_EMAIL));

    _txtPass = new Textbox();
    _txtPass.setXY(_txtEmail.getX(), _txtEmail.getY() + _txtEmail.getH() + 8);
    _txtPass.setH(20);
    _txtPass.setTextPlaceholder(Lang.Menu.get(MenuKeys.LOGIN_PASS));
    _txtPass.setMasked(true);

    _chkRemember = new Check();
    _chkRemember.setXY(_txtPass.getX(), _txtPass.getY() + _txtPass.getH() + 8);
    _chkRemember.setText(Lang.Menu.get(MenuKeys.LOGIN_REMEMBER));

    _btnLogin = new Button();
    _btnLogin.setY(_txtPass.getY() + _txtPass.getH() + 8);
    _btnLogin.setWH(50, 20);
    _btnLogin.setText(Lang.Menu.get(MenuKeys.LOGIN_LOGIN));
    _btnLogin.events().addClickHandler(new ControlEvents.Click() {
      @Override public void click()    { _proxy.login(_txtEmail.getText(), _txtPass.getText()); }
      @Override public void clickDbl() { }
    });

    _btnRegister = new Button();
    _btnRegister.setY(_btnLogin.getY());
    _btnRegister.setWH(80, 20);
    _btnRegister.setText(Lang.Menu.get(MenuKeys.LOGIN_REGISTER));
    _btnRegister.events().addClickHandler(new ControlEvents.Click() {
      @Override
      public void click() {
        _wndLogin.hide();
        _wndRegister.show();
      }

      @Override public void clickDbl() { }
    });

    _fraInfo = new Frame();
    _fraInfo.setX(_chkRemember.getX());
    
    _lblInfo = new Label();
    _lblInfo.setXY(4, 4);
    _lblInfo.setVAlign(VAlign.ALIGN_TOP);
    
    _wndRegister = new Window();
    _wndRegister.setWH(400, 300);
    _wndRegister.setXY((_context.getW() - _wndRegister.getW()) / 2, (_context.getH() - _wndRegister.getH()) / 2);
    _wndRegister.setText(Lang.Menu.get(MenuKeys.REGISTER_TITLE));
    _wndRegister.hide();
    _wndRegister.events().addResizeHandler(new ControlEvents.Resize() {
      @Override
      public void resize() {
        _txtRegisterEmail.setW(_wndRegister.getContentW() - _txtRegisterEmail.getX() * 2);
        _txtRegisterPass[0].setW(_wndRegister.getContentW() - _txtRegisterPass[0].getX() * 2);
        _txtRegisterPass[1].setW(_wndRegister.getContentW() - _txtRegisterPass[1].getX() * 2);
        
        for(Textbox t : _txtRegisterSecurityQuestion) {
          t.setW(_wndRegister.getContentW() - t.getX() * 2);
        }
        
        for(Textbox t : _txtRegisterSecurityAnswer) {
          t.setW(_wndRegister.getContentW() - t.getX() * 2);
        }
      }
    });

    _txtRegisterEmail = new Textbox();
    _txtRegisterEmail.setXY(4, 4);
    _txtRegisterEmail.setH(20);
    _txtRegisterEmail.setTextPlaceholder(Lang.Menu.get(MenuKeys.REGISTER_EMAIL));

    _txtRegisterPass[0] = new Textbox();
    _txtRegisterPass[0].setXY(_txtRegisterEmail.getX(), _txtRegisterEmail.getY() + _txtRegisterEmail.getH() + 8);
    _txtRegisterPass[0].setH(20);
    _txtRegisterPass[0].setTextPlaceholder(Lang.Menu.get(MenuKeys.REGISTER_PASS));
    _txtRegisterPass[0].setMasked(true);

    _txtRegisterPass[1] = new Textbox();
    _txtRegisterPass[1].setXY(_txtRegisterPass[0].getX(), _txtRegisterPass[0].getY() + _txtRegisterPass[0].getH() + 8);
    _txtRegisterPass[1].setH(20);
    _txtRegisterPass[1].setTextPlaceholder(Lang.Menu.get(MenuKeys.REGISTER_CONFIRM));
    _txtRegisterPass[1].setMasked(true);
    
    int x = _txtRegisterPass[1].getX();
    int y = _txtRegisterPass[1].getY() + 8;
    for(int i = 0; i < _txtRegisterSecurityQuestion.length; i++) {
      _txtRegisterSecurityQuestion[i] = new Textbox();
      _txtRegisterSecurityAnswer  [i] = new Textbox();
      _txtRegisterSecurityQuestion[i].setH(20);
      _txtRegisterSecurityAnswer  [i].setH(20);
      _txtRegisterSecurityQuestion[i].setXY(x, y += (_txtRegisterSecurityQuestion[i].getH() + 12));
      _txtRegisterSecurityAnswer  [i].setXY(x, y += (_txtRegisterSecurityAnswer  [i].getH() + 6));
    }

    _wndMainMenu = new Window();
    _wndMainMenu.setWH(400, 300);
    _wndMainMenu.setXY((_context.getW() - _wndLogin.getW()) / 2, (_context.getH() - _wndLogin.getH()) / 2);
    _wndMainMenu.setText("mainmenu");
    _wndMainMenu.hide();
    _wndMainMenu.events().addResizeHandler(new ControlEvents.Resize() {
      @Override
      public void resize() {
        _btnPlay  .setX((_wndMainMenu.getContentW() - _btnPlay  .getW()) / 2);
        _btnLogout.setX((_wndMainMenu.getContentW() - _btnLogout.getW()) / 2);
      }
    });
    
    _btnPlay = new Button();
    _btnPlay.setWH(50, 20);
    _btnPlay.setY(8);
    _btnPlay.setText("play");
    _btnPlay.events().addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        _wndMainMenu.hide();
        _proxy.play();
      }
    });
    
    _btnLogout = new Button();
    _btnLogout.setWH(50, 20);
    _btnLogout.setY(_btnPlay.getY() + _btnPlay.getH() + 8);
    _btnLogout.setText("logout");
    _btnLogout.events().addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        _wndMainMenu.hide();
        _proxy.logout();
      }
    });
    
    controls().add(_wndLogin);
    controls().add(_wndRegister);
    controls().add(_wndMainMenu);
    
    _fraInfo.controls().add(_lblInfo);
    
    _wndLogin.controls().add(_txtEmail);
    _wndLogin.controls().add(_txtPass);
    _wndLogin.controls().add(_chkRemember);
    _wndLogin.controls().add(_btnLogin);
    _wndLogin.controls().add(_btnRegister);
    _wndLogin.controls().add(_fraInfo);

    _wndRegister.controls().add(_txtRegisterEmail);
    _wndRegister.controls().add(_txtRegisterPass[0]);
    _wndRegister.controls().add(_txtRegisterPass[1]);
    
    for(int i = 0; i < _txtRegisterSecurityQuestion.length; i++) {
      _wndRegister.controls().add(_txtRegisterSecurityQuestion[i]);
      _wndRegister.controls().add(_txtRegisterSecurityAnswer  [i]);
    }
    
    _wndMainMenu.controls().add(_btnPlay);
    _wndMainMenu.controls().add(_btnLogout);
    
    _proxy.requestNews();
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
  
  @Override
  public void reset() {
    hideLogin();
    hideRegister();
    hideMainMenu();
  }
  
  @Override
  public MessageInterface showMessage(MenuKeys title, MenuKeys text) {
    return new MessageHandler(title, text);
  }
  
  @Override
  public void gettingNews() {
    _lblInfo.setText("Getting news...");
  }
  
  @Override
  public void showNews(TextStream news) {
    _lblInfo.setTextStream(news);
  }
  
  @Override
  public void noNews() {
    _lblInfo.setText(null);
  }
  
  @Override
  public void showLogin() {
    _wndLogin.show();
    _txtEmail.setFocus(true);
    _txtEmail.setText(null);
    _txtPass.setText(null);
    _chkRemember.setChecked(false);
  }
  
  @Override
  public void hideLogin() {
    _wndLogin.hide();
  }
  
  @Override
  public void showRegister() {
    _wndRegister.show();
    _txtRegisterEmail.setText(null);
    _txtRegisterPass[0].setText(null);
    _txtRegisterPass[1].setText(null);
    
    for(int i = 0; i < _txtRegisterSecurityQuestion.length; i++) {
      _txtRegisterSecurityQuestion[i].setText(null);
      _txtRegisterSecurityAnswer  [i].setText(null);
    }
  }
  
  @Override
  public void hideRegister() {
    _wndRegister.hide();
  }
  
  @Override
  public void showSecurity() {
    
  }
  
  @Override
  public void hideSecurity() {
    
  }
  
  @Override public void showMainMenu() {
    _wndMainMenu.show();
  }
  
  @Override public void hideMainMenu() {
    _wndMainMenu.hide();
  }
  
  private class MessageHandler implements Game.MessageInterface {
    Message m;
    
    private MessageHandler(MenuKeys title, MenuKeys text) {
      m = Message.wait(Lang.Menu.get(title), Lang.Menu.get(text));
      m.push();
    }
    
    @Override public void update(MenuKeys title, MenuKeys text) {
      m.setTitle(Lang.Menu.get(title));
      m.setText (Lang.Menu.get(text));
    }
    
    @Override public void hide() {
      m.pop();
    }
  }
}
