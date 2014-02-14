package malachite.engine.gfx.textures;

import malachite.engine.gfx.Loader;
import malachite.engine.gfx.Manager;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public class Texture {
  private int _id;
  private int _w, _h;

  private boolean _loaded;

  protected Texture(int w, int h, ByteBuffer data) {
    _w = w;
    _h = h;

    Manager.getContext().addLoadCallback(Loader.LoaderThread.GRAPHICS, () -> {
      _id = GL11.glGenTextures();
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, _id);
      GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, _w, _h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
      System.out.println("Loaded requested texture ID " + _id);
      _loaded = true;
    });
  }

  public int getID() { return _id; }
  public int getW()  { return _w; }
  public int getH()  { return _h; }

  public boolean loaded() { return _loaded; }

  public void use() {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, _id);
  }

  public void destroy() {
    GL11.glDeleteTextures(_id);
  }
}
