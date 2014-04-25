package malachite.api;

public class APIFuture {
  private boolean _done;
  
  public boolean isDone() {
    return _done;
  }
  
  void complete() {
    _done = true;
  }
  
  public void await() {
    while(!_done) {
      try {
        Thread.sleep(10);
      } catch(InterruptedException e) { }
    }
  }
  
  public static void await(APIFuture... futures) {
    boolean done = false;
    
    while(!done) {
      done = true;
      
      for(APIFuture f : futures) {
        done = f._done ? done : false;
      }
      
      try {
        Thread.sleep(10);
      } catch(InterruptedException e) { }
    }
  }
  
  public static void await(Callback cb, APIFuture... futures) {
    new Thread(new Runnable() {
      @Override public void run() {
        await(futures);
        cb.execute();
      }
    }).start();
  }
  
  public interface Callback {
    void execute();
  }
}