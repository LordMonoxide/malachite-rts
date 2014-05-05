package malachite.buildings;

import java.util.ArrayList;

public final class Buildings {
  private Buildings() { }
  
  private static ArrayList<Building> _building = new ArrayList<>();
  
  public static final Building base = register(new Base());
  
  static Building register(Building building) {
    _building.add(building);
    return building;
  }
  
  public static int count() {
    return _building.size();
  }
  
  public static Building get(int index) {
    return _building.get(index);
  }
  
  public static void each(BuildingIterator iterator) {
    for(Building building : _building) {
      iterator.iteration(building);
    }
  }
  
  public static interface BuildingIterator {
    public void iteration(Building building);
  }
}