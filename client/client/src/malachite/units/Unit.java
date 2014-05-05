package malachite.units;

import malachite.world.Entity;
import malachite.world.UnitEntity;

public class Unit implements Entity.Source {
  private float _x, _y;
  
  public Unit(float x, float y) {
    _x = x;
    _y = y;
  }
  
  public float getX() { return _x; }
  public float getY() { return _y; }
  
  @Override
  public Entity createEntity() {
    return new UnitEntity(this, _x, _y, 32, 32);
  }
}