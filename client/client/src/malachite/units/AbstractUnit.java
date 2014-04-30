package malachite.units;

import malachite.api.models.Unit;
import malachite.world.Entity;

public abstract class AbstractUnit implements Entity.Source {
  private Unit _source;
  
  private float _x, _y;
  
  public static AbstractUnit Create(Unit source, float x, float y) {
    AbstractUnit u = null;
    
    switch(source.type) {
      case VILLAGER:
        u = new Villager();
        break;
        
      default:
        return null;
    }
    
    u._source = source;
    u._x = x;
    u._y = y;
    
    return u;
  }
  
  public float getX() { return _x; }
  public float getY() { return _y; }
  
  @Override
  public Entity createEntity() {
    return new Entity(this, _x, _y, 32, 32);
  }
}