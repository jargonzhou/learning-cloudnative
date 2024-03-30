package com.spike.netty.example.codec.provided.serialization;

import com.google.protobuf.MessageLite;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * ProtoBuf序列化支持
 *
 * @see ProtobufVarint32FrameDecoder
 * @see ProtobufDecoder
 * @see ProtobufEncoder
 */
public class ProtoBufInitializer extends ChannelInitializer<Channel> {
    private final MessageLite prototype;

    public ProtoBufInitializer(MessageLite prototype) {
        this.prototype = prototype;
    }

    public MessageLite getPrototype() {
        return prototype;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(prototype));
        pipeline.addLast(new ProtobufEncoder());

        pipeline.addLast(new ObjectHandler());
    }

    public static final class ObjectHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(msg);
        }
    }
}
