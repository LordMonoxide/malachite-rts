package malachite.world;

public class World {
  Tile[][] _tile;
  
  World() { }
  
  public Tile[][] getTiles() {
    return _tile;
  }
}