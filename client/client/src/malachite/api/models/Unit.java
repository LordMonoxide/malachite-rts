package malachite.api.models;

public class Unit {
  public static final String DB_ID            = "id";          //$NON-NLS-1$
  public static final String DB_BUILDING_ID   = "building_id"; //$NON-NLS-1$
  public static final String DB_NAME          = "name";        //$NON-NLS-1$
  public static final String DB_TYPE          = "type";        //$NON-NLS-1$
  public static final String DB_TYPE_VILLAGER = "villager";    //$NON-NLS-1$
  
  public final int      id;
  public final int      building_id;
  public final String   name;
  public final TYPE     type;
  
  public Unit(int building, String name, TYPE type) {
    this(0, building, name, type);
  }
  
  public Unit(int id, int building, String name, TYPE type) {
    this.id          = id;
    this.building_id = building;
    this.name        = name;
    this.type        = type;
  }
  
  public enum TYPE {
    VILLAGER;
    
    public static TYPE fromString(String name) {
      switch(name) {
        case DB_TYPE_VILLAGER: return VILLAGER;
      }
      
      return null;
    }
  }
}