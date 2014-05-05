package malachite.buildings;

import malachite.api.Lang.GameKeys;
import malachite.world.BuildingEntity;
import malachite.world.Entity;

public class Base implements Building {
  Base() { }
  
  @Override public GameKeys name() { return GameKeys.BUILDING_CAMP_NAME; }
  @Override public GameKeys desc() { return GameKeys.BUILDING_CAMP_DESC; }
  @Override public int constructionTime() { return 1000; }
  
  @Override public Entity createEntity() {
    return new BuildingEntity(this, 64, 48);
  }
}