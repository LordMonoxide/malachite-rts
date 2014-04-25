package malachite.api.models;

public class Building {
  public static final String DB_ID   = "id";   //$NON-NLS-1$
  public static final String DB_NAME = "name"; //$NON-NLS-1$
  public static final String DB_TYPE = "type"; //$NON-NLS-1$
  public static final String DB_TYPE_CAMP       = "camp"; //$NON-NLS-1$
  public static final String DB_TYPE_FOODSTORE  = "foodstore"; //$NON-NLS-1$
  public static final String DB_TYPE_WOODSTORE  = "woodstore"; //$NON-NLS-1$
  public static final String DB_TYPE_METALSTORE = "metalstore"; //$NON-NLS-1$
  public static final String DB_TYPE_HOUSING    = "housing"; //$NON-NLS-1$
  
  public final int    id;
  public final String name;
  public final TYPE   type;
  
  public Building(String name, TYPE type) {
    this(0, name, type);
  }
  
  public Building(int id, String name, TYPE type) {
    this.id   = id;
    this.name = name;
    this.type = type;
  }
  
  public enum TYPE {
    CAMP, FOODSTORE, WOODSTORE, METALSTORE, HOUSING;
    
    public static TYPE fromString(String name) {
      switch(name) {
        case DB_TYPE_CAMP:       return CAMP;
        case DB_TYPE_FOODSTORE:  return FOODSTORE;
        case DB_TYPE_WOODSTORE:  return WOODSTORE;
        case DB_TYPE_METALSTORE: return METALSTORE;
        case DB_TYPE_HOUSING:    return HOUSING;
      }
      
      return null;
    }
  }
}