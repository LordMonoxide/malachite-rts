package malachite.world.generators;

import malachite.world.Generator;
import malachite.world.Terrain;
import malachite.world.Tile;
import malachite.world.World;

public class Blank extends Generator {
  public Blank() {
    super(0);
  }
  
  @Override
  public World generate() {
    for(int y = 0; y < _tile.length; y++) {
      for(int x = 0; x < _tile[y].length; x++) {
        _tile[x][y] = new Tile(_rand.nextBoolean() ? Terrain.GRASS : Terrain.DIRT, x * 32, y * 32);
      }
    }
    
    return commit();
  }
}