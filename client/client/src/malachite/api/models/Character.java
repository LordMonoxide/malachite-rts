package malachite.api.models;

public class Character {
  public static final String ID   = "id";   //$NON-NLS-1$
  public static final String NAME = "name"; //$NON-NLS-1$
  public static final String RACE = "race"; //$NON-NLS-1$
  public static final String SEX  = "sex";  //$NON-NLS-1$
  
  public final int    id;
  public final String name;
  public final Race   race;
  public final String sex;
  
  public Character(String name, Race race, String sex) {
    this(0, name, race, sex);
  }
  
  public Character(int id, String name, Race race, String sex) {
    this.id   = id;
    this.name = name;
    this.race = race;
    this.sex  = sex;
  }
}