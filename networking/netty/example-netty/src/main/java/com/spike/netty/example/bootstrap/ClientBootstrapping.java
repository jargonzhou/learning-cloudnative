package com.spike.netty.example.bootstrap;

import com.spike.netty.support.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

public class ClientBootstrapping {
    public static void main(String[] args) {
        EventLoopGroup elg = EventLoops.nio();
        Bootstrap bootstrap =
                Bootstraps.CLIENT(elg, Channels.nio(), Nettys.DEFAULT_ADDRESS, ChannelHandlers.SIMPLE());

        ChannelFuture channelFuture = bootstrap.connect();

        channelFuture.addListener(ChannelFutures.DEFAULT_CHANNEL_FUTURE_LISTENER());

        io.netty.util.concurrent.Future<?> f = elg.shutdownGracefully();
        f.syncUninterruptibly();
    }
}
