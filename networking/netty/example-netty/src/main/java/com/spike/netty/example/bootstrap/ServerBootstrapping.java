package com.spike.netty.example.bootstrap;

import com.spike.netty.support.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端启动类示例
 *
 * @see ServerBootstrap
 * @see ChannelOption<T>
 */
public class ServerBootstrapping {
    private static final Logger LOG = LoggerFactory.getLogger(ServerBootstrapping.class);

    public static void main(String[] args) {
        EventLoopGroup elg = EventLoops.nio();

        final AttributeKey<String> key = AttributeKey.newInstance("ID");

        ChannelHandler handler = new SimpleChannelInboundHandler<ByteBuf>() {
            @Override
            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                LOG.info("ATTR: {}", ctx.channel().attr(key).get());
            }

            @Override
            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                LOG.info("RECEIVE DATA: {}", ByteBufs.introspect(msg));
            }
        };

        ServerBootstrap serverBootstrap = Bootstraps.SERVER(elg, Channels.nioserver(),
                Nettys.DEFAULT_ADDRESS, handler, ChannelHandlers.SIMPLE());

        // options
        serverBootstrap.option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator());// the default
        serverBootstrap.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

        // attr: for the newly created channel
        serverBootstrap.attr(key, "zhoujiagen");

        ChannelFuture channelFuture = serverBootstrap.bind();
        channelFuture.addListener(ChannelFutures.DEFAULT_CHANNEL_FUTURE_LISTENER());

        io.netty.util.concurrent.Future<?> f = elg.shutdownGracefully();
        f.syncUninterruptibly();
    }
}
