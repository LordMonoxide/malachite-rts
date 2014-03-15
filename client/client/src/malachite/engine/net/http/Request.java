package malachite.engine.net.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Request {
  private static final String URL = "malachite.monoxidedesign.com";

  private static EventLoopGroup _group;
  private static Bootstrap _bootstrap;
  
  private static Map<Channel, Callback> _cb;
  
  private URI _uri;
  private HttpMethod _method;

  static {
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

              for(String name : response.headers().names()) {
                for(String val : response.headers().getAll(name)) {
                  System.out.println("HEADER: " + name + ": " + val); //$NON-NLS-1$ //$NON-NLS-2$
                }
              }

              if(response.getStatus().code() >= 200 &&
                 response.getStatus().code() <= 299 &&
                 HttpHeaders.isTransferEncodingChunked(response)) {
                _chunked = true;
                System.out.println("CHUNKED CONTENT {"); //$NON-NLS-1$
              } else {
                System.out.println("CONTENT {"); //$NON-NLS-1$
              }
            } else if(msg instanceof HttpContent) {
              HttpContent chunk = (HttpContent)msg;

              if(chunk instanceof LastHttpContent) {
                if(_chunked) {
                  _chunked = false;
                  System.out.println("} END OF CHUNKED CONTENT"); //$NON-NLS-1$
                } else {
                  System.out.println("} END OF CONTENT"); //$NON-NLS-1$
                }

                _cb.get(ctx.channel()).onResponse(r);
              } else {
                System.out.println(chunk.content().toString(CharsetUtil.UTF_8));

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

  public void dispatch(Callback cb) {
    Channel ch = _bootstrap.connect(URL, 80).syncUninterruptibly().channel();

    FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, _method, _uri.toString());
    request.headers()
      .set(HttpHeaders.Names.HOST, URL)
      .set(HttpHeaders.Names.ACCEPT, "application/json");

    ch.writeAndFlush(request);
    // ch.closeFuture().syncUninterruptibly();

    _cb.put(ch, cb);
  }
  
  public interface Callback {
    void onResponse(Response response);
  }
}
