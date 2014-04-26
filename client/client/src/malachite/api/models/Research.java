package malachite.api.models;

public class Research {
  public static final String DB_ID          = "id";          //$NON-NLS-1$
  public static final String DB_BUILDING_ID = "building_id"; //$NON-NLS-1$
  public static final String DB_NAME        = "name";        //$NON-NLS-1$
  
  public final int      id;
  public final int      building_id;
  public final String   name;
  
  public Research(int building, String name) {
    this(0, building, name);
  }
  
  public Research(int id, int building, String name) {
    this.id          = id;
    this.building_id = building;
    this.name        = name;
  }
}