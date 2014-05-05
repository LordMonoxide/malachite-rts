package malachite;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.world.BuildingEntity;
import malachite.world.UnitEntity;

public class Player {
  private ConcurrentLinkedDeque<BuildingEntity> _building = new ConcurrentLinkedDeque<>();
  private ConcurrentLinkedDeque<UnitEntity>     _unit     = new ConcurrentLinkedDeque<>();
  
  public void addBuilding(BuildingEntity building) {
    _building.add(building);
  }
  
  public void addUnit(UnitEntity unit) {
    _unit.add(unit);
  }
  
  public final Iterable<BuildingEntity> buildings = new Iterable<BuildingEntity>() {
    @Override public Iterator<BuildingEntity> iterator() {
      return _building.iterator();
    }
  };
  
  public final Iterable<UnitEntity> units = new Iterable<UnitEntity>() {
    @Override public Iterator<UnitEntity> iterator() {
      return _unit.iterator();
    }
  };
}