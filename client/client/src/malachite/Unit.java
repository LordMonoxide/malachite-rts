package malachite;

import malachite.world.Entity;

public class Unit {
  private malachite.api.models.Unit _source;
  
  private float _x, _y;
  
  public Unit(float x, float y, malachite.api.models.Unit source) {
    _x = x;
    _y = y;
    _source = source;
  }
  
  public Entity createEntity() {
    return new Entity(_x, _y, 32, 32);
  }
}