package malachite.world;

import java.util.Random;

public abstract class Generator {
  protected Random _rand;
  
  protected Tile[][] _tile = new Tile[256][256];
  
  public Generator(long seed) {
    _rand = new Random(seed);
  }
  
  public abstract World generate();
  
  protected World commit() {
    World w = new World();
    w._tile = _tile;
    return w;
  }
}