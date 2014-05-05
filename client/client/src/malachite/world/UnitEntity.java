package malachite.world;

import malachite.api.Lang;
import malachite.pathfinding.Point;

public class UnitEntity extends Entity {
  private Entity _target;
  private TASK   _task = TASK.NONE;
  
  public UnitEntity() { this(0, 0); }
  public UnitEntity(float x, float y) {
    super(source, x, y, 32, 32);
  }
  
  @Override public void moveAlong(Point[] path, Callback onReachDestination) {
    stopTask();
    super.moveAlong(path, onReachDestination);
  }
  
  public void construct(BuildingEntity building) {
    _task = TASK.BUILDING;
    _target = building;
  }
  
  public void stopTask() {
    _task = TASK.NONE;
    _target = null;
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
  
  private static Entity.Source source = new Source() {
    @Override public Lang.GameKeys name() { return Lang.GameKeys.UNIT_VILLAGER; }
    @Override public Entity createEntity() { return null; }
  };
}