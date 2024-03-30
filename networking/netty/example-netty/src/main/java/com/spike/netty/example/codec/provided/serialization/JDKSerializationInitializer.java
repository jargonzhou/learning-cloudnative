package com.spike.netty.example.codec.provided.serialization;

import io.netty.channel.*;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.CompatibleObjectEncoder;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.Serializable;

/**
 * <pre>
 * JDK序列化支持
 *
 * ObjectOutputStream, ObjectInputStream
 * </pre>
 *
 * @see CompatibleObjectEncoder
 * @see ObjectDecoder
 * @see ObjectEncoder
 */
public class JDKSerializationInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new ObjectDecoder(//
                ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
        pipeline.addLast(new ObjectEncoder());

        pipeline.addLast(new SerializableHandler());
    }

    // ======================================== classes

    public static final class SerializableHandler extends SimpleChannelInboundHandler<Serializable> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) throws Exception {
            System.out.println(msg);
        }
    }

}
