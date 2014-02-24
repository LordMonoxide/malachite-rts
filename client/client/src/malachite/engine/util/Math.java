package malachite.engine.util;

public class Math {
  public static int nextPowerOfTwo(int number) {
    int power = 2;

    while(power < number) {
      power *= 2;
    }

    return power;
  }
}
