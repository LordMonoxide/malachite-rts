package malachite.api.models;

import org.json.JSONArray;
import org.json.JSONObject;

public class Settings {
  public static final String DB_ID          = "id";          //$NON-NLS-1$
  public static final String DB_NAME        = "name";        //$NON-NLS-1$
  public static final String DB_BUILDINGS   = "buildings";   //$NON-NLS-1$
  public static final String DB_UNITS       = "units";       //$NON-NLS-1$
  public static final String DB_BUILDING_ID = "building_id"; //$NON-NLS-1$
  public static final String DB_COUNT       = "count";       //$NON-NLS-1$
  
  public final int    id;
  public final String name;
  public final int    units;
  
  public final Building[] building;
  
  public Settings(JSONObject j) {
    id    = j.getInt   (DB_ID);
    name  = j.getString(DB_NAME);
    units = j.getInt   (DB_UNITS);
    
    JSONArray b = j.getJSONArray(DB_BUILDINGS);
    
    building = new Building[b.length()];
    
    for(int i = 0; i < building.length; i++) {
      building[i] = new Building(b.getJSONObject(i));
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
}