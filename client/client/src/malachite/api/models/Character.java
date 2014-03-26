package malachite.api.models;

public class Character {
  public final String name;
  public final String race;
  public final String sex;
  
  public Character(String name, String race, String sex) {
    this.name = name;
    this.race = race;
    this.sex  = sex;
  }
}