package malachite;

import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.control.Button;
import malachite.engine.gfx.gui.control.Image;
import malachite.engine.gfx.gui.control.Label;
import malachite.engine.gfx.gui.control.Textbox;
import malachite.engine.gfx.textures.Texture;
import malachite.engine.gfx.textures.TextureBuilder;

public class TestGUI extends AbstractGUI {
  private Image[] _imgBackground = new Image[15];
  private Label _lblTest;
  private Textbox _txtTest;
  private Button _btnTest;

  @Override
  protected void load() {
    for(int i = 0; i < _imgBackground.length; i++) {
      Texture t = _textures.getTexture("gui/menu/" + i + ".png");
      _imgBackground[i] = new Image(this);
      _imgBackground[i].setTexture(t);

      final int n = i;
      t.events().addLoadHandler(() -> {
        _imgBackground[n].setXY(n % 5 * _imgBackground[n].getW(), n / 5 * _imgBackground[n].getH());
      });

      controls().add(_imgBackground[i]);
    }

    _lblTest = new Label(this);
    _lblTest.setText("This is a test");
    _lblTest.setXYWH(200, 200, 100, 20);

    _txtTest = new Textbox(this);
    _txtTest.setXYWH(200, 224, 100, 20);

    _btnTest = new Button(this);
    _btnTest.setXYWH(200, 248, 100, 20);
    _btnTest.setText("Test");

    controls().add(_lblTest);
    controls().add(_txtTest);
    controls().add(_btnTest);
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
