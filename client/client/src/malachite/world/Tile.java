package malachite.world;

import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.gl14.Context;
import malachite.engine.gfx.textures.Texture;
import malachite.engine.gfx.textures.TextureBuilder;

public class Tile {
  private static final TextureBuilder _textures = TextureBuilder.getInstance();
  
  public final Terrain terrain;
  
  AbstractDrawable d;
  
  public Tile(Terrain terrain, int x, int y) {
    this.terrain = terrain;
    
    if(terrain == Terrain.NONE) {
      return;
    }
    
    Texture t = _textures.getTexture("terrain.png");
    d = Context.newDrawable();
    d.setTexture(t);
    d.setXYWH(x, y, 32, 32);
    
    switch(terrain) {
      case DIRT:
        d.setTXY(224, 0);
        break;
        
      case GRASS:
        d.setTXY(0, 0);
        break;
        
      case WATER:
        d.setTXY(32, 160);
        break;
    }
    
    d.setTWH(32, 32);
    d.createQuad();
  }
  
  public void render() {
    if(d != null) {
      d.draw();
    }
  }
}