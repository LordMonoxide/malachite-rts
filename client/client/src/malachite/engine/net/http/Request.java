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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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

              if(response.headers().contains(HttpHeaders.Names.SET_COOKIE)) {
                try(OutputStreamWriter o = new OutputStreamWriter(new FileOutputStream(CookieDir + URL))) {
                  o.write(response.headers().get(HttpHeaders.Names.SET_COOKIE));
                }
              }

              if(response.getStatus().code() >= 200 &&
                 response.getStatus().code() <= 299 &&
                 HttpHeaders.isTransferEncodingChunked(response)) {
                _chunked = true;
              }
            } else if(msg instanceof HttpContent) {
              HttpContent chunk = (HttpContent)msg;

              if(chunk instanceof LastHttpContent) {
                if(_chunked) {
                  _chunked = false;
                }

                _cb.remove(ctx.channel()).onResponse(r);
              } else {
                r._content += chunk.content().toString(CharsetUtil.UTF_8);
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
        
        if(_method == HttpMethod.GET || _data == null) {
          request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, _method, RoutePrefix + _uri.toString());
        } else {
          request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, _method, RoutePrefix + _uri.toString());
        }
        
        request.headers().set(HttpHeaders.Names.HOST, URL);
        
        try(BufferedReader br = new BufferedReader(new FileReader(CookieDir + URL))) {
          request.headers().set(HttpHeaders.Names.COOKIE, br.readLine());
        } catch(FileNotFoundException e) { }
        
        if(_header != null) {
          for(Map.Entry<CharSequence, Object> e : _header.entrySet()) {
            request.headers().set(e.getKey(), e.getValue());
          }
        }
        
        HttpPostRequestEncoder post = null;
        if(_method != HttpMethod.GET && _data != null) {
          try {
            post = new HttpPostRequestEncoder(new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE), request, false);
            
            for(Map.Entry<String, String> e : _data.entrySet()) {
              post.addBodyAttribute(e.getKey(), e.getValue());
            }
            
            request = post.finalizeRequest();
          } catch(ErrorDataEncoderException e) {
            e.printStackTrace();
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
