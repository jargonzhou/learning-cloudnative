package com.spike.netty.example.codec;

import com.spike.netty.support.ChannelHandlers;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

/**
 * <pre>
 * WebSocketFrame <=> WebSocketFrameCodec.SimpleWebSocketFrame
 *
 * 注意: 在encoded/decoded消息时, {@link MessageToMessageCodec}会调用<code>ReferenceCounted.release()</code>.
 * </pre>
 */
public class WebSocketFrameCodec
        extends MessageToMessageCodec<WebSocketFrame, ChannelHandlers.SimpleWebSocketFrame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ChannelHandlers.SimpleWebSocketFrame msg, List<Object> out)
            throws Exception {
        // => WebSocketFrame
        out.add(ChannelHandlers.WebSocketFrameConverters.convert(msg, true));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out)
            throws Exception {
        // => WebSocketFrameCodec.SimpleWebSocketFrame
        out.add(ChannelHandlers.WebSocketFrameConverters.convert(msg, true));
    }

}
