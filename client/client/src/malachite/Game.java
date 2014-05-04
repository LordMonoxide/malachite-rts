package malachite;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import malachite.api.API;
import malachite.api.APIFuture;
import malachite.api.Lang;
import malachite.api.Lang.AppKeys;
import malachite.api.Lang.MenuKeys;
import malachite.api.models.Building;
import malachite.api.models.News;
import malachite.api.models.Research;
import malachite.api.models.Settings;
import malachite.api.models.Unit;
import malachite.buildings.AbstractBuilding;
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
import malachite.engine.physics.Sandbox;
import malachite.gui.MainMenu;
import malachite.pathfinding.Pathfinder;
import malachite.pathfinding.Point;
import malachite.units.AbstractUnit;
import malachite.world.Entity;
import malachite.world.World;
import malachite.world.generators.Rivers;

public class Game {
  public static void main(String... args) {
    new Game().initialize();
  }
  
  private static Random _random = new Random(); //TODO: seed
  
  private Game _this = this;
  
  private AbstractContext _context;
  
  private MenuInterface _menu;
  private GameInterface _game;
  
  private Building[] _building;
  private Research[] _research;
  private Unit    [] _unit;
  
  private Settings _settings;
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
    MessageInterface m = _menu.showMessage(MenuKeys.STATUS_LOADING, MenuKeys.STATUS_CONNECTING);
    
    class R extends GenericResponse implements API.CheckResponse {
      R() { super(m); }
      
      @Override public void loggedIn() {
        m.hide();
        _menu.showMainMenu();
      }
    }
    
    API.Auth.check(new R());
  }
  
  private APIFuture loadBuildings() {
    class R extends GenericResponse implements API.BuildingsResponse {
      R() { super(null); }
      
      @Override public void success(Building[] buildings) {
        _building = buildings;
      }
    }
    
    return API.Storage.Tech.buildings(new R());
  }
  
  private APIFuture loadResearch() {
    class R extends GenericResponse implements API.ResearchResponse {
      R() { super(null); }
      
      @Override public void success(Research[] research) {
        _research = research;
      }
    }
    
    return API.Storage.Tech.research(new R());
  }
  
  private APIFuture loadUnits() {
    class R extends GenericResponse implements API.UnitsResponse {
      R() { super(null); }
      
      @Override public void success(Unit[] units) {
        _unit = units;
      }
    }
    
    return API.Storage.Tech.units(new R());
  }
  
  private APIFuture loadSettings() {
    class R extends GenericResponse implements API.SettingsResponse {
      R() { super(null); }
        
      @Override public void success(Settings[] settings) {
        _settings = settings[0]; //TODO: not this
      }
    }
    
    return API.Storage.Settings.all(new R());
  }
  
  private Building buildingByID(int id) {
    for(Building b : _building) {
      if(b.id == id) { return b; }
    }
    
    return null;
  }
  
  private Research researchByID(int id) {
    for(Research r : _research) {
      if(r.id == id) { return r; }
    }
    
    return null;
  }
  
  private Unit unitByID(int id) {
    for(Unit u : _unit) {
      if(u.id == id) { return u; }
    }
    
    return null;
  }
  
  private void initGame() {
    ((AbstractGUI)_menu).pop();
    ((AbstractGUI)_menu).destroy();
    _menu = null;
    
    _world = new Rivers().generate();
    _game  = new malachite.gui.Game(new GameProxy(), _world);
    
    ((AbstractGUI)_game).push();
    ((AbstractGUI)_game).events().addLoadHandler(() -> {
      _sandbox.start();
      addPlayer();
    });
  }
  
  private void addPlayer() {
    Player p = new Player();
    
    int startX = _random.nextInt(640) + 320;
    int startY = _random.nextInt(360) + 180;
    
    for(Settings.Building building : _settings.building) {
      for(int i = 0; i < building.count; i++) {
        //TODO: each building starts at the same loc
        addBuilding(p, AbstractBuilding.Create(buildingByID(building.id), startX, startY));
      }
    }
    
    for(Settings.Unit unit : _settings.unit) {
      for(int i = 0; i < unit.count; i++) {
        double theta = _random.nextDouble() * Math.PI * 2;
        double dist  = _random.nextDouble() * 150 + 100;
        float unitX = (float)(startX + Math.cos(theta) * dist);
        float unitY = (float)(startY + Math.sin(theta) * dist);
        addUnit(p, AbstractUnit.Create(unitByID(unit.id), unitX, unitY));
      }
    }
    
    _player.add(p);
  }
  
  private void addUnit(Player p, malachite.units.AbstractUnit u) {
    p.addUnit(u);
    Entity e = u.createEntity();
    _world.addEntity(e);
    _game.addEntity(e);
    _sandbox.add(e);
  }
  
  private void addBuilding(Player p, malachite.buildings.AbstractBuilding b) {
    p.addBuilding(b);
    Entity e = b.createEntity();
    _world.addEntity(e);
    _game.addEntity(e);
  }
  
  public interface MessageInterface {
    public void update(MenuKeys title, MenuKeys text);
    public void hide();
  }
  
  public class MenuProxy {
    public void quit() {
      _context.destroy();
    }
    
    public void register(String email, String pass, String pass2, String nameFirst, String nameLast, API.SecurityQuestion... security) {
      MessageInterface m = _menu.showMessage(MenuKeys.STATUS_LOADING, MenuKeys.STATUS_REGISTERING);
      _menu.hideRegister();
      
      class R extends GenericResponse implements API.RegisterResponse {
        R() { super(m); }
        
        @Override
        public void success() {
          m.hide();
          _menu.showMainMenu();
        }
        
        @Override public void invalid(JSONObject errors) {
          m.hide();
          _menu.showRegister();
          System.err.println(errors); //TODO
        }
      }
      
      API.Auth.register(email, pass, pass2, nameFirst, nameLast, new R(), security);
    }
    
    public void login(String email, String pass) {
      MessageInterface m = _menu.showMessage(MenuKeys.STATUS_LOADING, MenuKeys.STATUS_LOGGINGIN);
      _menu.hideLogin();
      
      class R extends GenericResponse implements API.LoginResponse {
        R() { super(m); }
        
        @Override public void success() {
          m.hide();
          _menu.showMainMenu();
        }
        
        @Override public void invalid(JSONObject errors) {
          m.hide();
          _menu.showLogin();
          System.err.println(errors); //TODO
        }
      }
      
      API.Auth.login(email, pass, new R());
    }
    
    public void logout() {
      MessageInterface m = _menu.showMessage(MenuKeys.STATUS_LOADING, MenuKeys.STATUS_LOGGINGOUT);
      _menu.reset();
      
      class R extends GenericResponse implements API.LogoutResponse {
        R() { super(m); }
        
        @Override public void success() {
          m.hide();
          _menu.showLogin();
        }
      }
      
      API.Auth.logout(new R());
    }
    
    public void security() {
      
    }
    
    public void unlock(String... security) {
      MessageInterface m = _menu.showMessage(MenuKeys.STATUS_LOADING, MenuKeys.STATUS_UNLOCKING);
      _menu.hideSecurity();
      
      class R extends GenericResponse implements API.UnlockResponse {
        R() { super(m); }
        
        @Override public void success() {
          m.hide();
          _menu.showMainMenu();
        }

        @Override public void invalid(JSONObject errors) {
          m.hide();
          _menu.showSecurity(null);
          System.err.println(errors); //TODO
        }
      }
      
      API.Auth.unlock(new R(), security);
    }
    
    public void requestNews() {
      _menu.gettingNews();
      
      class R extends ErrorResponse implements API.NewsLatestResponse {
        R() { super(null); }
        
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
      
      API.Storage.News.latest(new R());
    }
    
    public void play() {
      _menu.loadingGame();
      
      APIFuture.await(() -> {
        initGame();
      },
        loadBuildings(),
        loadResearch(),
        loadUnits(),
        loadSettings()
      );
    }
  }
  
  public interface MenuInterface {
    public void reset();
    
    public MessageInterface showMessage(MenuKeys title, MenuKeys text);
    
    public void gettingNews();
    public void showNews(TextStream news);
    public void noNews();
    
    public void showLogin();
    public void hideLogin();
    
    public void showRegister();
    public void hideRegister();
    
    public void showSecurity(String[] questions);
    public void hideSecurity();
    
    public void showMainMenu();
    public void hideMainMenu();
    
    public void loadingGame();
    
    public void showError    (Response r);
    public void showJSONError(Response r, JSONException e);
  }
  
  public class GameProxy {
    public void quit() {
      _context.destroy();
    }
    
    public Building buildingByID(int id) {
      return _this.buildingByID(id);
    }
    
    public Research researchByID(int id) {
      return _this.researchByID(id);
    }
    
    public Unit unitByID(int id) {
      return _this.unitByID(id);
    }
    
    public int buildingCount() {
      return _building.length;
    }
    
    public int researchCount() {
      return _research.length;
    }
    
    public int unitCount() {
      return _unit.length;
    }
    
    public void eachBuilding(BuildingIterator it) {
      for(Building building : _building) {
        it.next(building);
      }
    }
    
    public void eachResearch(ResearchIterator it) {
      for(Research research : _research) {
        it.next(research);
      }
    }
    
    public void eachUnit(UnitIterator it) {
      for(Unit unit : _unit) {
        it.next(unit);
      }
    }
    
    public void moveEntity(Entity entity, Point destination) {
      entity.moveAlong(_pathfinder.findPath(new Point(entity.getX(), entity.getY()), destination));
    }
  }
  
  public interface GameInterface {
    public void addEntity(Entity entity);
  }
  
  private class ErrorResponse implements API.ErrorResponse {
    protected final MessageInterface _message;
    
    private ErrorResponse(MessageInterface message) {
      _message = message;
    }
    
    @Override public void error(Response r) {
      _menu.showError(r);
    }
    
    @Override public void jsonError(Response r, JSONException e) {
      _menu.showJSONError(r, e);
    }
  }
  
  private class GenericResponse extends ErrorResponse implements API.GenericResponse {
    private GenericResponse(MessageInterface message) {
      super(message);
    }
    
    @Override
    public void loginRequired() {
      _message.hide();
      _menu.showLogin();
    }
    
    @Override
    public void securityRequired(String[] questions) {
      _message.hide();
      _menu.showSecurity(questions);
    }
  }
  
  public interface BuildingIterator {
    void next(Building building);
  }
  
  public interface ResearchIterator {
    void next(Research research);
  }
  
  public interface UnitIterator {
    void next(Unit unit);
  }
}
