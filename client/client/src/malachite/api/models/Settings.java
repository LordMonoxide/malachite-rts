package malachite.api.models;

import org.json.JSONArray;
import org.json.JSONObject;

public class Settings {
  public static final String DB_ID          = "id";          //$NON-NLS-1$
  public static final String DB_NAME        = "name";        //$NON-NLS-1$
  public static final String DB_BUILDINGS   = "buildings";   //$NON-NLS-1$
  public static final String DB_UNITS       = "units";       //$NON-NLS-1$
  public static final String DB_BUILDING_ID = "building_id"; //$NON-NLS-1$
  public static final String DB_UNIT_ID     = "unit_id";     //$NON-NLS-1$
  public static final String DB_COUNT       = "count";       //$NON-NLS-1$
  
  public final int    id;
  public final String name;
  
  public final Building[] building;
  public final Unit    [] unit;
  
  public Settings(JSONObject j) {
    id   = j.getInt   (DB_ID);
    name = j.getString(DB_NAME);
    
    JSONArray b = j.getJSONArray(DB_BUILDINGS);
    JSONArray u = j.getJSONArray(DB_UNITS);
    
    building = new Building[b.length()];
    unit     = new Unit    [u.length()];
    
    for(int i = 0; i < building.length; i++) {
      building[i] = new Building(b.getJSONObject(i));
    }
    
    for(int i = 0; i < unit.length; i++) {
      unit[i] = new Unit(u.getJSONObject(i));
    }
  }
  
  public class Building {
    public final int id;
    public final int building_id;
    public final int count;
    
    private Building(JSONObject j) {
      id          = j.getInt(DB_ID);
      building_id = j.getInt(DB_BUILDING_ID);
      count       = j.getInt(DB_COUNT);
    }
  }
  
  public class Unit {
    public final int id;
    public final int unit_id;
    public final int count;
    
    private Unit(JSONObject j) {
      id      = j.getInt(DB_ID);
      unit_id = j.getInt(DB_UNIT_ID);
      count   = j.getInt(DB_COUNT);
    }
  }
}