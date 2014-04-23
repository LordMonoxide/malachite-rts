package malachite.world.generators;

import malachite.world.Generator;
import malachite.world.Terrain;
import malachite.world.World;

public class Blank extends Generator {
  @Override
  public World generate() {
    for(int y = 0; y < _tile.length; y++) {
      for(int x = 0; x < _tile[y].length; x++) {
        _tile[x][y] = Terrain.GRASS;
      }
    }
    
    return commit();
  }
}