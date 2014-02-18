package malachite;

import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.control.*;
import malachite.engine.gfx.textures.Texture;
import malachite.engine.gfx.textures.TextureBuilder;

public class TestGUI extends AbstractGUI {
  private Image[] _imgBackground = new Image[15];
  private Label _lblTest;
  private Textbox _txtTest;
  private Button _btnTest;
  private Window _wndTest;

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

    _lblTest = new Label();
    _lblTest.setText("This is a test");
    _lblTest.setXYWH(200, 200, 100, 20);

    _txtTest = new Textbox();
    _txtTest.setXYWH(200, 224, 100, 20);

    _btnTest = new Button();
    _btnTest.setXYWH(200, 248, 100, 20);
    _btnTest.setText("Test");

    _wndTest = new Window();
    _wndTest.setXYWH(400, 400, 400, 300);
    _wndTest.setText("Test");

    controls().add(_lblTest);
    controls().add(_txtTest);
    controls().add(_btnTest);
    controls().add(_wndTest);
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
