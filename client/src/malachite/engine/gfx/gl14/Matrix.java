package malachite.engine.gfx.gl14;

import malachite.engine.gfx.AbstractMatrix;
import org.lwjgl.opengl.GL11;

public final class Matrix extends AbstractMatrix {
  Matrix() { }

  @Override
  public void push() {
    GL11.glPushMatrix();
  }

  @Override
  public void pop() {
    GL11.glPopMatrix();
  }

  @Override
  public void translate(float x, float y) {
    GL11.glTranslatef(x, y, 0);
  }

  @Override
  public void rotate(float angle, float x, float y) {
    GL11.glRotatef(angle, x, y, 0);
  }

  @Override
  public void scale(float x, float y) {
    GL11.glScalef(x, y, 1);
  }

  @Override
  public void reset() {
    GL11.glLoadIdentity();
  }
}
