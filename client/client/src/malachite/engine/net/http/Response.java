package malachite.engine.net.http;

import io.netty.handler.codec.http.HttpResponse;

public class Response {
  HttpResponse _response;
  String _content = ""; //$NON-NLS-1$

  public HttpResponse response() { return _response; }
  public String       content () { return _content;  }
}
