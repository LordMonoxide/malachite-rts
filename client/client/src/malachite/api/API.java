package malachite.api;

import malachite.engine.net.http.Request;
import malachite.engine.net.http.Response;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

import java.net.URISyntaxException;

import org.json.JSONObject;

public final class API {
  private API() { }
  
  public static void check(CheckResponse resp) {
    Request r = new Request();
    r.setMethod(HttpMethod.GET);
    
    try{
      r.setRoute("/auth/check");
    } catch(URISyntaxException e) { }
    
    r.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
    r.dispatch(response -> {
      if(response.succeeded()) {
        resp.loggedIn();
      } else {
        if(response.response().getStatus().code() == 401) {
          JSONObject j = new JSONObject(response.content());
          switch(j.getString("show")) {
            case "login"   : resp.loginRequired();    break;
            case "security": resp.securityRequired(); break;
          }
        } else {
          resp.error(response);
        }
      }
    });
  }

  public static void login(String email, String password, Request.Callback cb) {
    Request r = new Request();
    r.setMethod(HttpMethod.POST);
    
    try {
      r.setRoute("/auth/login");
    } catch(URISyntaxException e) { }
    
    r.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
    r.addData("email", email);
    r.addData("password", password);
    r.dispatch(cb);
  }
  
  public static void characters(Request.Callback cb) {
    Request r = new Request();
    r.setMethod(HttpMethod.GET);
    
    try {
      r.setRoute("/storage/characters");
    } catch(URISyntaxException e) { }
    
    r.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
    r.dispatch(cb);
  }
  
  public interface CheckResponse {
    public void loggedIn();
    public void loginRequired();
    public void securityRequired();
    public void error(Response r);
  }
}
