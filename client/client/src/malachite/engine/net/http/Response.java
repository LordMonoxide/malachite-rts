package malachite.engine.net.http;

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
}
