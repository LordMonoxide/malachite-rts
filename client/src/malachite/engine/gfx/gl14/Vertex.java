package malachite.engine.gfx.gl14;

import malachite.engine.gfx.AbstractVertex;
import org.lwjgl.opengl.GL11;

public final class Vertex extends AbstractVertex {
  public void use() {
    GL11.glColor4f(_col[0], _col[1], _col[2], _col[3]);
    GL11.glTexCoord2f(_tex[0], _tex[1]);
    GL11.glVertex2f(_loc[0], _loc[1]);
  }
}
