package malachite.engine.gfx.fonts;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.Loader;
import malachite.engine.gfx.textures.Texture;
import malachite.engine.gfx.textures.TextureBuilder;
import malachite.engine.util.Math;

import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FontBuilder {
  private static FontBuilder _instance = new FontBuilder();
  public static FontBuilder getInstance() { return _instance; }

  private TextureBuilder _textures = TextureBuilder.getInstance();
  private Map<String, Font> _fonts = new HashMap<>();

  private Font _default = getFont("Verdana", 11);
  public Font getDefault() { return _default; }

  private FontBuilder() { }

  public Font getFont(String name, int size) {
    return getFont(name, size, 0x20, 0x3FF, 0x25B2, 0x25BA, 0x25BC, 0x25C4);
    // Extra = Triangle up, right, down, left
  }

  public Font getFont(String name, int size, int startGlyph, int endGlyph, int... extraGlyphs) {
    String fullName = name + "." + size;
    if(_fonts.containsKey(fullName)) {
      return _fonts.get(fullName);
    }

    Font f = new Font();

    java.awt.Font font = new java.awt.Font(name, 0, size);
    FontRenderContext rendCont = new FontRenderContext(null, true, true);
    FontMetrics fm = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE).getGraphics().getFontMetrics(font);
    List<Metrics> metrics = new ArrayList<>();

    int highIndex = 0;

    for(int i = startGlyph; i <= endGlyph; i++) {
      highIndex = addGlyph(i, font, rendCont, metrics, highIndex);
    }

    for(int i : extraGlyphs) {
      int n = addGlyph(i, font, rendCont, metrics, highIndex);
      if(n > highIndex) { highIndex = n; }
    }

    int x = 0;
    int y = 0;
    int w = 512;
    int h = 512;

    Font.Glyph[] glyph = new Font.Glyph[highIndex + 1];

    byte[] data = new byte[w * h * 4];
    for(Metrics m : metrics) {
      if(x + m.w2 > w) {
        x = 0;
        y += m.h2;
      }

      int i1 = y * w * 4 + x * 4;
      int i2 = 0;

      for(int n = 0; n < m.h; n++) {
        System.arraycopy(m.argb, i2, data, i1, m.w * 4);

        i1 += w * 4;
        i2 += m.w * 4;
      }

      Font.Glyph g = new Font.Glyph();
      g.w = fm.charWidth(m.code);
      g.h = m.h;
      g.tx = x;
      g.ty = y;
      g.tw = m.w2;
      g.th = m.h2;
      glyph[m.code] = g;

      x += m.w2;
    }

    ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
    buffer.put(data);
    buffer.position(0);

    AbstractContext.getContext().addLoadCallback(Loader.LoaderThread.GRAPHICS, () -> {
      Texture texture = _textures.getTexture("Font." + font.getFontName() + "." + font.getSize(), w, h, buffer);

      f.load(metrics.get(0).h, glyph);
      f.setTexture(texture);
      _fonts.put(fullName, f);

      System.out.println("Font \"" + fullName + "\" created (" + w + "x" + h + ").");
    });

    return f;
  }

  private int addGlyph(int i, java.awt.Font font, FontRenderContext rendCont, List<Metrics> metrics, int highIndex) {
    if(!Character.isValidCodePoint(i)) { return highIndex; }

    char[] character = Character.toChars(i);

    Rectangle2D bounds = font.getStringBounds(character, 0, character.length, rendCont);
    int w = (int)bounds.getWidth();
    int h = (int)bounds.getHeight();

    if(w == 0) {
      return highIndex;
    }

    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g = (Graphics2D)bi.getGraphics();
    //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setFont(font);
    g.drawString(new String(character), 0, (int)(bounds.getHeight() - bounds.getMaxY()));

    int[] argb = null;
    argb = bi.getData().getPixels(0, 0, w, h, argb);

    byte[] argbByte = new byte[argb.length];
    for(int n = 0; n < argb.length; n++) {
      argbByte[n] = (byte)argb[n];
    }

    Metrics m = new Metrics();
    m.code = i;
    m.w = w;
    m.h = h;
    m.w2 = Math.nextPowerOfTwo(m.w);
    m.h2 = Math.nextPowerOfTwo(m.h);
    m.argb = argbByte;
    metrics.add(m);

    if(i > highIndex) { highIndex = i; }
    return highIndex;
  }

  private static class Metrics {
    private int code;
    private int w, h;
    private int w2, h2;
    private byte[] argb;
  }
}
