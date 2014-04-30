package malachite.buildings;

import malachite.api.models.Building;
import malachite.world.Entity;

public class AbstractBuilding implements Entity.Source {
  private Building _source;
  
  private float _x, _y;
  
  public static AbstractBuilding Create(Building source, float x, float y) {
    AbstractBuilding b = null;
    
    switch(source.type) {
      case BASE:
        b = new Base();
        break;
        
      default:
        return null;
    }
    
    b._source = source;
    b._x = x;
    b._y = y;
    
    return b;
  }
  
  @Override
  public Entity createEntity() {
    return new Entity(this, _x, _y, 64, 48);
  }
}