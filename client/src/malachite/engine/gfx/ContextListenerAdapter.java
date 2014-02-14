package malachite.engine.gfx;

public class ContextListenerAdapter implements ContextListener {
  @Override
  public void onCreate() {

  }

  @Override
  public void onRun() {

  }

  @Override
  public void onResize() {

  }

  @Override
  public ShouldClose onClosing() {
    return ShouldClose.SHOULD_CLOSE;
  }

  @Override
  public void onClosed() {

  }
}
