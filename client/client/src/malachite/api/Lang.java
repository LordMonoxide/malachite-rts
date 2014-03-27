package malachite.api;

import java.util.Map;

import malachite.engine.net.http.Response;

public class Lang {
  public static final Lang Menu = new Lang();
  
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
  
  public String get(String key) {
    return _lang.get(key);
  }
  
  public enum MenuKeys {
    LOGIN_TITLE   ("login.title"),    //$NON-NLS-1$
    LOGIN_EMAIL   ("login.email"),    //$NON-NLS-1$
    LOGIN_PASS    ("login.pass"),     //$NON-NLS-1$
    LOGIN_REMEMBER("login.remember"), //$NON-NLS-1$
    
    REGISTER_TITLE("register.title"); //$NON-NLS-1$
    
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