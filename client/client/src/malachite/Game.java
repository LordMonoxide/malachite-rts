package malachite;

import malachite.api.Lang;
import malachite.api.Lang.AppKeys;
import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.ContextListenerAdapter;
import malachite.engine.gfx.Loader;
import malachite.engine.gfx.Manager;
import malachite.engine.gfx.gl14.Context;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.net.http.Request;
import malachite.gui.MainMenu;
import malachite.world.World;
import malachite.world.generators.Rivers;

public class Game {
  public static void main(String... args) {
    new Game().initialize();
  }

  private AbstractContext _context;
  
  private AbstractGUI _interface;
  
  private World _world;

  public void initialize() {
    Request.init();
    Lang.load();
    
    Manager.registerContext(Context.class);
    _context = Manager.create(ctx -> {
      ctx.setTitle(Lang.App.get(AppKeys.TITLE));
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
        _interface = new MainMenu(new MenuProxy());
        _interface.push();
      });
    }
    
    @Override
    public void onClosed() {
      Request.destroy();
    }
  }
  
  public class MenuProxy {
    public void play() {
      _interface.pop();
      
      _world = new Rivers().generate();
      
      _interface = new malachite.gui.Game(_world);
      _interface.push();
    }
  }
  
  public class GameProxy {
    
  }
}
