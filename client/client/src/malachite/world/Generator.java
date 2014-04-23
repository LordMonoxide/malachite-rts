package malachite.world;

public abstract class Generator {
  protected Terrain[][] _tile = new Terrain[4096][4096];
  
  public abstract World generate();
  
  protected World commit() {
    World w = new World();
    w._tile = _tile;
    return w;
  }
}