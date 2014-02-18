package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractScalable;
import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.ControlList;
import malachite.engine.gfx.textures.TextureBuilder;

public class Window extends AbstractControl<ControlEvents> {
  private Image _title;
  private Button _close;
  private Image _content;

  public Window() {
    super();

    TextureBuilder t = TextureBuilder.getInstance();
    AbstractScalable s = AbstractContext.newScalable();
    s.setTexture(t.getTexture("gui/background.png"));
    s.setSize(new float[] {2, 2, 2, 2},
        new float[] {2, 2, 2, 2},
        260, 260, 256
    );

    _background = s;

    s = AbstractContext.newScalable();
    s.setTexture(t.getTexture("gui/title.png"));
    s.setSize(new float[] {2, 10, 2, 10},
        new float[] {2, 10, 2, 10},
        5, 21, 1
    );

    _title = new Image();
    _title.setBackground(s);
    _title.setY(-20);
    _title.setH(21);

    _close = new Button();
    _close.setBackground(AbstractContext.newDrawable());
    _close.getBackground().setTexture(t.getTexture("gui/close.png"));
    _close.getBackground().setTWH(13, 13);
    _close.setY(4);
    _close.setWH(13, 13);
    _title.controls().add(_close);

    s = AbstractContext.newScalable();
    s.setTexture(t.getTexture("gui/foreground.png"));
    s.setXY(-7, -7);
    s.setSize(new float[] {21, 21, 21, 21},
        new float[] {21, 21, 21, 21},
        43, 43, 1
    );

    _content = new Image();
    _content.setBackground(s);
    _content.setXY(8, 8);

    controls().add(_title);
    controls().add(_content);
  }

  @Override
  protected void setGUI(AbstractGUI gui) {
    super.setGUI(gui);
    _title.setGUI(gui);
    _close.setGUI(gui);
    _content.setGUI(gui);
  }

  @Override
  public ControlList controls() {
    return _content.controls();
  }

  @Override
  protected void resize() {
    _title.setW(_w);
    _close.setX(_w - _close.getW() - _close.getY());
    _content.setWH(
      _w - _content.getX() * 2,
      _h - _content.getY() * 2
    );
  }
}
