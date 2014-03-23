package malachite;

import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.ContextListenerAdapter;
import malachite.engine.gfx.Loader;
import malachite.engine.gfx.Manager;
import malachite.engine.gfx.gl14.Context;
import malachite.engine.net.http.Request;
import malachite.gui.MainMenu;

public class Game {
  public static void main(String... args) {
    new Game().initialize();
  }

  private AbstractContext _context;

  public void initialize() {
    Request.init();
    Manager.registerContext(Context.class);
    _context = Manager.create(ctx -> {
      ctx.setTitle("Engine Test");
      ctx.setResizable(true);
      ctx.setWH(1280, 720);
      ctx.setFPSTarget(60);
      ctx.setContextListener(new Listener());
    });

    _context.run();
  }

  private class Listener extends ContextListenerAdapter {
    @Override
    public void onRun() {
      _context.addLoadCallback(Loader.LoaderThread.GRAPHICS, () -> {
        _context.GUIs().push(new MainMenu());
      });
    }
    
    @Override
    public void onClosed() {
      Request.destroy();
    }
  }
}
