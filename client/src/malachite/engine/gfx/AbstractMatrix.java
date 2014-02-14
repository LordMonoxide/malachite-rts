package malachite.engine.gfx;

public abstract class AbstractMatrix {
  protected AbstractMatrix() { }
  public abstract void push();
  public abstract void pop();
  public abstract void translate(float x, float y);
  public abstract void rotate(float angle, float x, float y);
  public abstract void scale(float x, float y);
  public abstract void reset();
}
