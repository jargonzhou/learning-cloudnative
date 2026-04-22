package com.spike.dataengineering.netty.protocol.websocket;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

// serving index.html
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestHandler.class);

    private final String wsUri;
    private static final File indexHtml;

    static {
        URL location = HttpRequestHandler.class
                .getProtectionDomain()
                .getCodeSource().getLocation();

        try {
            String path = location.toURI() + "index.html";
            LOG.info("index.html: {}", path);
            path = !path.contains("file:") ? path : path.substring(5);
            indexHtml = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate index.html");
        }

    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if (wsUri.equalsIgnoreCase(msg.uri())) {
            ctx.fireChannelRead(msg.retain());
        } else {
            if (HttpUtil.is100ContinueExpected(msg)) {
                send100Continue(ctx);
            }
            // read index.html
            RandomAccessFile file = new RandomAccessFile(indexHtml, "r");

            // write response
            HttpResponse response = new DefaultHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
            response.headers().set(
                    HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            boolean isKeepAlive = HttpUtil.isKeepAlive(msg);
            if (isKeepAlive) {
                response.headers().set(
                        HttpHeaderNames.CONTENT_LENGTH, file.length());
                response.headers().set(
                        HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.write(response);
            // write index.html
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }
            // write LastHttpContent
            ChannelFuture f = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

            // close the channel when not keep alive
            if (!isKeepAlive) {
                f.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOG.error("ERROR", cause);
        ctx.close();
    }
}
