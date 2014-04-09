package malachite.gui;

import org.json.JSONException;
import org.json.JSONObject;

import malachite.api.API;
import malachite.api.Lang;
import malachite.api.Lang.MenuKeys;
import malachite.api.models.Character;
import malachite.api.models.Race;
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
  private List<Character> _lstChars;
  private Button _btnCharUse;
  private Button _btnCharNew;
  private Button _btnCharDel;
  
  private Window _wndNewChar;
  private Textbox _txtNewCharName;
  private Dropdown<Race> _drpNewCharRace;
  private Dropdown<String> _drpNewCharSex;
  private Button _btnNewCharCreate;

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
      @Override public void click()    { attemptLogin(); }
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
    
    _wndChars = new Window();
    _wndChars.setText(Lang.Menu.get(MenuKeys.CHARS_TITLE));
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
    _btnCharDel.setText(Lang.Menu.get(MenuKeys.CHARS_DEL));
    _btnCharDel.events().addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        _wndChars.hide();
        deleteCharacter(_lstChars.selected().getData());
      }
    });
    
    _btnCharNew = new Button();
    _btnCharNew.setWH(50, 20);
    _btnCharNew.setText(Lang.Menu.get(MenuKeys.CHARS_NEW));
    _btnCharNew.events().addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        _wndChars.hide();
        showCreateCharacter();
      }
    });
    
    _btnCharUse = new Button();
    _btnCharUse.setWH(50, 20);
    _btnCharUse.setText(Lang.Menu.get(MenuKeys.CHARS_USE));
    _btnCharUse.events().addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        useCharacter(_lstChars.selected().getData());
      }
    });
    
    _wndNewChar = new Window();
    _wndNewChar.setText(Lang.Menu.get(MenuKeys.NEWCHAR_TITLE));
    _wndNewChar.setWH(400, 300);
    _wndNewChar.setXY((_context.getW() - _wndNewChar.getW()) / 2, (_context.getH() - _wndNewChar.getH()) / 2);
    _wndNewChar.hide();
    _wndNewChar.events().addResizeHandler(new ControlEvents.Resize() {
      @Override
      public void resize() {
        _txtNewCharName.setW(_wndNewChar.getContentW() - _txtNewCharName.getX() * 2);
        _drpNewCharRace.setW(_wndNewChar.getContentW() - _drpNewCharRace.getX() * 2);
        _drpNewCharSex .setW(_wndNewChar.getContentW() - _drpNewCharSex .getX() * 2);
        _btnNewCharCreate.setX(_wndNewChar.getContentW() - _btnNewCharCreate.getW() - 4);
      }
    });
    
    _txtNewCharName = new Textbox();
    _txtNewCharName.setXY(4, 4);
    _txtNewCharName.setH(20);
    _txtNewCharName.setTextPlaceholder(Lang.Menu.get(MenuKeys.NEWCHAR_NAME));
    
    _drpNewCharRace = new Dropdown<>();
    _drpNewCharRace.setXY(_txtNewCharName.getX(), _txtNewCharName.getY() + _txtNewCharName.getH() + 8);
    _drpNewCharRace.setH(20);
    
    _drpNewCharSex = new Dropdown<>();
    _drpNewCharSex.setXY(_drpNewCharRace.getX(), _drpNewCharRace.getY() + _drpNewCharRace.getH() + 8);
    _drpNewCharSex.setH(20);
    _drpNewCharSex.add("Male", "male");
    _drpNewCharSex.add("Female", "female");
    
    _btnNewCharCreate = new Button();
    _btnNewCharCreate.setY(_drpNewCharSex.getY() + _drpNewCharSex.getH() + 8);
    _btnNewCharCreate.setWH(50, 20);
    _btnNewCharCreate.setText(Lang.Menu.get(MenuKeys.NEWCHAR_CREATE));
    _btnNewCharCreate.events().addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        _wndNewChar.hide();
        createCharacter(new Character(_txtNewCharName.getText(), _drpNewCharRace.getData(), _drpNewCharSex.getData()));
      }
    });
    
    controls().add(_wndLogin);
    controls().add(_wndRegister);
    controls().add(_wndChars);
    controls().add(_wndNewChar);

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

    _wndNewChar.controls().add(_txtNewCharName);
    _wndNewChar.controls().add(_drpNewCharRace);
    _wndNewChar.controls().add(_drpNewCharSex);
    _wndNewChar.controls().add(_btnNewCharCreate);
    
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
    Message connecting = Message.wait(Lang.Menu.get(MenuKeys.STATUS_LOADING), Lang.Menu.get(MenuKeys.STATUS_CONNECTING));
    connecting.push();
    
    API.Auth.check(new API.CheckResponse() {
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
        System.err.println("Error checking auth:\n" + r.content());
        connecting.pop();
      }
      
      @Override public void jsonError(Response r, JSONException e) {
        System.err.println("JSON encoding error checking auth:\n" + e + '\n' + r.content());
      }
    });
  }
  
  private void attemptLogin() {
    Message wait = Message.wait(Lang.Menu.get(MenuKeys.STATUS_LOADING), Lang.Menu.get(MenuKeys.STATUS_LOGGINGIN));
    wait.push();
    
    _wndLogin.disable();
    
    API.Auth.login(_txtEmail.getText(), _txtPass.getText(), new API.LoginResponse() {
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
        System.err.println(errors);
      }
      
      @Override public void error(Response r) {
        wait.pop();
        System.err.println("Error logging in:\n" + r.content());
      }
      
      @Override public void jsonError(Response r, JSONException e) {
        System.err.println("JSON encoding error logging in:\n" + e + '\n' + r.content());
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
    Message wait = Message.wait(Lang.Menu.get(MenuKeys.STATUS_LOADING), Lang.Menu.get(MenuKeys.STATUS_GETTINGCHARS));
    wait.push();
    
    _lstChars.clear();
    
    API.Storage.Characters.all(new API.CharactersAllResponse() {
      @Override public void success(Character[] characters) {
        wait.pop();
        _wndChars.show();
        
        for(Character c : characters) {
          _lstChars.add(Lang.Menu.get(MenuKeys.CHARS_LIST, c.name, c.sex, c.race.name), c);
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
        System.err.println("Error getting chars:\n" + r.content());
      }
      
      @Override public void jsonError(Response r, JSONException e) {
        System.err.println("JSON encoding error getting chars:\n" + e + '\n' + r.content());
      }
    });
  }
  
  private void showCreateCharacter() {
    Message wait = Message.wait(Lang.Menu.get(MenuKeys.STATUS_LOADING), Lang.Menu.get(MenuKeys.STATUS_GETTINGRACES));
    wait.push();
    
    API.Storage.Races.all(new API.RacesAllResponse() {
      @Override public void success(Race[] races) {
        wait.pop();
        _wndNewChar.show();
        
        for(Race r : races) {
          _drpNewCharRace.add(r.name, r);
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
        System.err.println("Error getting races:\n" + r.content());
      }
      
      @Override public void jsonError(Response r, JSONException e) {
        System.err.println("JSON encoding error getting races:\n" + e + '\n' + r.content());
      }
    });
  }
  
  private void deleteCharacter(Character character) {
    Message wait = Message.wait(Lang.Menu.get(MenuKeys.STATUS_LOADING), Lang.Menu.get(MenuKeys.STATUS_DELETINGCHAR));
    wait.push();
    
    API.Storage.Characters.delete(character, new API.CharactersDeleteResponse() {
      @Override
      public void success() {
        wait.pop();
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
        System.err.println(errors);
      }
      
      @Override public void error(Response r) {
        wait.pop();
        System.err.println("Error deleting char:\n" + r.content());
      }
      
      @Override public void jsonError(Response r, JSONException e) {
        System.err.println("JSON encoding error deleting char:\n" + e + '\n' + r.content());
      }
    });
  }
  
  private void createCharacter(Character character) {
    Message wait = Message.wait(Lang.Menu.get(MenuKeys.STATUS_LOADING), Lang.Menu.get(MenuKeys.STATUS_CREATINGCHAR));
    wait.push();
    
    API.Storage.Characters.create(character, new API.CharactersCreateResponse() {
      @Override
      public void success() {
        wait.pop();
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
        System.err.println(errors);
      }
      
      @Override public void error(Response r) {
        wait.pop();
        System.err.println("Error creating char:\n" + r.content());
      }
      
      @Override public void jsonError(Response r, JSONException e) {
        System.err.println("JSON encoding error creating char:\n" + e + '\n' + r.content());
      }
    });
  }
  
  private void useCharacter(Character character) {
    
  }
}
