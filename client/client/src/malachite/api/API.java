package malachite.api;

import malachite.api.models.Character;
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
  
  public static void check(CheckResponse cb) {
    Request r = new Request();
    r.setMethod(HttpMethod.GET);
    
    try{
      r.setRoute("/auth/check");
    } catch(URISyntaxException e) { }
    
    r.addHeader(HttpHeaders.Names.ACCEPT, APPLICATION_JSON);
    r.dispatch(resp -> {
      try {
        if(resp.succeeded()) {
          cb.loggedIn();
        } else {
          if(resp.response().getStatus().code() == 401) {
            JSONObject j = new JSONObject(resp.content());
            switch(j.getString("show")) {
              case "login":    cb.loginRequired();    break;
              case "security": cb.securityRequired(); break;
              default:         cb.error(resp);
            }
          } else {
            cb.error(resp);
          }
        }
      } catch(JSONException e) {
        cb.error(resp);
      }
    });
  }

  public static void login(String email, String password, LoginResponse cb) {
    Request r = new Request();
    r.setMethod(HttpMethod.POST);
    
    try {
      r.setRoute("/auth/login");
    } catch(URISyntaxException e) { }
    
    r.addHeader(HttpHeaders.Names.ACCEPT, APPLICATION_JSON);
    r.addData(User.EMAIL, email);
    r.addData(User.PASSWORD, password);
    r.dispatch(resp -> {
      try {
        if(resp.succeeded()) {
          cb.success();
        } else {
          switch(resp.response().getStatus().code()) {
            case 401:
              JSONObject j = new JSONObject(resp.content());
              switch(j.getString("security")) {
                case "login":    cb.loginRequired();    break;
                case "security": cb.securityRequired(); break;
                default:         cb.error(resp);
              }
              
              break;
            
            case 409:
              cb.invalid(new JSONObject(resp.content()));
              break;
            
            default:
              cb.error(resp);
          }
        }
      } catch(JSONException e) {
        cb.error(resp);
      }
    });
  }
  
  public static void characters(CharacterResponse cb) {
    Request r = new Request();
    r.setMethod(HttpMethod.GET);
    
    try {
      r.setRoute("/storage/characters");
    } catch(URISyntaxException e) { }
    
    r.addHeader(HttpHeaders.Names.ACCEPT, APPLICATION_JSON);
    r.dispatch(resp -> {
      try {
        if(resp.succeeded()) {
          JSONArray j = new JSONArray(resp.content());
          
          Character[] characters = new Character[j.length()];
          
          for(int i = 0; i < j.length(); i++) {
            JSONObject c = j.getJSONObject(i);
            characters[i] = new Character(c.getString(Character.NAME), c.getString(Character.RACE), c.getString(Character.SEX));
          }
          
          cb.success(characters);
        } else {
          if(resp.response().getStatus().code() == 401) {
            JSONObject j = new JSONObject(resp.content());
            switch(j.getString("show")) {
              case "login":    cb.loginRequired();    break;
              case "security": cb.securityRequired(); break;
              default:         cb.error(resp);
            }
          } else {
            cb.error(resp);
          }
        }
      } catch(JSONException e) {
        cb.error(resp);
      }
    });
  }
  
  public static void lang(String route, LangResponse cb) {
    Request r = new Request();
    r.setMethod(HttpMethod.GET);
    
    try {
      r.setRoute(route);
    } catch(URISyntaxException e) { }
    
    r.addHeader(HttpHeaders.Names.ACCEPT, APPLICATION_JSON);
    r.dispatch(resp -> {
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
        cb.error(resp);
      }
    });
  }
  
  public interface CheckResponse {
    public void loggedIn();
    public void loginRequired();
    public void securityRequired();
    public void error(Response r);
  }
  
  public interface LoginResponse {
    public void success();
    public void invalid(JSONObject errors);
    public void loginRequired();
    public void securityRequired();
    public void error(Response r);
  }
  
  public interface CharacterResponse {
    public void success(Character[] characters);
    public void loginRequired();
    public void securityRequired();
    public void error(Response r);
  }
  
  public interface LangResponse {
    public void success(Map<String, String> lang);
    public void error(Response r);
  }
}
