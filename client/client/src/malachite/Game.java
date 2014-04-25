package malachite;

import org.json.JSONException;
import org.json.JSONObject;

import malachite.api.API;
import malachite.api.Lang;
import malachite.api.Lang.AppKeys;
import malachite.api.Lang.MenuKeys;
import malachite.api.models.Building;
import malachite.api.models.News;
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
import malachite.gui.MainMenu;
import malachite.world.World;
import malachite.world.generators.Rivers;

public class Game {
  public static void main(String... args) {
    new Game().initialize();
  }

  private AbstractContext _context;
  
  private MenuInterface _menu;
  private GameInterface _game;
  
  private World _world;
  
  private Building[] _building;

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
  
  public interface MessageInterface {
    public void update(MenuKeys title, MenuKeys text);
    public void hide();
  }
  
  public class MenuProxy {
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
          System.err.println(errors);
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
    
    public void requestNews() {
      _menu.gettingNews();
      
      API.Storage.News.latest(new API.NewsLatestResponse() {
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
        
        @Override public void jsonError(Response r, JSONException e) {
          TextStream ts = new TextStream();
          ts.insert(ts.face(Font.FONT_FACE.BOLD));
          ts.insert("JSON error:");
          ts.insert(ts.face(Font.FONT_FACE.REGULAR));
          ts.insert(ts.newLine());
          ts.insert(r.toString());
          ts.insert(ts.newLine());
          ts.insert(e.toString());
          _menu.showNews(ts);
        }
        
        @Override public void error(Response r) {
          TextStream ts = new TextStream();
          ts.insert(ts.face(Font.FONT_FACE.BOLD));
          ts.insert("Error:");
          ts.insert(ts.face(Font.FONT_FACE.REGULAR));
          ts.insert(ts.newLine());
          ts.insert(r.toString());
          _menu.showNews(ts);
        }
      });
    }
    
    public void play() {
      ((AbstractGUI)_menu).pop();
      _menu = null;
      
      _world = new Rivers().generate();
      
      _game = new malachite.gui.Game(_world);
      ((AbstractGUI)_game).push();
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
    
    public void showSecurity();
    public void hideSecurity();
    
    public void showMainMenu();
    public void hideMainMenu();
  }
  
  public class GameProxy {
    
  }
  
  public interface GameInterface {
    
  }
  
  private class GenericResponse implements API.GenericResponse {
    private final MessageInterface _message;
    
    private GenericResponse(MessageInterface message) {
      _message = message;
    }
    
    @Override
    public void loginRequired() {
      _message.hide();
      _menu.showLogin();
    }
    
    @Override
    public void securityRequired() {
      _message.hide();
      _menu.showSecurity();
    }
    
    @Override
    public void error(Response r) {
      System.err.println("Error " + r);
      //TODO: _message.update("Error " + r);
    }
    
    @Override
    public void jsonError(Response r, JSONException e) {
      System.err.println("Enconding error " + e + ' ' + r);
      //TODO: _message.update("Encoding error: " + e + ' ' + r);
    }
  }
}
