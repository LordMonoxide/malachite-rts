package malachite.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public final class API {
  static {
    CookieHandler.setDefault(new CookieManager());
  }

  private API() { }

  private static final String baseURL = "http://malachite.monoxidedesign.com/api/client/";

  public static Response request(String url, Method method, String encoding, Parameter... param) throws IOException {
    HttpURLConnection con = (HttpURLConnection)new URL(baseURL + url).openConnection();

    con.setRequestMethod(method.name());
    con.setRequestProperty("Accept-Charset", encoding);
    con.setUseCaches(false);

    if(method != Method.GET) {
      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);
      con.setDoInput(true);
      con.setDoOutput(true);

      StringBuilder params = new StringBuilder(param.length * 30);
      for(Parameter p : param) {
        if(params.length() != 0) { params.append('&'); }
        params.append(p.key);
        params.append('=');
        params.append(URLEncoder.encode(p.val, encoding));
      }

      OutputStream out = con.getOutputStream();
      out.write(params.toString().getBytes("UTF-8"));
      out.close();
    }

    return new Response(con);
  }

  public static Parameter param(String key, String val) {
    return new Parameter(key, val);
  }

  public static class Parameter {
    public final String key, val;
    public Parameter(String key, String val) {
      this.key = key;
      this.val = val;
    }
  }

  public static class Response {
    private HttpURLConnection _con;
    private String _resp;
    private Status _status;

    private Response(HttpURLConnection con) throws IOException {
      _con = con;

      _status = Status.fromCode(_con.getResponseCode());

      try {
        _resp = read(_con.getInputStream(), _con.getContentLength());
      } catch(IOException e) {
        e.printStackTrace();
        _resp = read(_con.getErrorStream(), _con.getContentLength());
      }
    }

    public Status  status () { return _status; }
    public boolean success() { return _status._code >= Status.OK_START._code && _status._code <= Status.OK_END._code; }
    public JSONObject parseObject() { return new JSONObject(_resp); }
    public JSONArray  parseArray () { return new JSONArray (_resp); }

    private String read(InputStream is, int length) throws IOException {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
      StringBuilder resp = new StringBuilder(length);
      String line;

      while((line = rd.readLine()) != null) {
        resp.append(line);
        resp.append('\r');
      }

      rd.close();

      System.out.println(resp);

      return resp.toString();
    }
  }

  enum Method {
    GET, POST, PUT, DELETE
  }

  enum Status {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NON_AUTHORITATIVE_INFORMATION(203),
    NO_CONTENT(204),
    RESET_CONTENT(205),
    PARTIAL_CONTENT(206),

    MULTIPLE_CHOICES(300),
    MOVED_PERMANENTLY(301),
    FOUND(302),
    SEE_OTHER(303),
    NOT_MODIFIED(304),
    USE_PROXY(305),
    UNUSED_306(306),
    TEMPORARY_REDIRECT(307),

    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    PAYMENT_REQUIRED(402),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    NOT_ACCEPTABLE(406),
    PROXY_AUTHENTICATION_REQUIRED(407),
    REQUEST_TIMEOUT(408),
    CONFLICT(409),
    GONE(410),
    LENGTH_REQUIRED(411),
    PRECONDITION_FAILED(412),
    REQUEST_ENTITY_TOO_LARGE(413),
    REQUEST_URI_TOO_LONG(414),
    UNSUPPORTED_MEDIA_TYPE(415),
    REQUEST_RANGE_NOT_SATISFIABLE(416),
    EXPECTATION_FAILED(417),

    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504),
    HTTP_VERSION_NOT_SUPPORTED(505),

    OK_START(200),
    OK_END(299),
    REDIRECT_START(300),
    REDIRECT_END(399),
    CLIENT_ERROR_START(400),
    CLIENT_ERROR_END(499),
    SERVER_ERROR_START(500),
    SERVER_ERROR_END(599);

    private final int _code;

    Status(int code) {
      _code = code;
    }

    public int code() {
      return _code;
    }

    static Status fromCode(int code) {
      for(Status s : Status.values()) {
        if(s._code == code) {
          return s;
        }
      }

      return null;
    }
  }
}
