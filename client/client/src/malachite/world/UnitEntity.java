package malachite.world;

import malachite.units.Unit;

public class UnitEntity extends Entity {
  public final Unit unit = (Unit)source;
  
  private Entity _target;
  private TASK   _task = TASK.NONE;
  
  public UnitEntity(Source source,                   int w, int h) { super(source,       w, h); }
  public UnitEntity(Source source, float x, float y, int w, int h) { super(source, x, y, w, h); }
  
  public void construct(BuildingEntity building) {
    _task = TASK.BUILDING;
    _target = building;
  }
  
  public void logic() {
    switch(_task) {
      case BUILDING:
        ((BuildingEntity)_target).construct(1); //TODO
        break;
    }
  }
  
  public enum TASK {
    NONE, BUILDING
  }
}