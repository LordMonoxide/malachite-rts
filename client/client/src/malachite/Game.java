package malachite;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import malachite.api.API;
import malachite.api.Lang;
import malachite.api.Lang.AppKeys;
import malachite.api.models.News;
import malachite.buildings.Building;
import malachite.buildings.Buildings;
import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.ContextListenerAdapter;
import malachite.engine.gfx.Loader;
import malachite.engine.gfx.Manager;
import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.TextStream;
import malachite.engine.gfx.gl14.Context;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.net.http.Request;
import malachite.engine.net.http.Response;
import malachite.engine.physics.Movable;
import malachite.engine.physics.Sandbox;
import malachite.gui.MainMenu;
import malachite.pathfinding.Pathfinder;
import malachite.pathfinding.Point;
import malachite.world.BuildingEntity;
import malachite.world.Entity;
import malachite.world.UnitEntity;
import malachite.world.World;
import malachite.world.generators.Rivers;

public class Game {
  public static void main(String... args) {
    new Game().initialize();
  }
  
  private static Random _random = new Random(); //TODO: seed
  
  private AbstractContext _context;
  
  private MenuInterface _menu;
  private GameInterface _game;
  
  private World _world;
  private ArrayList<Player> _player = new ArrayList<>();
  
  private Sandbox    _sandbox = new Sandbox();
  private Pathfinder _pathfinder = new Pathfinder();
  
  public void initialize() {
    Request.init();
    Lang.load();
    
    Manager.registerContext(Context.class);
    _context = Manager.create(ctx -> {
      ctx.setTitle(Lang.App.get(AppKeys.TITLE));
      ctx.setResizable(true);
      ctx.setWH(1280, 720);
      ctx.setFPSTarget(60);
      ctx.setContextListener(new ContextListenerAdapter() {
        @Override
        public void onRun() {
          _context.addLoadCallback(Loader.LoaderThread.GRAPHICS, () -> {
            _menu = new MainMenu(new MenuProxy());
            ((AbstractGUI)_menu).push();
            ((AbstractGUI)_menu).events().addLoadHandler(() -> {
              checkLogin();
            });
          });
        }
        
        @Override
        public void onClosed() {
          Request.destroy();
          _sandbox.stop();
        }
      });
    });

    _context.run();
  }
  
  private void checkLogin() {
    class R extends GenericResponse implements API.CheckResponse {
      @Override public void loggedIn() {
        _menu.loginSuccess();
      }
    }
    
    _menu.checkingLogin();
    API.Auth.check(new R());
  }
  
  private void initGame() {
    ((AbstractGUI)_menu).pop();
    ((AbstractGUI)_menu).destroy();
    _menu = null;
    
    Player p = addPlayer();
    _world = new Rivers().generate();
    _game  = new malachite.gui.Game(new GameProxy(p), _world);
    
    ((AbstractGUI)_game).push();
    ((AbstractGUI)_game).events().addLoadHandler(() -> {
      _sandbox.start();
      initPlayer(p);
    });
  }
  
  private Player addPlayer() {
    Player p = new Player();
    _player.add(p);
    return p;
  }
  
  private void initPlayer(Player p) {
    int startX = _random.nextInt(640) + 320;
    int startY = _random.nextInt(360) + 180;
    
    BuildingEntity b = addBuilding(p, Buildings.base, startX, startY);
    b.finish();
    
    for(int i = 0; i < 3; i++) {
      addUnit(p, b.trainUnit());
    }
  }
  
  private UnitEntity addUnit(Player p, UnitEntity e) {
    p.addUnit(e);
    _world.addEntity(e);
    _game.addEntity(e);
    _sandbox.add(e);
    return e;
  }
  
  private BuildingEntity addBuilding(Player p, Building b, float x, float y) {
    BuildingEntity e = (BuildingEntity)b.createEntity();
    p.addBuilding(e);
    e.setX(x);
    e.setY(y);
    _world.addEntity(e);
    _game.addEntity(e);
    return e;
  }
  
  public class MenuProxy {
    public void quit() {
      _context.destroy();
    }
    
    public void register(String email, String pass, String pass2, String nameFirst, String nameLast, API.SecurityQuestion... security) {
      class R extends GenericResponse implements API.RegisterResponse {
        @Override public void success() {
          _menu.registerSuccess();
          _menu.loginSuccess();
        }
        
        @Override public void invalid(JSONObject errors) {
          _menu.registerError(errors);
        }
      }
      
      _menu.registering();
      API.Auth.register(email, pass, pass2, nameFirst, nameLast, new R(), security);
    }
    
    public void login(String email, String pass, boolean remember) {
      class R extends GenericResponse implements API.LoginResponse {
        @Override public void success() {
          _menu.loginSuccess();
        }
        
        @Override public void invalid(JSONObject errors) {
          _menu.loginError(errors);
        }
      }
      
      _menu.loggingIn();
      API.Auth.login(email, pass, remember, new R());
    }
    
    public void logout() {
      class R extends GenericResponse implements API.LogoutResponse {
        @Override public void success() {
          _menu.showLogin();
        }
      }
      
      _menu.reset();
      API.Auth.logout(new R());
    }
    
    public void unlock(String... security) {
      class R extends GenericResponse implements API.UnlockResponse {
        @Override public void success() {
          _menu.securitySuccess();
        }

        @Override public void invalid(JSONObject errors) {
          _menu.securityError(errors);
        }
      }
      
      _menu.securing();
      API.Auth.unlock(new R(), security);
    }
    
    public void requestNews() {
      class R extends ErrorResponse implements API.NewsLatestResponse {
        @Override public void success(News news) {
          if(news != null) {
            System.out.println(news.title + '\n' + news.body);
            
            TextStream ts = new TextStream();
            ts.insert(ts.face(Font.FONT_FACE.BOLD));
            ts.insert(news.title);
            ts.insert(ts.face(Font.FONT_FACE.REGULAR));
            ts.insert(ts.newLine());
            ts.insert(news.body);
            _menu.showNews(ts);
          } else {
            _menu.noNews();
          }
        }
      }
      
      _menu.gettingNews();
      API.Storage.News.latest(new R());
    }
    
    public void play() {
      _menu.loadingGame();
      initGame();
    }
  }
  
  public interface MenuInterface {
    public void reset();
    
    public void checkingLogin();
    
    public void gettingNews();
    public void showNews(TextStream news);
    public void noNews();
    
    public void showLogin();
    public void loggingIn();
    public void loginSuccess();
    public void loginError(JSONObject errors);
    
    public void showRegister();
    public void registering();
    public void registerSuccess();
    public void registerError(JSONObject errors);
    
    public void showSecurity(String[] questions);
    public void securing();
    public void securitySuccess();
    public void securityError(JSONObject errors);
    
    public void loadingGame();
    
    public void showError    (Response r);
    public void showJSONError(Response r, JSONException e);
  }
  
  public class GameProxy {
    public final Player player;
    
    private GameProxy(Player player) {
      this.player = player;
    }
    
    public void quit() {
      _context.destroy();
    }
    
    //TODO: This should be a logic callback
    public void logic() {
      for(UnitEntity unit : player.units) {
        unit.logic();
      }
    }
    
    public void moveEntity(Entity entity, Point destination) { moveEntity(entity, destination, null); }
    public void moveEntity(Entity entity, Point destination, Movable.Callback onReachDestination) {
      entity.moveAlong(_pathfinder.findPath(new Point(entity.getX(), entity.getY()), destination), onReachDestination);
    }
    
    public void trainUnit(BuildingEntity building) {
      addUnit(player, building.trainUnit());
    }
    
    public void placeFoundation(Building building, float x, float y, UnitEntity... builders) {
      BuildingEntity e = addBuilding(player, building, x, y);
      constructBuilding(e, builders);
    }
    
    public void constructBuilding(BuildingEntity building, UnitEntity... builders) {
      for(UnitEntity builder : builders) {
        moveEntity(builder, new Point(building.getX(), building.getY()), () -> {
          builder.construct(building);
        });
      }
    }
  }
  
  public interface GameInterface {
    public void addEntity(Entity entity);
  }
  
  private class ErrorResponse implements API.ErrorResponse {
    @Override public void error(Response r) {
      _menu.showError(r);
    }
    
    @Override public void jsonError(Response r, JSONException e) {
      _menu.showJSONError(r, e);
    }
  }
  
  private class GenericResponse extends ErrorResponse implements API.GenericResponse {
    @Override //TODO
    public void loginRequired() {
      _menu.showLogin();
    }
    
    @Override
    public void securityRequired(String[] questions) {
      _menu.showSecurity(questions);
    }
  }
}
