package malachite.engine.gfx.gui.control;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.gui.AbstractControl;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.textures.Texture;

public class Image extends AbstractControl<ControlEvents> {
  private AbstractDrawable _image;

  public Image(InitFlags... flags) {
    super(flags);
    _image = AbstractContext.newDrawable();
  }

  @Override
  protected void setGUI(AbstractGUI gui) {
    super.setGUI(gui);
  }

  public void setTexture(Texture texture) {
    texture.events().addLoadHandler(() -> {
      _image.setTexture(texture);
      _image.createQuad();
      setWH(texture.getW(), texture.getH());
    });
  }

  public Texture getTexture() {
    return _image.getTexture();
  }

  @Override
  protected void resize() {

  }

  @Override
  public void draw() {
    if(drawBegin()) {
      _image.draw();
    }

    drawEnd();
  }
}
