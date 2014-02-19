package malachite.gui;

import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.control.*;
import malachite.engine.gfx.textures.Texture;

public class MainMenu extends AbstractGUI {
  private Image[] _imgBackground = new Image[15];
  private Window _wndMenu;
  private Textbox _txtName;
  private Textbox _txtPass;
  private Check _chkRemember;

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

    _wndMenu = new Window();
    _wndMenu.setWH(400, 300);
    _wndMenu.setXY((_context.getW() - _wndMenu.getW()) / 2, (_context.getH() - _wndMenu.getH()) / 2);
    _wndMenu.setText("Main Menu");
    _wndMenu.events().addResizeHandler(new ControlEvents.Resize() {
      @Override
      public void resize() {
        _txtName.setW(_wndMenu.getContentW() - _txtName.getX() * 2);
        _txtPass.setW(_wndMenu.getContentW() - _txtPass.getX() * 2);
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

    controls().add(_wndMenu);
    _wndMenu.controls().add(_txtName);
    _wndMenu.controls().add(_txtPass);
    _wndMenu.controls().add(_chkRemember);
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
