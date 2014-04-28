package malachite;

import java.util.ArrayList;

public class Player {
  private ArrayList<Building> _building = new ArrayList<>();
  private ArrayList<Unit>     _unit = new ArrayList<>();
  
  public void addBuilding(Building building) {
    _building.add(building);
  }
  
  public void addUnit(Unit unit) {
    _unit.add(unit);
  }
}