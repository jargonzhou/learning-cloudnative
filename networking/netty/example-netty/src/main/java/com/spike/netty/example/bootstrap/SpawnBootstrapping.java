package com.spike.netty.example.bootstrap;

import com.spike.netty.support.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;

public class SpawnBootstrapping {
    public static void main(String[] args) {
        EventLoopGroup elg = EventLoops.nio();
        ServerBootstrap serverBootstrap = Bootstraps.SERVER(elg, Channels.nioserver(),
                Nettys.DEFAULT_ADDRESS, ChannelHandlers.SIMPLE(), null);

        serverBootstrap.childHandler(
                ChannelHandlers.SPAWN_BOOTSTRAP("www.baidu.com", 80, new ChannelHandlers.NettyCallable<ByteBuf, String>() {
                    @Override
                    public String call(ChannelHandlerContext ctx, ByteBuf msg) {
                        return ByteBufs.introspect(msg);
                    }
                }));

        ChannelFuture channelFuture = serverBootstrap.bind();
        channelFuture.addListener(ChannelFutures.DEFAULT_CHANNEL_FUTURE_LISTENER());

        // elg.shutdownGracefully();
    }
}
