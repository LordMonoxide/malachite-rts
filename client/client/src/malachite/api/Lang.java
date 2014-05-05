package malachite.api;

import java.util.Map;

import org.json.JSONException;

import malachite.engine.net.http.Response;

public class Lang<T> {
  public static final Lang<AppKeys>  App  = new Lang<>();
  public static final Lang<MenuKeys> Menu = new Lang<>();
  public static final Lang<GameKeys> Game = new Lang<>();
  
  public static void load() {
    System.out.println("Getting lang..."); //$NON-NLS-1$
    
    APIFuture.await(
      API.lang(API.Route.Lang.App, new API.LangResponse() {
        @Override public void success(Map<String, String> lang) {
          App._lang = lang;
        }
        
        @Override public void error(Response r) {
          System.err.println(r.content());
        }
        
        @Override public void jsonError(Response r, JSONException e) {
          System.err.println("JSON encoding error getting app lang:\n" + e + '\n' + r.content()); //$NON-NLS-1$
        }
      }),
      
      API.lang(API.Route.Lang.Menu, new API.LangResponse() {
        @Override public void success(Map<String, String> lang) {
          Menu._lang = lang;
        }
        
        @Override public void error(Response r) {
          System.err.println(r.content());
        }
        
        @Override public void jsonError(Response r, JSONException e) {
          System.err.println("JSON encoding error getting menu lang:\n" + e + '\n' + r.content()); //$NON-NLS-1$
        }
      }),
      
      API.lang(API.Route.Lang.Game, new API.LangResponse() {
        @Override public void success(Map<String, String> lang) {
          Game._lang = lang;
        }
        
        @Override public void error(Response r) {
          System.err.println(r.content());
        }
        
        @Override public void jsonError(Response r, JSONException e) {
          System.err.println("JSON encoding error getting menu lang:\n" + e + '\n' + r.content()); //$NON-NLS-1$
        }
      })
    );
  }
  
  private Map<String, String> _lang;
  
  private Lang() { }
  
  public String get(T key, String... substitute) {
    if(_lang == null) {
      System.err.println("Missing lang"); //$NON-NLS-1$
      return key.toString();
    }
    
    String lang = _lang.get(key.toString());
    
    if(lang == null) {
      System.err.println("Missing lang mapping for " + key); //$NON-NLS-1$
      return key.toString();
    }
    
    for(String s : substitute) {
      lang = lang.replaceFirst(":\\w+", s); //$NON-NLS-1$
    }
    
    return lang;
  }
  
  public enum AppKeys {
    TITLE("title"); //$NON-NLS-1$
    
    String _text;
    
    AppKeys(String text) {
      _text = text;
    }
    
    @Override
    public String toString() {
      return _text;
    }
  }
  
  public enum MenuKeys {
    STATUS_LOADING     ("status.loading"),      //$NON-NLS-1$
    STATUS_CONNECTING  ("status.connecting"),   //$NON-NLS-1$
    STATUS_LOGGINGIN   ("status.loggingin"),    //$NON-NLS-1$
    STATUS_LOGGINGOUT  ("status.loggingout"),   //$NON-NLS-1$
    STATUS_REGISTERING ("status.registering"),  //$NON-NLS-1$
    STATUS_UNLOCKING   ("status.unlocking"),    //$NON-NLS-1$
    STATUS_GETTINGCHARS("status.gettingchars"), //$NON-NLS-1$
    STATUS_GETTINGRACES("status.gettingraces"), //$NON-NLS-1$
    STATUS_CREATINGCHAR("status.creatingchar"), //$NON-NLS-1$
    STATUS_DELETINGCHAR("status.deletingchar"), //$NON-NLS-1$
    STATUS_LOADINGGAME ("status.loadinggame"),  //$NON-NLS-1$
    
    NEWS_GETTINGNEWS("news.gettingnews"), //$NON-NLS-1$
    NEWS_NONEWS     ("news.nonews"),      //$NON-NLS-1$
    
    LOGIN_TITLE   ("login.title"),    //$NON-NLS-1$
    LOGIN_EMAIL   ("login.email"),    //$NON-NLS-1$
    LOGIN_PASS    ("login.pass"),     //$NON-NLS-1$
    LOGIN_REMEMBER("login.remember"), //$NON-NLS-1$
    LOGIN_LOGIN   ("login.login"),    //$NON-NLS-1$
    LOGIN_REGISTER("login.register"), //$NON-NLS-1$
    
    REGISTER_TITLE    ("register.title"),     //$NON-NLS-1$
    REGISTER_CREDS    ("register.creds"),     //$NON-NLS-1$
    REGISTER_EMAIL    ("register.email"),     //$NON-NLS-1$
    REGISTER_PASS     ("register.pass"),      //$NON-NLS-1$
    REGISTER_CONFIRM  ("register.confirm"),   //$NON-NLS-1$
    REGISTER_PERSONAL ("register.personal"),  //$NON-NLS-1$
    REGISTER_NAMEFIRST("register.namefirst"), //$NON-NLS-1$
    REGISTER_NAMELAST ("register.namelast"),  //$NON-NLS-1$
    REGISTER_SECURITY ("register.security"),  //$NON-NLS-1$
    REGISTER_QUESTION ("register.question"),  //$NON-NLS-1$
    REGISTER_ANSWER   ("register.answer"),    //$NON-NLS-1$
    REGISTER_SUBMIT   ("register.submit"),    //$NON-NLS-1$
    
    SECURITY_TITLE   ("security.title"),    //$NON-NLS-1$
    SECURITY_WHY     ("security.why"),      //$NON-NLS-1$
    SECURITY_SECURITY("security.security"), //$NON-NLS-1$
    SECURITY_ANSWER  ("security.answer"),   //$NON-NLS-1$
    SECURITY_SUBMIT  ("security.submit"),   //$NON-NLS-1$
    
    MAINMENU_TITLE ("mainmenu.title"),  //$NON-NLS-1$
    MAINMENU_PLAY  ("mainmenu.play"),   //$NON-NLS-1$
    MAINMENU_LOGOUT("mainmenu.logout"), //$NON-NLS-1$
    
    ERROR_ERROR("error.error"), //$NON-NLS-1$
    ERROR_JSON ("error.json");  //$NON-NLS-1$
    
    String _text;
    
    MenuKeys(String text) {
      _text = text;
    }
    
    @Override
    public String toString() {
      return _text;
    }
  }
  
  public enum GameKeys {
    MENU_BUILDINGS_TITLE("menu.buildings.title"), //$NON-NLS-1$
    MENU_PAUSE_TITLE    ("menu.pause.title"),     //$NON-NLS-1$
    MENU_PAUSE_RETURN   ("menu.pause.return"),    //$NON-NLS-1$
    MENU_PAUSE_QUIT     ("menu.pause.quit"),      //$NON-NLS-1$
    
    BUILDING_CAMP_NAME  ("building.camp.name"),   //$NON-NLS-1$
    BUILDING_CAMP_DESC  ("building.camp.desc");   //$NON-NLS-1$
    
    String _text;
    
    GameKeys(String text) {
      _text = text;
    }
    
    @Override
    public String toString() {
      return _text;
    }
  }
}