package malachite.engine.util;

import org.lwjgl.Sys;

public final class Time {
  public static double get() {
    return Sys.getTime();
  }

  public static double HzToTicks(double hz) {
    return 1000 / hz * Sys.getTimerResolution() / 1000;
  }

  public static double MSToTicks(double ms) {
    return ms * Sys.getTimerResolution() / 1000;
  }

  private Time() { }
}
