package malachite;

import java.util.ArrayList;

public class Player {
  private ArrayList<Unit> _unit = new ArrayList<>();
  
  public void addUnit(Unit unit) {
    _unit.add(unit);
  }
}