package malachite.world;

import malachite.buildings.Building;

public class BuildingEntity extends Entity {
  public final Building building = (Building)source;
  
  private int _completion;
  
  public BuildingEntity(Source source,                   int w, int h) { super(source,       w, h); }
  public BuildingEntity(Source source, float x, float y, int w, int h) { super(source, x, y, w, h); }
  
  public boolean construct(int amount) {
    _completion += amount;
    
    if(_completion >= building.constructionTime()) {
      _completion = building.constructionTime();
      return true;
    }
    
    return false;
  }
  
  public void finish() {
    _completion = building.constructionTime();
  }
  
  public float completion() {
    return (float)_completion / building.constructionTime();
  }
  
  public boolean finished() {
    return _completion >= building.constructionTime();
  }
  
  public boolean unfinished() {
    return !finished();
  }
  
  public UnitEntity trainUnit() {
    return new UnitEntity(_x, _y);
  }
}