package malachite.engine.gfx;

import malachite.engine.gfx.textures.Texture;

public abstract class AbstractDrawable {
  protected AbstractMatrix _matrix = AbstractContext.getMatrix();

  protected Texture _texture;

  protected float[] _loc = {0, 0, 0, 0};
  protected float[] _tex = {0, 0, 1, 1};
  protected float[] _col = {1, 1, 1, 1};
  protected boolean _visible = true;

  protected int _renderMode;
  protected AbstractVertex[] _vertex;

  protected AbstractDrawable() { }

  public float getX()  { return _loc[0]; }
  public float getY()  { return _loc[1]; }
  public float getW()  { return _loc[2]; }
  public float getH()  { return _loc[3]; }
  public float getTX() { return _tex[0]; }
  public float getTY() { return _tex[1]; }
  public float getTW() { return _tex[2]; }
  public float getTH() { return _tex[3]; }

  public void setX(float x)  { _loc[0] = x; }
  public void setY(float y)  { _loc[1] = y; }
  public void setW(float w)  { _loc[2] = w; }
  public void setH(float h)  { _loc[3] = h; }
  public void setTX(float x) { _tex[0] = x / _texture.getW(); }
  public void setTY(float y) { _tex[1] = y / _texture.getH(); }
  public void setTW(float w) { _tex[2] = w / _texture.getW(); }
  public void setTH(float h) { _tex[3] = h / _texture.getH(); }

  public void setXY(float x, float y) {
    _loc[0] = x;
    _loc[1] = y;
  }

  public void setWH(float w, float h) {
    _loc[2] = w;
    _loc[3] = h;
  }

  public void setXYWH(float x, float y, float w, float h) {
    _loc[0] = x;
    _loc[1] = y;
    _loc[2] = w;
    _loc[3] = h;
  }

  public void setTXY(float x, float y) {
    if(_texture != null) {
      _tex[0] = x / _texture.getW();
      _tex[1] = y / _texture.getH();
    } else {
      _tex[0] = 0;
      _tex[1] = 0;
    }
  }

  public void setTWH(float w, float h) {
    if(_texture != null) {
      _tex[2] = w / _texture.getW();
      _tex[3] = h / _texture.getH();
    } else {
      _tex[2] = 0;
      _tex[3] = 0;
    }
  }

  public void setTXYWH(float x, float y, float w, float h) {
    if(_texture != null) {
      _tex[0] = x / _texture.getW();
      _tex[1] = y / _texture.getH();
      _tex[2] = w / _texture.getW();
      _tex[3] = h / _texture.getH();
    } else {
      _tex[0] = 0;
      _tex[1] = 0;
      _tex[2] = 0;
      _tex[3] = 0;
    }
  }

  public float[] getColour() {
    return _col;
  }

  public void setColour(float[] col) {
    _col = col;
  }
  
  public void setColour(float r, float g, float b, float a) {
    _col[0] = r;
    _col[1] = g;
    _col[2] = b;
    _col[3] = a;
  }

  public boolean getVisible() {
    return _visible;
  }

  public void setVisible(boolean visible) {
    _visible = visible;
  }

  public Texture getTexture() {
    return _texture;
  }

  public void setTexture(Texture texture) {
    _texture = texture;

    if(_texture != null) {
      setWH(texture.getW(), texture.getH());
      setTWH(texture.getW(), texture.getH());
    }
  }

  public int getRenderMode() {
    return _renderMode;
  }

  public void setRenderMode(int renderMode) {
    _renderMode = renderMode;
  }

  public AbstractVertex[] getVertices() {
    return _vertex;
  }

  public void setVertices(AbstractVertex[] vertex) {
    _vertex = vertex;
  }

  public abstract void createQuad();
  public abstract void createBorder();
  public abstract void createLine();
  public abstract void draw();
}