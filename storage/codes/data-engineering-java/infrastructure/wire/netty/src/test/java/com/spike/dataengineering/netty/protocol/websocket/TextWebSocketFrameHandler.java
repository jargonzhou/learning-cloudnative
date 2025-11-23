package com.spike.dataengineering.netty.protocol.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup channelGroup;

    public TextWebSocketFrameHandler(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // remove HTTP handler
            ctx.pipeline().remove(HttpRequestHandler.class);

            // broadcast in channel group
            channelGroup.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel().id() + " joined"));
            // join channel group
            channelGroup.add(ctx.channel());
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // broadcast in channel group
        channelGroup.writeAndFlush(new TextWebSocketFrame(ctx.channel().id() + "> " + msg.text()));
    }
}
