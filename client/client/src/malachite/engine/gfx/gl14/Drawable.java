package malachite.engine.gfx.gl14;

import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.AbstractVertex;
import org.lwjgl.opengl.GL11;

public class Drawable extends AbstractDrawable {
  @Override
  public void createQuad() {
    _renderMode = GL11.GL_TRIANGLE_STRIP;
    _vertex = Vertex.createQuad(new float[] {0, 0, _loc[2], _loc[3]}, _tex, _col);
  }

  @Override
  public void createBorder() {
    _renderMode = GL11.GL_LINE_STRIP;
    _vertex = Vertex.createBorder(new float[] {0, 0, _loc[2], _loc[3]}, _col);
  }

  @Override
  public void createLine() {
    _renderMode = GL11.GL_LINE;
    _vertex = Vertex.createLine(new float[] {_loc[0], _loc[1]}, new float[] {_loc[2], _loc[3]}, _col);
  }

  @Override
  public void draw() {
    if(_vertex == null || !_visible) { return; }

    _matrix.push();
    _matrix.translate(_loc[0], _loc[1]);

    if(_texture != null) {
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      _texture.use();
    } else {
      GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    GL11.glBegin(_renderMode);

    for(AbstractVertex vertex : _vertex) {
      if(vertex == null) { continue; }
      vertex.use();
    }

    GL11.glEnd();

    _matrix.pop();
  }
}
