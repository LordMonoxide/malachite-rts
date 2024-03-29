package malachite.buildings;

import malachite.api.Lang.GameKeys;
import malachite.world.Entity;

public interface Building extends Entity.Source {
  @Override
  public GameKeys name();
  public GameKeys desc();
  public int constructionTime();
}