package malachite.world;

import java.util.ArrayList;

import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.gl14.Context;
import malachite.engine.gfx.textures.Texture;
import malachite.engine.gfx.textures.TextureBuilder;

public class Tile {
  private static final TextureBuilder _textures = TextureBuilder.getInstance();
  
  public final Terrain terrain;
  public final int x, y;
  
  private ArrayList<AbstractDrawable> _d = new ArrayList<>();
  
  private Tile _left, _right, _up, _down;
  
  public Tile(Terrain terrain, int x, int y) {
    this.terrain = terrain;
    this.x = x;
    this.y = y;
    
    if(terrain == Terrain.NONE) {
      return;
    }
    
    Texture t = _textures.getTexture("terrain.png");
    AbstractDrawable d = Context.newDrawable();
    d.setTexture(t);
    d.setXYWH(x, y, 32, 32);
    
    switch(terrain) {
      case DIRT:
        d.setTXY(64, 0);
        break;
        
      case GRASS:
        d.setTXY(0, 0);
        break;
        
      case WATER:
        d.setTXY(96, 0);
        break;
    }
    
    d.setTWH(32, 32);
    d.createQuad();
    
    _d.add(d);
  }
  
  void link(Tile left, Tile right, Tile up, Tile down) {
    _left  = left;
    _right = right;
    _up    = up;
    _down  = down;
    
    if(terrain == Terrain.WATER) {
      Texture t = _textures.getTexture("terrain.png");
      
      if(left != null && left.terrain == Terrain.GRASS) {
        AbstractDrawable d = Context.newDrawable();
        d.setTexture(t);
        
        if(up != null && up.terrain == Terrain.GRASS) {
          d.setXYWH(x, y, 16, 32);
          d.setTXYWH(128, 64, 32, 32);
        } else {
          d.setXYWH(x, y, 16, 32);
          d.setTXYWH(176, 0, 16, 32);
        }
        
        d.createQuad();
        _d.add(d);
      }
      
      if(right != null && right.terrain == Terrain.GRASS) {
        AbstractDrawable d = Context.newDrawable();
        d.setTexture(t);
        
        if(up != null && up.terrain == Terrain.GRASS) {
          d.setXYWH(x, y, 32, 32);
          d.setTXYWH(96, 64, 32, 32);
        } else {
          d.setXYWH(x + 16, y, 16, 32);
          d.setTXYWH(160, 0, 16, 32);
        }
        
        d.createQuad();
        _d.add(d);
      }
      
      if(up != null && up.terrain == Terrain.GRASS) {
        if((left == null || left.terrain != Terrain.GRASS) && (right == null || right.terrain != Terrain.GRASS)) {
          AbstractDrawable d = Context.newDrawable();
          d.setTexture(t);
          d.setXYWH(x, y, 32, 16);
          d.setTXYWH(192, 16, 32, 16);
          d.createQuad();
          _d.add(d);
        }
      }
    }
  }
  
  public void render() {
    for(AbstractDrawable d : _d) {
      d.draw();
    }
  }
}