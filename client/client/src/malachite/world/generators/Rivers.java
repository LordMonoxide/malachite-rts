package malachite.world.generators;

import malachite.world.Generator;
import malachite.world.Terrain;
import malachite.world.Tile;
import malachite.world.World;

public class Rivers extends Generator {
  public Rivers() {
    super(14);
  }
  
  @Override
  public World generate() {
    generateWater();
    generateTerrain();
    
    return commit();
  }
  
  private void generateTerrain() {
    for(int y = 0; y < _tile.length; y++) {
      for(int x = 0; x < _tile[y].length; x++) {
        if(_tile[x][y] == null) {
          _tile[x][y] = new Tile(_rand.nextBoolean() ? Terrain.GRASS : Terrain.DIRT, x * 32, y * 32);
        }
      }
    }
  }
  
  private void generateWater() {
    int numberOfRivers = _rand.nextInt(3) + 1;
    
    for(int riverNumber = 0; riverNumber < numberOfRivers; riverNumber++) {
      double riverTheta = _rand.nextInt(360) * Math.PI / 180;
      int riverSize = _rand.nextInt(3) + 2;
      
      System.out.println("Angle\t" + riverTheta);
      
      int x = _tile[0].length / 2;
      int y = _tile.length / 2;
      
      double x2 = x;
      double y2 = y;
      while(x2 > 0 && y2 > 0 && x2 < _tile[0].length - 1 && y2 < _tile.length - 1) {
        x2 += Math.cos(riverTheta);
        y2 += Math.sin(riverTheta);
      }
      
      x2 = Math.round(x2);
      y2 = Math.round(y2);
      
      System.out.println("Generating river @ " + x2 + ", " + y2 + " size " + riverSize + " (theta " + riverTheta * 180 / Math.PI + ')');
      
      int changeChance = 10;
      
      for(;;) {
        x2 -= Math.cos(riverTheta);
        y2 -= Math.sin(riverTheta);
        
        if(_rand.nextInt(changeChance) == 0) {
          riverTheta += (_rand.nextDouble() * Math.PI * 2 - Math.PI) * 0.1;
          changeChance = 10;
        } else {
          changeChance--;
        }
        
        int tx = (int)x2;
        int ty = (int)y2;
        
        if(tx == -1 || ty == -1 || tx == _tile[0].length || ty == _tile.length) {
          break;
        }
        
        double swathTheta = riverTheta - Math.PI / 2;
        double leftX = tx - (int)(Math.cos(swathTheta) * riverSize);
        double leftY = ty - (int)(Math.sin(swathTheta) * riverSize);
        double rightX = tx - (int)(Math.cos(riverTheta + Math.PI / 2) * riverSize);
        double rightY = ty - (int)(Math.sin(riverTheta + Math.PI / 2) * riverSize);
        
        for(int s = 1; s <= riverSize * 32; s++) {
          int leftXI = (int)Math.round(leftX + Math.cos(swathTheta) * s / 32);
          int leftYI = (int)Math.round(leftY + Math.sin(swathTheta) * s / 32);
          
          if(leftXI >= 0 && leftYI >= 0 && leftXI < _tile[0].length && leftYI < _tile.length) {
            if(_tile[leftXI][leftYI] == null) {
              _tile[leftXI][leftYI] = new Tile(Terrain.WATER, leftXI * 32, leftYI * 32);
            }
          }
        }
      }
    }
  }
}