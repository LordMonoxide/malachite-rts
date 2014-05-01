package malachite.engine.net.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.ErrorDataEncoderException;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Request {
  private static final String URL = "malachite.monoxidedesign.com"; //$NON-NLS-1$
  private static final String RoutePrefix = "/api"; //$NON-NLS-1$
  private static final String CookieDir = "cookies/"; //$NON-NLS-1$
  
  private static EventLoopGroup _group;
  private static Bootstrap _bootstrap;
  
  private static Map<Channel, Callback> _cb;
  
  private URI _uri;
  private HttpMethod _method;
  private Map<CharSequence, Object> _header;
  private Map<String, String> _data;
  
  public static void destroy() {
    _group.shutdownGracefully();
    
    File dir = new File(CookieDir + URL);
    if(dir.exists()) {
      for(File f : dir.listFiles()) {
        Properties p = new Properties();
        
        try(FileInputStream fs = new FileInputStream(f)) {
          p.load(fs);
        } catch(IOException ex) {
          continue;
        }
        
        if(p.getProperty("expires") == null) { //$NON-NLS-1$
          f.delete();
        }
      }
    }
  }
  
  public static void init() {
    _cb = new HashMap<>();
    
    _group = new NioEventLoopGroup();
    _bootstrap = new Bootstrap()
    .group(_group)
    .channel(NioSocketChannel.class)
    .handler(new ChannelInitializer<SocketChannel>() {
      private boolean _chunked;
      
      @Override
      protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpClientCodec(), new SimpleChannelInboundHandler<HttpObject>() {
          private Response r;
          
          @Override
          protected void messageReceived(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
            if(msg instanceof HttpResponse) {
              HttpResponse response = (HttpResponse)msg;
              
              r = new Response();
              r._response = response;
              
              for(Map.Entry<String, String> header : response.headers()) {
                if(header.getKey().equals(HttpHeaders.Names.SET_COOKIE.toString())) {
                  String[] parts = header.getValue().split("; "); //$NON-NLS-1$
                  String[] kv = parts[0].split("="); //$NON-NLS-1$
                  
                  File file = new File(CookieDir + URL + '/' + kv[0]);
                  file.getParentFile().mkdirs();
                  file.createNewFile();
                  
                  Properties p = new Properties();
                  
                  try(FileInputStream fs = new FileInputStream(file)) {
                    p.load(fs);
                  }
                  
                  boolean hasExpiry = false;
                  boolean hasHttpOnly = false;
                  for(String part : parts) {
                    kv = part.split("="); //$NON-NLS-1$
                    
                    if(kv[0].equals("expires" )) { hasExpiry   = true; } //$NON-NLS-1$
                    if(kv[0].equals("httponly")) { hasHttpOnly = true; } //$NON-NLS-1$
                    
                    if(kv.length == 1) {
                      p.setProperty(kv[0], kv[0]);
                    } else {
                      p.setProperty(kv[0], kv[1]);
                    }
                  }
                  
                  if(!hasExpiry  ) { p.remove("expires" ); } //$NON-NLS-1$
                  if(!hasHttpOnly) { p.remove("httponly"); } //$NON-NLS-1$
                  
                  try(FileOutputStream fs = new FileOutputStream(file)) {
                    p.store(fs, null);
                  }
                }
              }
              
              if(response.getStatus().code() >= 200 &&
                 response.getStatus().code() <= 299 &&
                 HttpHeaders.isTransferEncodingChunked(response)) {
                _chunked = true;
              }
            } else if(msg instanceof HttpContent) {
              HttpContent chunk = (HttpContent)msg;
              
              r._content += chunk.content().toString(CharsetUtil.UTF_8);
              
              if(chunk instanceof LastHttpContent) {
                if(_chunked) {
                  _chunked = false;
                }
                
                _cb.remove(ctx.channel()).onResponse(r);
              }
            }
          }
          
          @Override
          public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.channel().close();
          }
        });
      }
    });
  }
  
  public void setRoute(String route) throws URISyntaxException {
    _uri = new URI(route);
  }
  
  public void setMethod(HttpMethod method) {
    _method = method;
  }
  
  public void addHeader(CharSequence name, Object value) {
    if(_header == null) { _header = new HashMap<>(); }
    _header.put(name, value);
  }
  
  public void addData(String key, String value) {
    if(_data == null) { _data = new HashMap<>(); }
    _data.put(key, value);
  }
  
  public void setData(Map<String, String> data) {
    _data = data;
  }
  
  public void dispatch(Callback cb) {
    _bootstrap.connect(URL, 80).addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        Channel ch = future.channel();
        
        HttpRequest request = null;
        
        if(_method == HttpMethod.GET || _method == HttpMethod.DELETE) {
          if(_data == null) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, _method, RoutePrefix + _uri.toString());
          } else {
            String uri = "?"; //$NON-NLS-1$
            for(Map.Entry<String, String> e : _data.entrySet()) {
              uri += e.getKey() + '=' + e.getValue() + '&';
            }
            
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, _method, RoutePrefix + _uri.toString() + uri.substring(0, uri.length() - 1));
          }
        } else {
          request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, _method, RoutePrefix + _uri.toString());
        }
        
        request.headers().set(HttpHeaders.Names.HOST, URL);
        
        File dir = new File(CookieDir + URL);
        if(dir.exists()) {
          for(File f : dir.listFiles()) {
            Properties p = new Properties();
            
            try(FileInputStream fs = new FileInputStream(f)) {
              p.load(fs);
            }
            
            String expires = p.getProperty("expires"); //$NON-NLS-1$
            if(expires != null) {
              DateFormat df = new SimpleDateFormat("EEE, dd-MMM-yyyy kk:mm:ss z"); //$NON-NLS-1$
              Date d = df.parse(expires);
              if(d.before(new Date())) {
                f.delete();
                continue;
              }
            }
            
            request.headers().set(HttpHeaders.Names.COOKIE, f.getName() + '=' + p.getProperty(f.getName()));
          }
        }
        
        if(_header != null) {
          for(Map.Entry<CharSequence, Object> e : _header.entrySet()) {
            request.headers().set(e.getKey(), e.getValue());
          }
        }
        
        HttpPostRequestEncoder post = null;
        if(_method != HttpMethod.GET && _method != HttpMethod.DELETE) {
          if(_data != null) {
            try {
              post = new HttpPostRequestEncoder(new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE), request, false);
              
              for(Map.Entry<String, String> e : _data.entrySet()) {
                post.addBodyAttribute(e.getKey(), e.getValue());
              }
              
              request = post.finalizeRequest();
            } catch(ErrorDataEncoderException e) {
              e.printStackTrace();
            }
          } else {
            request.headers().add(HttpHeaders.Names.CONTENT_LENGTH, new Integer(0));
          }
        }
        
        if(post == null || !post.isChunked()) {
          ch.writeAndFlush(request);
        } else {
          ch.write(request);
          ch.writeAndFlush(post);
        }
        
        System.out.println(ch);
        _cb.put(ch, cb);
      }
    });
  }
  
  public interface Callback {
    void onResponse(Response response);
  }
}
