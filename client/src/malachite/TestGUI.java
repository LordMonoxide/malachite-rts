package malachite;

import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.control.Label;
import malachite.engine.gfx.gui.control.Textbox;

public class TestGUI extends AbstractGUI {
  private Label _lblTest;
  private Textbox _txtTest;

  @Override
  protected void load() {
    _lblTest = new Label(this);
    _lblTest.setText("This is a test");
    _lblTest.setXYWH(200, 200, 100, 20);

    _txtTest = new Textbox(this);
    _txtTest.setXYWH(200, 224, 100, 20);

    controls().add(_lblTest);
    controls().add(_txtTest);
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
