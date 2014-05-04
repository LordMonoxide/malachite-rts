package malachite.buildings;

import malachite.world.Entity;

public class Base implements Building {
  { Buildings.register(new Base()); }
  
  private Base() { }
  
  @Override public Entity createEntity() {
    return null;
  }
}