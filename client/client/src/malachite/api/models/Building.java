package malachite.api.models;

public class Building {
  public static final String ID   = "id";   //$NON-NLS-1$
  public static final String NAME = "name"; //$NON-NLS-1$
  public static final String TYPE = "type"; //$NON-NLS-1$
  
  public final int    id;
  public final String name;
  public final String type;
  
  public Building(String name, String type) {
    this(0, name, type);
  }
  
  public Building(int id, String name, String type) {
    this.id   = id;
    this.name = name;
    this.type = type;
  }
}