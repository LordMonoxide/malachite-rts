package malachite.engine.util;

import org.lwjgl.Sys;

public class Time {
  public static double get() {
    return Sys.getTime();
  }

  public static double HzToTicks(double hz) {
    return 1000 / hz * Sys.getTimerResolution() / 1000;
  }

  private Time() { }
}
