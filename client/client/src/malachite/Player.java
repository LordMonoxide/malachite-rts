package malachite;

import java.util.ArrayList;

import malachite.buildings.AbstractBuilding;
import malachite.units.AbstractUnit;

public class Player {
  private ArrayList<AbstractBuilding> _building = new ArrayList<>();
  private ArrayList<AbstractUnit>     _unit = new ArrayList<>();
  
  public void addBuilding(AbstractBuilding building) {
    _building.add(building);
  }
  
  public void addUnit(AbstractUnit unit) {
    _unit.add(unit);
  }
}