package malachite.engine.gfx;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class Manager {
  private static List<Class<? extends AbstractContext>> _contexts = new ArrayList<>();

  private static AbstractContext _context;

  public static AbstractContext getContext() {
    return _context;
  }

  public static void registerContext(@NotNull Class<? extends AbstractContext> context) {
    _contexts.add(context);
  }

  public static AbstractContext create(@Nullable ContextInitializer initializer) {
    for(Class<? extends AbstractContext> c : _contexts) {
      try {
        _context = c.newInstance();

        if(initializer != null) {
          initializer.initialize(_context);
        }

        _context.create();
        return _context;
      } catch(InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  private Manager() { }

  public interface ContextInitializer {
    void initialize(AbstractContext ctx);
  }
}
