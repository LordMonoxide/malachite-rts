package malachite.api;

import java.util.Map;

import malachite.engine.net.http.Response;

public class Lang<T> {
  public static final Lang<MenuKeys> Menu = new Lang<>();
  
  public static void load() {
    System.out.println("Getting lang..."); //$NON-NLS-1$
    
    API.langMenu(new API.LangResponse() {
      @Override public void success(Map<String, String> lang) {
        Menu._lang = lang;
      }
      
      @Override public void error(Response r) {
        System.err.println(r.content());
      }
    });
    
    while(Menu._lang == null) {
      try {
        Thread.sleep(10);
      } catch(InterruptedException e) { }
    }
  }
  
  private Map<String, String> _lang;
  
  private Lang() { }
  
  public String get(T key, String... substitute) {
    String lang = _lang.get(key.toString());
    
    for(String s : substitute) {
      lang = lang.replaceFirst(":\\w+", s); //$NON-NLS-1$
    }
    
    return lang;
  }
  
  public enum MenuKeys {
    LOGIN_TITLE   ("login.title"),    //$NON-NLS-1$
    LOGIN_EMAIL   ("login.email"),    //$NON-NLS-1$
    LOGIN_PASS    ("login.pass"),     //$NON-NLS-1$
    LOGIN_REMEMBER("login.remember"), //$NON-NLS-1$
    
    REGISTER_TITLE  ("register.title"),   //$NON-NLS-1$
    REGISTER_EMAIL  ("register.email"),   //$NON-NLS-1$
    REGISTER_PASS   ("register.pass"),    //$NON-NLS-1$
    REGISTER_CONFIRM("register.confirm"), //$NON-NLS-1$
    
    CHARS_TITLE("chars.title"), //$NON-NLS-1$
    CHARS_NEW  ("chars.new"),   //$NON-NLS-1$
    CHARS_DEL  ("chars.del"),   //$NON-NLS-1$
    CHARS_USE  ("chars.use"),   //$NON-NLS-1$
    CHARS_LIST ("chars.list");  //$NON-NLS-1$
    
    String _text;
    
    MenuKeys(String text) {
      _text = text;
    }
    
    @Override
    public String toString() {
      return _text;
    }
  }
}