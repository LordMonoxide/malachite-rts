package malachite.engine.net.http;

import org.json.JSONArray;
import org.json.JSONObject;

import io.netty.handler.codec.http.HttpResponse;

public class Response {
  HttpResponse _response;
  String _content = ""; //$NON-NLS-1$

  public HttpResponse response() { return _response; }
  public String       content () { return _content;  }
  
  public boolean succeeded() {
    return _response.getStatus().code() >= 200 && _response.getStatus().code() <= 299;
  }
  
  public boolean failed() {
    return _response.getStatus().code() >= 400 && _response.getStatus().code() <= 499;
  }
  
  public JSONObject toJSON() {
    return new JSONObject(_content);
  }
  
  public JSONArray toJSONArray() {
    return new JSONArray(_content);
  }
}
