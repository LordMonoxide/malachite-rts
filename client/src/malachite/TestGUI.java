package malachite;

import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.control.Label;

public class TestGUI extends AbstractGUI {
  private Label _lblTest;

  @Override
  protected void load() {
    _lblTest = new Label(this);
    _lblTest.setText("This is a test");
    _lblTest.setXYWH(200, 200, 100, 20);
    controls().add(_lblTest);
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
