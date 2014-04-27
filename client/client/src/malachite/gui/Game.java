package malachite.gui;

import java.util.ArrayList;

import malachite.Unit;
import malachite.Game.GameInterface;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.world.Tile;
import malachite.world.World;

public class Game extends AbstractGUI implements GameInterface {
  World _world;
  float _viewX, _viewY;
  int _viewW, _viewH;
  
  public Game(World world) {
    _world = world;
  }
  
  @Override
  protected void load() {
    _context.setBackColour(0, 0, 0, 1);
    resize();
  }
  
  @Override
  protected void destroy() {
    
  }
  
  @Override
  protected void resize() {
    _viewW = (int)Math.ceil(_context.getW() / 32);
    _viewH = (int)Math.ceil(_context.getH() / 32);
  }
  
  @Override
  protected void draw() {
    _matrix.push();
    _matrix.translate(-_viewX, -_viewY);
    
    int x1 = (int)Math.max(0, _viewX / 32);
    int y1 = (int)Math.max(0, _viewY / 32);
    int w1 = Math.min(x1 + _viewW + 1, _world.getW() - 1);
    int h1 = Math.min(y1 + _viewH + 1, _world.getH() - 1);
    
    _world.draw(x1, y1, w1, h1);
    
    _matrix.pop();
  }
  
  @Override
  protected boolean logic() {
    int x = _context.getMouseX();
    int y = _context.getMouseY();
    
    if(x < 64) {
      _viewX -= 3;
    }
    
    if(y < 64) {
      _viewY -= 3;
    }
    
    if(x > _context.getW() - 64) {
      _viewX += 3;
    }
    
    if(y > _context.getH() - 64) {
      _viewY += 3;
    }
    
    return false;
  }
}