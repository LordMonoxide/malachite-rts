package malachite.api.models;

public class Race {
  public static final String ID   = "id";   //$NON-NLS-1$
  public static final String NAME = "name"; //$NON-NLS-1$
  
  public final int    id;
  public final String name;
  
  public Race(String name) {
    this(0, name);
  }
  
  public Race(int id, String name) {
    this.id   = id;
    this.name = name;
  }
}