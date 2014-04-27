package malachite.world;

import java.util.ArrayList;

public class World {
  Tile[][] _tile;
  
  private ArrayList<Entity> _entity = new ArrayList<>();
  
  World() { }
  
  public int getW() {
    return _tile[0].length;
  }
  
  public int getH() {
    return _tile.length;
  }
  
  public void addEntity(Entity e) {
    _entity.add(e);
  }
  
  public void draw(int x, int y, int w, int h) {
    for(int y1 = y; y1 < h; y1++) {
      for(int x1 = x; x1 < w; x1++) {
        _tile[x1][y1].render();
      }
    }
    
    for(Entity entity : _entity) {
      entity.draw();
    }
  }
}