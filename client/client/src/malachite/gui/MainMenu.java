package malachite.gui;

import org.json.JSONObject;

import malachite.api.API;
import malachite.api.Lang;
import malachite.api.models.Character;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.builtin.Message;
import malachite.engine.gfx.gui.control.*;
import malachite.engine.gfx.textures.Texture;
import malachite.engine.net.http.Response;

public class MainMenu extends AbstractGUI {
  private Image[] _imgBackground = new Image[15];
  private Window _wndLogin;
  private Textbox _txtEmail;
  private Textbox _txtPass;
  private Check _chkRemember;
  private Button _btnLogin;
  private Button _btnRegister;
  private Frame _fraInfo;

  private Window _wndRegister;
  private Textbox _txtRegisterEmail;
  private Textbox[] _txtRegisterPass = new Textbox[2];
  private Textbox[] _txtRegisterSecurityQuestion = new Textbox[3];
  private Textbox[] _txtRegisterSecurityAnswer = new Textbox[3];
  
  private Window _wndChars;
  private List<?> _lstChars;
  private Button _btnCharUse;
  private Button _btnCharNew;
  private Button _btnCharDel;

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
    _wndLogin.setText(Lang.Menu.get(Lang.MenuKeys.LOGIN_TITLE));
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
      }
    });

    _txtEmail = new Textbox();
    _txtEmail.setXY(4, 4);
    _txtEmail.setH(20);
    _txtEmail.setTextPlaceholder(Lang.Menu.get(Lang.MenuKeys.LOGIN_EMAIL));

    _txtPass = new Textbox();
    _txtPass.setXY(_txtEmail.getX(), _txtEmail.getY() + _txtEmail.getH() + 8);
    _txtPass.setH(20);
    _txtPass.setTextPlaceholder(Lang.Menu.get(Lang.MenuKeys.LOGIN_PASS));
    _txtPass.setMasked(true);

    _chkRemember = new Check();
    _chkRemember.setXY(_txtPass.getX(), _txtPass.getY() + _txtPass.getH() + 8);
    _chkRemember.setText(Lang.Menu.get(Lang.MenuKeys.LOGIN_REMEMBER));

    _btnLogin = new Button();
    _btnLogin.setY(_txtPass.getY() + _txtPass.getH() + 8);
    _btnLogin.setWH(50, 20);
    _btnLogin.setText("Login");
    _btnLogin.events().addClickHandler(new ControlEvents.Click() {
      @Override public void click()    { attemptLogin(); }
      @Override public void clickDbl() { }
    });

    _btnRegister = new Button();
    _btnRegister.setY(_btnLogin.getY());
    _btnRegister.setWH(80, 20);
    _btnRegister.setText("Register...");
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

    _wndRegister = new Window();
    _wndRegister.setWH(400, 300);
    _wndRegister.setXY((_context.getW() - _wndRegister.getW()) / 2, (_context.getH() - _wndRegister.getH()) / 2);
    _wndRegister.setText(Lang.Menu.get(Lang.MenuKeys.REGISTER_TITLE));
    _wndRegister.hide();
    _wndRegister.events().addResizeHandler(new ControlEvents.Resize() {
      @Override
      public void resize() {
        _txtRegisterEmail.setW(_wndRegister.getContentW() - _txtRegisterEmail.getX() * 2);
        _txtRegisterPass[0].setW(_wndRegister.getContentW() - _txtRegisterPass[0].getX() * 2);
        _txtRegisterPass[1].setW(_wndRegister.getContentW() - _txtRegisterPass[1].getX() * 2);
      }
    });

    _txtRegisterEmail = new Textbox();
    _txtRegisterEmail.setXY(4, 4);
    _txtRegisterEmail.setH(20);
    _txtRegisterEmail.setTextPlaceholder(Lang.Menu.get(Lang.MenuKeys.REGISTER_EMAIL));

    _txtRegisterPass[0] = new Textbox();
    _txtRegisterPass[0].setXY(_txtRegisterEmail.getX(), _txtRegisterEmail.getY() + _txtRegisterEmail.getH() + 8);
    _txtRegisterPass[0].setH(20);
    _txtRegisterPass[0].setTextPlaceholder(Lang.Menu.get(Lang.MenuKeys.REGISTER_PASS));
    _txtRegisterPass[0].setMasked(true);

    _txtRegisterPass[1] = new Textbox();
    _txtRegisterPass[1].setXY(_txtRegisterPass[0].getX(), _txtRegisterPass[0].getY() + _txtRegisterPass[0].getH() + 8);
    _txtRegisterPass[1].setH(20);
    _txtRegisterPass[1].setTextPlaceholder(Lang.Menu.get(Lang.MenuKeys.REGISTER_CONFIRM));
    _txtRegisterPass[1].setMasked(true);
    
    _wndChars = new Window();
    _wndChars.setText(Lang.Menu.get(Lang.MenuKeys.CHARS_TITLE));
    _wndChars.setWH(400, 300);
    _wndChars.setXY((_context.getW() - _wndChars.getW()) / 2, (_context.getH() - _wndChars.getH()) / 2);
    _wndChars.hide();
    _wndChars.events().addResizeHandler(new ControlEvents.Resize() {
      @Override public void resize() {
        _lstChars.setWH(_wndChars.getContentW(), _wndChars.getContentH() - _btnCharUse.getH() - 8);
        _btnCharDel.setXY(4, _lstChars.getH() + 6);
        _btnCharNew.setXY(_btnCharDel.getX() + _btnCharDel.getW() + 4, _btnCharDel.getY());
        _btnCharUse.setXY(_lstChars.getW() - _btnCharUse.getW() - 4, _btnCharDel.getY());
      }
    });
    
    _lstChars = new List<>();
    
    _btnCharDel = new Button();
    _btnCharDel.setWH(50, 20);
    _btnCharDel.setText(Lang.Menu.get(Lang.MenuKeys.CHARS_DEL));
    
    _btnCharNew = new Button();
    _btnCharNew.setWH(50, 20);
    _btnCharNew.setText(Lang.Menu.get(Lang.MenuKeys.CHARS_NEW));
    
    _btnCharUse = new Button();
    _btnCharUse.setWH(50, 20);
    _btnCharUse.setText(Lang.Menu.get(Lang.MenuKeys.CHARS_USE));
    
    controls().add(_wndLogin);
    controls().add(_wndRegister);
    controls().add(_wndChars);

    _wndLogin.controls().add(_txtEmail);
    _wndLogin.controls().add(_txtPass);
    _wndLogin.controls().add(_chkRemember);
    _wndLogin.controls().add(_btnLogin);
    _wndLogin.controls().add(_btnRegister);
    _wndLogin.controls().add(_fraInfo);

    _wndRegister.controls().add(_txtRegisterEmail);
    _wndRegister.controls().add(_txtRegisterPass[0]);
    _wndRegister.controls().add(_txtRegisterPass[1]);
    
    _wndChars.controls().add(_lstChars);
    _wndChars.controls().add(_btnCharDel);
    _wndChars.controls().add(_btnCharNew);
    _wndChars.controls().add(_btnCharUse);
    
    checkLogin();
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
  
  private void checkLogin() {
    Message connecting = Message.wait("Connecting...", "Connecting...");
    connecting.push();
    
    API.check(new API.CheckResponse() {
      @Override public void loggedIn() {
        showCharacters();
        connecting.pop();
      }
      
      @Override public void loginRequired() {
        showLogin();
        connecting.pop();
      }
      
      @Override public void securityRequired() {
        showSecurity();
        connecting.pop();
      }
      
      @Override public void error(Response r) {
        System.out.println(r.content());
        connecting.pop();
      }
    });
  }
  
  private void attemptLogin() {
    Message wait = Message.wait("Logging in...", "Please wait.");
    wait.push();
    
    _wndLogin.disable();
    
    API.login(_txtEmail.getText(), _txtPass.getText(), new API.LoginResponse() {
      @Override public void success() {
        wait.pop();
        _wndLogin.hide();
        showCharacters();
      }
      
      @Override public void loginRequired() {
        wait.pop();
        showLogin();
      }
      
      @Override public void securityRequired() {
        wait.pop();
        _wndLogin.hide();
        showSecurity();
      }
      
      @Override public void invalid(JSONObject errors) {
        wait.pop();
        System.out.println(errors);
      }
      
      @Override public void error(Response r) {
        wait.pop();
        System.out.println(r.content());
      }
    });
  }
  
  private void showLogin() {
    _wndLogin.show();
    _txtEmail.setFocus(true);
  }
  
  private void showSecurity() {
    
  }
  
  private void showCharacters() {
    Message wait = Message.wait("Getting characters...", "Please wait.");
    wait.push();
    
    API.characters(new API.CharacterResponse() {
      @Override public void success(Character[] characters) {
        wait.pop();
        _wndChars.show();
        
        for(Character c : characters) {
          //_lstChars.add(c.name + ", a " + c.sex + ' ' + c.race, null);
          _lstChars.add(Lang.Menu.get(Lang.MenuKeys.CHARS_LIST, c.name, c.sex, c.race), null);
        }
      }
      
      @Override public void loginRequired() {
        wait.pop();
        showLogin();
      }
      
      @Override public void securityRequired() {
        wait.pop();
        showSecurity();
      }
      
      @Override public void error(Response r) {
        wait.pop();
        System.out.println(r.content());
      }
    });
  }
}
