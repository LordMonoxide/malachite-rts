package malachite.api;

import malachite.api.models.Character;
import malachite.api.models.Race;
import malachite.api.models.User;
import malachite.engine.net.http.Request;
import malachite.engine.net.http.Response;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class API {
  private API() { }
  
  private static final String APPLICATION_JSON = "application/json"; //$NON-NLS-1$
  
  private static final String NOT_AUTHED_SHOW          = "show";     //$NON-NLS-1$
  private static final String NOT_AUTHED_SHOW_LOGIN    = "login";    //$NON-NLS-1$
  private static final String NOT_AUTHED_SHOW_SECURITY = "security"; //$NON-NLS-1$
  
  private static void dispatch(Route route, Request.Callback resp) {
    dispatch(route, null, resp);
  }
  
  private static void dispatch(Route route, Map<String, String> data, Request.Callback resp) {
    Request r = new Request();
    r.setMethod(route.method);
    
    try {
      r.setRoute(route.route);
    } catch(URISyntaxException e) { }
    
    r.addHeader(HttpHeaders.Names.ACCEPT, APPLICATION_JSON);
    r.setData(data);
    r.dispatch(resp);
  }
  
  private static void checkGeneric(Response resp, GenericResponse cb) {
    if(resp.response().getStatus().code() == 401) {
      JSONObject j = new JSONObject(resp.content());
      switch(j.getString(NOT_AUTHED_SHOW)) {
        case NOT_AUTHED_SHOW_LOGIN:    cb.loginRequired();    break;
        case NOT_AUTHED_SHOW_SECURITY: cb.securityRequired(); break;
        default:         cb.error(resp);
      }
    } else {
      cb.error(resp);
    }
  }
  
  public static final class Auth {
    private Auth() { }
    
    public static void check(CheckResponse cb) {
      dispatch(Route.Auth.Check, resp -> {
        try {
          if(resp.succeeded()) {
            cb.loggedIn();
          } else {
            checkGeneric(resp, cb);
          }
        } catch(JSONException e) {
          cb.jsonError(resp, e);
        }
      });
    }
  
    public static void login(String email, String password, LoginResponse cb) {
      Map<String, String> data = new HashMap<>();
      data.put(User.EMAIL,    email);
      data.put(User.PASSWORD, password);
      
      dispatch(Route.Auth.Login, data, resp -> {
        try {
          if(resp.succeeded()) {
            cb.success();
          } else {
            if(resp.response().getStatus().code() == 409) {
              cb.invalid(new JSONObject(resp.content()));
            } else {
              checkGeneric(resp, cb);
            }
          }
        } catch(JSONException e) {
          cb.jsonError(resp, e);
        }
      });
    }
  }
  
  public static final class Storage {
    private Storage() { }
    
    public static final class Characters {
      private Characters() { }
      
      public static void all(CharactersAllResponse cb) {
        dispatch(Route.Storage.Characters.All, resp -> {
          try {
            if(resp.succeeded()) {
              JSONArray j = new JSONArray(resp.content());
              
              Character[] characters = new Character[j.length()];
              
              for(int i = 0; i < j.length(); i++) {
                JSONObject c = j.getJSONObject(i);
                characters[i] = new Character(c.getInt(Character.ID), c.getString(Character.NAME), new Race(c.getString(Character.RACE)), c.getString(Character.SEX));
              }
              
              cb.success(characters);
            } else {
              checkGeneric(resp, cb);
            }
          } catch(JSONException e) {
            cb.jsonError(resp, e);
          }
        });
      }
      
      public static void create(Character character, CharactersCreateResponse cb) {
        Map<String, String> data = new HashMap<>();
        data.put(Character.NAME, character.name);
        data.put(Character.RACE, Integer.toString(character.race.id));
        data.put(Character.SEX , character.sex);
        
        dispatch(Route.Storage.Characters.Create, data, resp -> {
          try {
            if(resp.succeeded()) {
              cb.success();
            } else {
              if(resp.response().getStatus().code() == 409) {
                cb.invalid(new JSONObject(resp.content()));
              } else {
                checkGeneric(resp, cb);
              }
            }
          } catch(JSONException e) {
            cb.jsonError(resp, e);
          }
        });
      }
      
      public static void delete(Character character, CharactersDeleteResponse cb) {
        Map<String, String> data = new HashMap<>();
        data.put(Character.ID, Integer.toString(character.id));
        
        dispatch(Route.Storage.Characters.Delete, data, resp -> {
          try {
            if(resp.succeeded()) {
              cb.success();
            } else {
              if(resp.response().getStatus().code() == 409) {
                cb.invalid(new JSONObject(resp.content()));
              } else {
                checkGeneric(resp, cb);
              }
            }
          } catch(JSONException e) {
            cb.jsonError(resp, e);
          }
        });
      }
    }
    
    public static final class Races {
      private Races() { }
      
      public static void all(RacesAllResponse cb) {
        dispatch(Route.Storage.Races.All, resp -> {
          try {
            if(resp.succeeded()) {
              JSONArray j = new JSONArray(resp.content());
              
              Race[] races = new Race[j.length()];
              
              for(int i = 0; i < j.length(); i++) {
                JSONObject r = j.getJSONObject(i);
                races[i] = new Race(r.getInt(Race.ID), r.getString(Race.NAME));
              }
              
              cb.success(races);
            } else {
              checkGeneric(resp, cb);
            }
          } catch(JSONException e) {
            cb.jsonError(resp, e);
          }
        });
      }
    }
  }
  
  public static void lang(Route route, LangResponse cb) {
    dispatch(route, resp -> {
      try {
        if(resp.succeeded()) {
          JSONObject j = new JSONObject(resp.content());
          
          Map<String, String> lang = new HashMap<>();
          
          for(String key : j.keySet()) {
            lang.put(key, j.getString(key));
          }
          
          cb.success(lang);
        } else {
          cb.error(resp);
        }
      } catch(JSONException e) {
        cb.jsonError(resp, e);
      }
    });
  }
  
  private static abstract class GenericResponse {
    public abstract void loginRequired();
    public abstract void securityRequired();
    public abstract void error(Response r);
    public abstract void jsonError(Response r, JSONException e);
  }
  
  public static abstract class CheckResponse extends GenericResponse {
    public abstract void loggedIn();
  }
  
  public static abstract class LoginResponse extends GenericResponse {
    public abstract void success();
    public abstract void invalid(JSONObject errors);
  }
  
  public static abstract class CharactersAllResponse extends GenericResponse {
    public abstract void success(Character[] characters);
  }
  
  public static abstract class CharactersCreateResponse extends GenericResponse {
    public abstract void success();
    public abstract void invalid(JSONObject errors);
  }
  
  public static abstract class CharactersDeleteResponse extends GenericResponse {
    public abstract void success();
    public abstract void invalid(JSONObject errors);
  }
  
  public static abstract class RacesAllResponse extends GenericResponse {
    public abstract void success(Race[] races);
  }
  
  public static abstract class LangResponse {
    public abstract void success(Map<String, String> lang);
    public abstract void error(Response r);
    public abstract void jsonError(Response r, JSONException e);
  }
  
  public static class Route {
    public final String route;
    public final HttpMethod method;
    
    private Route(String route, HttpMethod method) {
      this.route  = route;
      this.method = method;
    }
    
    public static class Lang {
      public static final App  App  = new App();
      public static final Menu Menu = new Menu();
      
      private Lang() { }
      
      public static class App extends Route {
        private App() {
          super("/lang/app", HttpMethod.GET); //$NON-NLS-1$
        }
      }
      
      public static class Menu extends Route {
        private Menu() {
          super("/lang/menu", HttpMethod.GET); //$NON-NLS-1$
        }
      }
    }
    
    public static class Auth {
      public static final Check    Check    = new Check();
      public static final Register Register = new Register();
      public static final Login    Login    = new Login();
      public static final Logout   Logout   = new Logout();
      public static final Security Security = new Security(HttpMethod.GET);
      public static final Security Unlock   = new Security(HttpMethod.POST);
      
      private Auth() { }
      
      public static class Check extends Route {
        private Check() {
          super("/auth/check", HttpMethod.GET); //$NON-NLS-1$
        }
      }
      
      public static class Register extends Route {
        private Register() {
          super("/auth/register", HttpMethod.PUT); //$NON-NLS-1$
        }
      }
      
      public static class Login extends Route {
        private Login() {
          super("/auth/login", HttpMethod.POST); //$NON-NLS-1$
        }
      }
      
      public static class Logout extends Route {
        private Logout() {
          super("/auth/logout", HttpMethod.POST); //$NON-NLS-1$
        }
      }
      
      public static class Security extends Route {
        private Security(HttpMethod method) {
          super("/auth/security", method); //$NON-NLS-1$
        }
      }
    }
    
    public static class Storage {
      private Storage() { }
      
      public static class Characters extends Route {
        public static final Characters All    = new Characters(HttpMethod.GET);
        public static final Characters Create = new Characters(HttpMethod.PUT);
        public static final Characters Delete = new Characters(HttpMethod.DELETE);
        public static final Characters Choose = new Characters(HttpMethod.POST);
        
        private Characters(HttpMethod method) {
          super("/storage/characters", method); //$NON-NLS-1$
        }
      }
      
      public static class Races extends Route {
        public static final Races All = new Races(HttpMethod.GET);
        
        private Races(HttpMethod method) {
          super("/storage/races", method); //$NON-NLS-1$
        }
      }
    }
  }
}
