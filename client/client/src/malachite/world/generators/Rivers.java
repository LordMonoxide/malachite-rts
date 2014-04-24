package malachite.world.generators;

import malachite.world.Generator;
import malachite.world.Terrain;
import malachite.world.Tile;
import malachite.world.World;

public class Rivers extends Generator {
  public Rivers() {
    super(0);
  }
  
  @Override
  public World generate() {
    for(int y = 0; y < _tile.length; y++) {
      for(int x = 0; x < _tile[y].length; x++) {
        _tile[x][y] = new Tile(_rand.nextBoolean() ? Terrain.GRASS : Terrain.DIRT, x * 32, y * 32);
      }
    }
    
    generateWater();
    
    return commit();
  }
  
  private void generateWater() {
    int numberOfRivers = _rand.nextInt(3) + 1;
    
    for(int riverNumber = 0; riverNumber < numberOfRivers; riverNumber++) {
      double riverTheta = _rand.nextInt(360) * Math.PI / 180;
      
      System.out.println("Angle\t" + riverTheta);
      
      int x = _tile[0].length / 2;
      int y = _tile.length / 2;
      
      double hyp = Math.sqrt(2);
      
      double x2 = x;
      double y2 = y;
      while(x2 > 0 && y2 > 0 && x2 < _tile[0].length - 1 && y2 < _tile.length - 1) {
        x2 += Math.cos(riverTheta);
        y2 += Math.sin(riverTheta);
      }
      
      x2 = Math.round(x2);
      y2 = Math.round(y2);
      
      System.out.println("Generating river @ " + x2 + ", " + y2 + " (theta " + riverTheta * 180 / Math.PI + ')');
      
      for(;;) {
        x2 -= Math.cos(riverTheta);
        y2 -= Math.sin(riverTheta);
        
        int tx = (int)x2;
        int ty = (int)y2;
        
        if(tx == -1 || ty == -1 || tx == _tile[0].length || ty == _tile.length) {
          break;
        }
        
        _tile[tx][ty] = new Tile(Terrain.WATER, tx * 32, ty * 32);
      }
    }
  }
}