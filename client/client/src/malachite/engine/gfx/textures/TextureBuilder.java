package malachite.engine.gfx.textures;

import de.matthiasmann.twl.utils.PNGDecoder;
import malachite.engine.util.Time;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class TextureBuilder {
  private static TextureBuilder _instance = new TextureBuilder();
  public static TextureBuilder getInstance() { return _instance; }
  
  private static final String TEXTURES_DIR = "gfx/textures/"; //$NON-NLS-1$

  private Map<String, Texture> _textures = new HashMap<>();
  private int _lock;

  public Texture getTexture(String name, int w, int h, ByteBuffer data) {
    double t = Time.get();

    while(_lock != 0) try { Thread.sleep(1); } catch(Exception e) { }
    if(_textures.containsKey(name)) {
      return _textures.get(name);
    }

    _lock++;
    Texture texture = new Texture(w, h, data);
    _textures.put(name, texture);
    _lock--;

    System.out.println("Texture \"" + name + "\" (" + w + 'x' + h + ") loaded. (" + (Time.get() - t) + ')'); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    return texture;
  }

  public Texture getTexture(String file) {
    double t = Time.get();

    while(_lock != 0) try { Thread.sleep(1); } catch(Exception e) { }
    if(_textures.containsKey(file)) {
      return _textures.get(file);
    }

    _lock++;

    ByteBuffer data = null;

    int w, h;

    try {
      File f = new File(TEXTURES_DIR + file);

      try(InputStream in = new FileInputStream(f)) {
        PNGDecoder png = new PNGDecoder(in);

        w = png.getWidth();
        h = png.getHeight();

        data = ByteBuffer.allocateDirect(4 * w * h);
        png.decode(data, w * 4, PNGDecoder.Format.RGBA);
        data.flip();
      }
    } catch(FileNotFoundException e) {
      System.err.println("Couldn't find texture \"" + file + '\"'); //$NON-NLS-1$
      _lock--;
      return null;
    } catch(IOException e) {
      e.printStackTrace();
      _lock--;
      return null;
    }

    Texture texture = new Texture(w, h, data);
    _textures.put(file, texture);

    _lock--;

    System.out.println("Texture \"" + file + "\" (" + w + 'x' + h +") loaded. (" + (Time.get() - t) + ')'); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    return texture;
  }

  public void destroy() {
    for(Texture t : _textures.values()) {
      t.destroy();
    }

    _textures.clear();
  }
}
