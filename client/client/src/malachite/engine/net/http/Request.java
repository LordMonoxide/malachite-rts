package malachite.engine.net.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.net.URISyntaxException;

public class Request {
  private static final String URL = "malachite.monoxidedesign.com";

  private EventLoopGroup _group;
  private Bootstrap _bootstrap;

  private URI _uri;
  private HttpMethod _method;

  private Response _response;

  public Request() {
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

                _response = r;
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

  public Response dispatch() {
    _response = null;

    Channel ch = _bootstrap.connect(URL, 80).syncUninterruptibly().channel();

    FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, _method, _uri.toString());
    request.headers()
      .set(HttpHeaders.Names.HOST, URL)
      .set(HttpHeaders.Names.ACCEPT, "application/json");

    ch.writeAndFlush(request);
    // ch.closeFuture().syncUninterruptibly();

    while(_response == null) {
      try {
        Thread.sleep(1);
      } catch(InterruptedException e) {
      }
    }

    return _response;
  }
}
