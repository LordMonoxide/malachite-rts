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
    for(int y = 0; y < _tile.length; y++) {
      for(int x = 0; x < _tile[y].length; x++) {
        Tile left  = x >                   0 ? _tile[x - 1][y] : null;
        Tile up    = y >                   0 ? _tile[x][y - 1] : null;
        Tile right = x < _tile[y].length - 1 ? _tile[x + 1][y] : null;
        Tile down  = y < _tile   .length - 1 ? _tile[x][y + 1] : null;
        _tile[x][y].link(left, right, up, down);
      }
    }
    
    World w = new World();
    w._tile = _tile;
    return w;
  }
}