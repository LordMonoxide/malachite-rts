package malachite;

import java.util.ArrayList;

import malachite.buildings.AbstractBuilding;
import malachite.units.Unit;

public class Player {
  private ArrayList<AbstractBuilding> _building = new ArrayList<>();
  private ArrayList<Unit>     _unit = new ArrayList<>();
  
  public void addBuilding(AbstractBuilding building) {
    _building.add(building);
  }
  
  public void addUnit(Unit unit) {
    _unit.add(unit);
  }
}