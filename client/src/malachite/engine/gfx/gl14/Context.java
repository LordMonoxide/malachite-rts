package malachite.engine.gfx.gl14;

import malachite.engine.gfx.AbstractContext;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class Context extends AbstractContext {
  @Override
  protected void createDisplay() throws LWJGLException {
    ContextAttribs attribs = new ContextAttribs(1, 4);
    PixelFormat format = new PixelFormat();
    Display.create(format, attribs);
  }

  @Override
  protected void createInstances() {
    _matrix = new Matrix();
    _vertex = Vertex.class;
    _drawable = Drawable.class;
  }

  @Override
  protected void updateSize() {
    GL11.glViewport(0, 0, getW(), getH());

    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GL11.glOrtho(0.0d, getW(), getH(), 0.0d, 1.0d, -1.0d);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
  }
}
