package malachite.api.models;

public class Character {
  public static final String NAME = "name"; //$NON-NLS-1$
  public static final String RACE = "race"; //$NON-NLS-1$
  public static final String SEX  = "sex"; //$NON-NLS-1$
  
  public final String name;
  public final Race race;
  public final String sex;
  
  public Character(String name, Race race, String sex) {
    this.name = name;
    this.race = race;
    this.sex  = sex;
  }
}