package malachite.gui;

import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.control.*;
import malachite.engine.gfx.textures.Texture;

public class MainMenu extends AbstractGUI {
  private Image[] _imgBackground = new Image[15];
  private Window _wndLogin;
  private Textbox _txtName;
  private Textbox _txtPass;
  private Check _chkRemember;
  private Button _btnLogin;
  private Button _btnRegister;
  private Frame _fraInfo;

  private Window _wndRegister;
  private Textbox _txtRegisterName;
  private Textbox[] _txtRegisterPass = new Textbox[2];
  private Textbox[] _txtRegisterSecurityQuestion = new Textbox[3];
  private Textbox[] _txtRegisterSecurityAnswer = new Textbox[3];

  @Override
  protected void load() {
    for(int i = 0; i < _imgBackground.length; i++) {
      Texture t = _textures.getTexture("gui/menu/" + i + ".png");
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
    _wndLogin.setText("Login");
    _wndLogin.events().addResizeHandler(new ControlEvents.Resize() {
      @Override
      public void resize() {
        _txtName.setW(_wndLogin.getContentW() - _txtName.getX() * 2);
        _txtPass.setW(_wndLogin.getContentW() - _txtPass.getX() * 2);
        _btnLogin.setX(_wndLogin.getContentW() - _btnLogin.getW() - _txtName.getX());
        _btnRegister.setX(_btnLogin.getX() - _btnRegister.getW() - 4);
        _fraInfo.setY(_chkRemember.getY() + _btnLogin.getH() + 8);
        _fraInfo.setWH(_wndLogin.getContentW() - _fraInfo.getX() * 2, _wndLogin.getContentH() - _fraInfo.getY() - _fraInfo.getX());
      }
    });

    _txtName = new Textbox();
    _txtName.setXY(4, 4);
    _txtName.setH(20);
    _txtName.setTextPlaceholder("Username");

    _txtPass = new Textbox();
    _txtPass.setXY(_txtName.getX(), _txtName.getY() + _txtName.getH() + 8);
    _txtPass.setH(20);
    _txtPass.setTextPlaceholder("Password");
    _txtPass.setMasked(true);

    _chkRemember = new Check();
    _chkRemember.setXY(_txtPass.getX(), _txtPass.getY() + _txtPass.getH() + 8);
    _chkRemember.setText("Remember me");

    _btnLogin = new Button();
    _btnLogin.setY(_txtPass.getY() + _txtPass.getH() + 8);
    _btnLogin.setWH(50, 20);
    _btnLogin.setText("Login");

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
    _wndRegister.setText("Register");
    _wndRegister.hide();
    _wndRegister.events().addResizeHandler(new ControlEvents.Resize() {
      @Override
      public void resize() {
        _txtRegisterName.setW(_wndRegister.getContentW() - _txtRegisterName.getX() * 2);
        _txtRegisterPass[0].setW(_wndRegister.getContentW() - _txtRegisterPass[0].getX() * 2);
        _txtRegisterPass[1].setW(_wndRegister.getContentW() - _txtRegisterPass[1].getX() * 2);
      }
    });

    _txtRegisterName = new Textbox();
    _txtRegisterName.setXY(4, 4);
    _txtRegisterName.setH(20);
    _txtRegisterName.setTextPlaceholder("Username");

    _txtRegisterPass[0] = new Textbox();
    _txtRegisterPass[0].setXY(_txtRegisterName.getX(), _txtRegisterName.getY() + _txtRegisterName.getH() + 8);
    _txtRegisterPass[0].setH(20);
    _txtRegisterPass[0].setTextPlaceholder("Password");
    _txtRegisterPass[0].setMasked(true);

    _txtRegisterPass[1] = new Textbox();
    _txtRegisterPass[1].setXY(_txtRegisterPass[0].getX(), _txtRegisterPass[0].getY() + _txtRegisterPass[0].getH() + 8);
    _txtRegisterPass[1].setH(20);
    _txtRegisterPass[1].setTextPlaceholder("Confirm Password");
    _txtRegisterPass[1].setMasked(true);

    controls().add(_wndLogin);
    controls().add(_wndRegister);

    _wndLogin.controls().add(_txtName);
    _wndLogin.controls().add(_txtPass);
    _wndLogin.controls().add(_chkRemember);
    _wndLogin.controls().add(_btnLogin);
    _wndLogin.controls().add(_btnRegister);
    _wndLogin.controls().add(_fraInfo);

    _wndRegister.controls().add(_txtRegisterName);
    _wndRegister.controls().add(_txtRegisterPass[0]);
    _wndRegister.controls().add(_txtRegisterPass[1]);

    _txtName.setFocus(true);
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
