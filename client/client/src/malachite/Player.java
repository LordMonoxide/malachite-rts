package malachite;

import java.util.ArrayList;
import java.util.Iterator;

import malachite.world.BuildingEntity;
import malachite.world.UnitEntity;

public class Player {
  private ArrayList<BuildingEntity> _building = new ArrayList<>();
  private ArrayList<UnitEntity>     _unit = new ArrayList<>();
  
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
    
    public BuildingEntity get(int index) {
      return _building.get(index);
    }
    
    public int count() {
      return _building.size();
    }
  };
  
  public final Iterable<UnitEntity> units = new Iterable<UnitEntity>() {
    @Override public Iterator<UnitEntity> iterator() {
      return _unit.iterator();
    }
    
    public UnitEntity get(int index) {
      return _unit.get(index);
    }
    
    public int count() {
      return _unit.size();
    }
  };
}