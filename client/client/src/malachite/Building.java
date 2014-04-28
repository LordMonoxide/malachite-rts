package malachite;

import malachite.world.Entity;

public class Building {
  private malachite.api.models.Building _source;
  
  private float _x, _y;
  
  public Building(float x, float y, malachite.api.models.Building source) {
    _x = x;
    _y = y;
    _source = source;
  }
  
  public Entity createEntity() {
    return new Entity(_x, _y, 64, 48);
  }
}