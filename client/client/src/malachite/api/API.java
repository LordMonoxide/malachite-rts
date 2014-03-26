package malachite.api;

import malachite.api.models.Character;
import malachite.engine.net.http.Request;
import malachite.engine.net.http.Response;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class API {
  private API() { }
  
  public static void check(CheckResponse cb) {
    Request r = new Request();
    r.setMethod(HttpMethod.GET);
    
    try{
      r.setRoute("/auth/check");
    } catch(URISyntaxException e) { }
    
    r.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
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
    
    r.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
    r.addData("email", email);
    r.addData("password", password);
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
    
    r.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
    r.dispatch(resp -> {
      try {
        if(resp.succeeded()) {
          JSONArray j = new JSONArray(resp.content());
          
          Character[] characters = new Character[j.length()];
          
          for(int i = 0; i < j.length(); i++) {
            JSONObject c = j.getJSONObject(i);
            characters[i] = new Character(c.getString("name"), c.getString("race"), c.getString("sex"));
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
}
