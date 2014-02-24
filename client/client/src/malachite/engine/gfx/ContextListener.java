package malachite.engine.gfx;

public interface ContextListener {
  void onCreate();
  void onRun();
  void onResize();
  ShouldClose onClosing();
  void onClosed();

  enum ShouldClose {
    SHOULD_CLOSE,
    SHOULD_NOT_CLOSE
  }
}