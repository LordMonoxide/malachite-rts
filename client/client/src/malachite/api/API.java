package malachite.api;

import malachite.api.models.Building;
import malachite.api.models.News;
import malachite.api.models.Research;
import malachite.api.models.Unit;
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
      JSONObject j = resp.toJSON();
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
    
    public static APIFuture check(CheckResponse cb) {
      APIFuture f = new APIFuture();
      
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
        
        f.complete();
      });
      
      return f;
    }
  
    public static APIFuture login(String email, String password, LoginResponse cb) {
      APIFuture f = new APIFuture();
      
      Map<String, String> data = new HashMap<>();
      data.put(User.DB_EMAIL,    email);
      data.put(User.DB_PASSWORD, password);
      
      dispatch(Route.Auth.Login, data, resp -> {
        try {
          if(resp.succeeded()) {
            cb.success();
          } else {
            if(resp.response().getStatus().code() == 409) {
              cb.invalid(resp.toJSON());
            } else {
              checkGeneric(resp, cb);
            }
          }
        } catch(JSONException e) {
          cb.jsonError(resp, e);
        }
        
        f.complete();
      });
      
      return f;
    }
    
    public static APIFuture logout(LogoutResponse cb) {
      APIFuture f = new APIFuture();
      
      dispatch(Route.Auth.Logout, resp -> {
        try {
          if(resp.succeeded()) {
            cb.success();
          } else {
            checkGeneric(resp, cb);
          }
        } catch(JSONException e) {
          cb.jsonError(resp, e);
        }
        
        f.complete();
      });
      
      return f;
    }
  }
  
  public static final class Storage {
    private Storage() { }
    
    public static final class News {
      private News() { }
      
      public static APIFuture all(NewsAllResponse cb) {
        APIFuture f = new APIFuture();
        
        dispatch(Route.Storage.News.All, resp -> {
          try {
            if(resp.succeeded()) {
              JSONArray j = resp.toJSONArray();
              
              malachite.api.models.News[] news = new malachite.api.models.News[j.length()];
              
              for(int i = 0; i < j.length(); i++) {
                JSONObject r = j.getJSONObject(i);
                news[i] = new malachite.api.models.News(r.getInt(malachite.api.models.News.DB_ID), r.getString(malachite.api.models.News.DB_TITLE), r.getString(malachite.api.models.News.DB_BODY));
              }
              
              cb.success(news);
            } else {
              cb.error(resp);
            }
          } catch(JSONException e) {
            cb.jsonError(resp, e);
          }
          
          f.complete();
        });
        
        return f;
      }
      
      public static APIFuture latest(NewsLatestResponse cb) {
        APIFuture f = new APIFuture();
        
        dispatch(Route.Storage.News.Latest, resp -> {
          try {
            switch(resp.response().getStatus().code()) {
              case 200:
                JSONObject r = resp.toJSON();
                cb.success(new malachite.api.models.News(r.getInt(malachite.api.models.News.DB_ID), r.getString(malachite.api.models.News.DB_TITLE), r.getString(malachite.api.models.News.DB_BODY)));
                break;
                
              case 204:
                cb.success(null);
                break;
                
              default:
                cb.error(resp);
            }
          } catch(JSONException e) {
            cb.jsonError(resp, e);
          }
          
          f.complete();
        });
        
        return f;
      }
    }
    
    public static final class Tech {
      private Tech() { }
      
      public static final APIFuture buildings(BuildingsResponse cb) {
        APIFuture f = new APIFuture();
        
        dispatch(Route.Storage.Tech.Buildings, resp -> {
          try {
            if(resp.succeeded()) {
              JSONArray j = resp.toJSONArray();
              
              Building[] buildings = new Building[j.length()];
              
              for(int i = 0; i < j.length(); i++) {
                JSONObject r = j.getJSONObject(i);
                buildings[i] = new Building(r.getInt(Building.DB_ID), r.getString(Building.DB_NAME), Building.TYPE.fromString(r.getString(Building.DB_TYPE)));
              }
              
              cb.success(buildings);
            } else {
              checkGeneric(resp, cb);
            }
          } catch(JSONException e) {
            cb.jsonError(resp, e);
          }
          
          f.complete();
        });
        
        return f;
      }
      
      public static final APIFuture research(ResearchResponse cb) {
        APIFuture f = new APIFuture();
        
        dispatch(Route.Storage.Tech.Research, resp -> {
          try {
            if(resp.succeeded()) {
              JSONArray j = resp.toJSONArray();
              
              Research[] research = new Research[j.length()];
              
              for(int i = 0; i < j.length(); i++) {
                JSONObject r = j.getJSONObject(i);
                research[i] = new Research(r.getInt(Research.DB_ID), r.getInt(Research.DB_BUILDING_ID), r.getString(Research.DB_NAME));
              }
              
              cb.success(research);
            } else {
              checkGeneric(resp, cb);
            }
          } catch(JSONException e) {
            cb.jsonError(resp, e);
          }
          
          f.complete();
        });
        
        return f;
      }
      
      public static final APIFuture units(UnitsResponse cb) {
        APIFuture f = new APIFuture();
        
        dispatch(Route.Storage.Tech.Units, resp -> {
          try {
            if(resp.succeeded()) {
              JSONArray j = resp.toJSONArray();
              
              Unit[] units = new Unit[j.length()];
              
              for(int i = 0; i < j.length(); i++) {
                JSONObject r = j.getJSONObject(i);
                units[i] = new Unit(r.getInt(Unit.DB_ID), r.getInt(Unit.DB_BUILDING_ID), r.getString(Unit.DB_NAME), Unit.TYPE.fromString(r.getString(Unit.DB_TYPE)));
              }
              
              cb.success(units);
            } else {
              checkGeneric(resp, cb);
            }
          } catch(JSONException e) {
            cb.jsonError(resp, e);
          }
          
          f.complete();
        });
        
        return f;
      }
    }
  }
  
  public static APIFuture lang(Route route, LangResponse cb) {
    APIFuture f = new APIFuture();
    
    dispatch(route, resp -> {
      try {
        if(resp.succeeded()) {
          JSONObject j = resp.toJSON();
          
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
      
      f.complete();
    });
    
    return f;
  }
  
  public interface ErrorResponse {
    public abstract void error(Response r);
    public abstract void jsonError(Response r, JSONException e);
  }
  
  public interface GenericResponse extends ErrorResponse {
    public abstract void loginRequired();
    public abstract void securityRequired();
  }
  
  public interface CheckResponse extends GenericResponse {
    public abstract void loggedIn();
  }
  
  public interface LoginResponse extends GenericResponse {
    public abstract void success();
    public abstract void invalid(JSONObject errors);
  }
  
  public interface LogoutResponse extends GenericResponse {
    public abstract void success();
  }
  
  public interface NewsAllResponse {
    public abstract void success(News[] news);
    public abstract void error(Response r);
    public abstract void jsonError(Response r, JSONException e);
  }
  
  public interface NewsLatestResponse {
    public abstract void success(News news);
    public abstract void error(Response r);
    public abstract void jsonError(Response r, JSONException e);
  }
  
  public interface BuildingsResponse extends GenericResponse {
    public abstract void success(Building[] buildings);
  }
  
  public interface ResearchResponse extends GenericResponse {
    public abstract void success(Research[] research);
  }
  
  public interface UnitsResponse extends GenericResponse {
    public abstract void success(Unit[] units);
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
    
    public static final class Lang {
      public static final App  App  = new App();
      public static final Menu Menu = new Menu();
      
      private Lang() { }
      
      public static final class App extends Route {
        private App() {
          super("/lang/app", HttpMethod.GET); //$NON-NLS-1$
        }
      }
      
      public static final class Menu extends Route {
        private Menu() {
          super("/lang/menu", HttpMethod.GET); //$NON-NLS-1$
        }
      }
    }
    
    public static final class Auth {
      public static final Check    Check    = new Check();
      public static final Register Register = new Register();
      public static final Login    Login    = new Login();
      public static final Logout   Logout   = new Logout();
      public static final Security Security = new Security(HttpMethod.GET);
      public static final Security Unlock   = new Security(HttpMethod.POST);
      
      private Auth() { }
      
      public static final class Check extends Route {
        private Check() {
          super("/auth/check", HttpMethod.GET); //$NON-NLS-1$
        }
      }
      
      public static final class Register extends Route {
        private Register() {
          super("/auth/register", HttpMethod.PUT); //$NON-NLS-1$
        }
      }
      
      public static final class Login extends Route {
        private Login() {
          super("/auth/login", HttpMethod.POST); //$NON-NLS-1$
        }
      }
      
      public static final class Logout extends Route {
        private Logout() {
          super("/auth/logout", HttpMethod.POST); //$NON-NLS-1$
        }
      }
      
      public static final class Security extends Route {
        private Security(HttpMethod method) {
          super("/auth/security", method); //$NON-NLS-1$
        }
      }
    }
    
    public static final class Storage {
      private Storage() { }
      
      public static final class News extends Route {
        public static final News All    = new News("/");       //$NON-NLS-1$
        public static final News Latest = new News("/latest"); //$NON-NLS-1$
        
        private News(String route) {
          super("/storage/news" + route, HttpMethod.GET); //$NON-NLS-1$
        }
      }
      
      public static final class Tech {
        private Tech() { }
        
        public static final Route Buildings = new Route("/storage/tech/buildings", HttpMethod.GET); //$NON-NLS-1$
        public static final Route Research  = new Route("/storage/tech/research",  HttpMethod.GET); //$NON-NLS-1$
        public static final Route Units     = new Route("/storage/tech/units",     HttpMethod.GET); //$NON-NLS-1$
      }
    }
  }
}
