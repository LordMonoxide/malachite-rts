package malachite.world;

import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.AbstractMatrix;
import malachite.engine.gfx.gl14.Context;

public class Entity {
  private static final AbstractMatrix _matrix = Context.getMatrix();
  
  private AbstractDrawable _draw;
  
  private float _x, _y;
  
  public Entity(float x, float y) {
    _x = x;
    _y = y;
    
    _draw = Context.newDrawable();
    _draw.setWH(32, 32);
    _draw.setColour(1, 0, 1, 1);
    _draw.createQuad();
  }
  
  public void draw() {
    _matrix.push();
    _matrix.translate(_x, _y);
    
    _draw.draw();
    
    _matrix.pop();
  }
}