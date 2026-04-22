package com.spike.dataengineering.netty.protocol.websocket;

import com.spike.dataengineering.netty.handler.exception.InboundExceptionHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.net.ssl.SSLEngine;

public class ChatServerInitializer extends ChannelInitializer<Channel> {
    private final ChannelGroup channelGroup;

    public ChatServerInitializer(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec()); // HTTP
        pipeline.addLast(new ChunkedWriteHandler()); // Chunk write
        pipeline.addLast(new HttpObjectAggregator(64 * 1024)); // FullHttpRequest
        pipeline.addLast(new HttpRequestHandler("/ws")); // serving index.html
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws")); // WebSocket
        pipeline.addLast(new TextWebSocketFrameHandler(channelGroup)); //

        pipeline.addLast(new InboundExceptionHandler()); // DEBUG
    }

    public static class Secure extends ChatServerInitializer {
        private final SslContext sslContext;

        public Secure(ChannelGroup channelGroup, SslContext sslContext) {
            super(channelGroup);
            this.sslContext = sslContext;
        }

        @Override
        protected void initChannel(Channel ch) throws Exception {
            super.initChannel(ch);

            SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
            ch.pipeline().addFirst(new SslHandler(sslEngine));
        }
    }
}
