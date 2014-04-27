package malachite;

import malachite.world.Entity;

public class Unit {
  private malachite.api.models.Unit _source;
  
  public Unit(malachite.api.models.Unit source) {
    _source = source;
  }
  
  public Entity createEntity() {
    return new Entity(100, 100);
  }
}