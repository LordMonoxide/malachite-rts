package malachite.api;

import malachite.api.models.News;
import malachite.api.models.Settings;
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
        case NOT_AUTHED_SHOW_SECURITY:
          JSONArray a = j.getJSONArray("questions");
          String[] questions = new String[a.length()];
          for(int i = 0; i < a.length(); i++) {
            questions[i] = a.getString(i);
          }
          
          cb.securityRequired(questions);
          break;
        
        default: cb.error(resp);
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
    
    public static APIFuture register(String email, String password, String password2, String nameFirst, String nameLast, RegisterResponse cb, SecurityQuestion... security) {
      APIFuture f = new APIFuture();
      
      Map<String, String> data = new HashMap<>();
      data.put(User.DB_EMAIL, email);
      data.put(User.DB_PASSWORD, password);
      data.put(User.DB_PASSWORD_CONFIRMATION, password2);
      data.put(User.DB_NAME_FIRST, nameFirst);
      data.put(User.DB_NAME_LAST, nameLast);
      
      int i = 1;
      for(SecurityQuestion s : security) {
        data.put(User.DB_QUESTION + i, s.question);
        data.put(User.DB_ANSWER   + i, s.answer  );
        i++;
      }
      
      dispatch(Route.Auth.Register, data, resp -> {
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
    
    public static APIFuture security(SecurityResponse cb) {
      APIFuture f = new APIFuture();
      
      dispatch(Route.Auth.Security, resp -> {
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
    
    public static APIFuture unlock(UnlockResponse cb, String... security) {
      APIFuture f = new APIFuture();
      
      Map<String, String> data = new HashMap<>();
      
      int i = 1;
      for(String s : security) {
        data.put(User.DB_ANSWER + i++, s);
      }
      
      dispatch(Route.Auth.Unlock, data, resp -> {
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
    
    public static final class Settings {
      private Settings() { }
      
      public static final APIFuture all(SettingsResponse cb) {
        APIFuture f = new APIFuture();
        
        dispatch(Route.Storage.Settings.All, resp -> {
          try {
            if(resp.succeeded()) {
              JSONArray j = resp.toJSONArray();
              
              malachite.api.models.Settings[] settings = new malachite.api.models.Settings[j.length()];
              
              for(int i = 0; i < j.length(); i++) {
                JSONObject r = j.getJSONObject(i);
                settings[i] = new malachite.api.models.Settings(r);
              }
              
              cb.success(settings);
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
    public abstract void securityRequired(String[] question);
  }
  
  public interface CheckResponse extends GenericResponse {
    public abstract void loggedIn();
  }
  
  public interface RegisterResponse extends GenericResponse {
    public abstract void success();
    public abstract void invalid(JSONObject errors);
  }
  
  public interface LoginResponse extends GenericResponse {
    public abstract void success();
    public abstract void invalid(JSONObject errors);
  }
  
  public interface SecurityResponse extends GenericResponse {
    public abstract void success();
  }
  
  public interface UnlockResponse extends GenericResponse {
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
  
  public interface SettingsResponse extends GenericResponse {
    public abstract void success(Settings[] settings);
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
      public static final Route App  = new Route("/lang/app" , HttpMethod.GET); //$NON-NLS-1$
      public static final Route Menu = new Route("/lang/menu", HttpMethod.GET); //$NON-NLS-1$
      public static final Route Game = new Route("/lang/game", HttpMethod.GET); //$NON-NLS-1$
      
      private Lang() { }
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
      
      public static final class Settings {
        private Settings() { }
        
        public static final Route All = new Route("/storage/settings", HttpMethod.GET); //$NON-NLS-1$
      }
    }
  }
  
  public static class SecurityQuestion {
    public final String question, answer;
    
    public SecurityQuestion(String question, String answer) {
      this.question = question;
      this.answer   = answer;
    }
  }
}
