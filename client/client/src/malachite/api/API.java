package malachite.api;

import malachite.engine.net.http.Request;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

import java.net.URISyntaxException;

public final class API {
  private API() { }
  
  public static void check(Request.Callback cb) {
    Request r = new Request();
    r.setMethod(HttpMethod.GET);
    
    try{
      r.setRoute("/auth/check");
    } catch(URISyntaxException e) { }
    
    r.addHeader(HttpHeaders.Names.ACCEPT, "application/json");
    r.dispatch(cb);
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
}
