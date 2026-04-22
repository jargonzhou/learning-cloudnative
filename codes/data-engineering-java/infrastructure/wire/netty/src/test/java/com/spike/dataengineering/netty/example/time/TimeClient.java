package com.spike.dataengineering.netty.example.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1) for non-server channeld
            b.group(workerGroup); // (2) used both as boss and worker, boss not used in client side
            b.channel(NioSocketChannel.class); // (3) channel
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4) channel parameters
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new TimeDecoder(),  // decoder
                            new TimeClientHandler() // handler
                    );
                }
            });

            // Start the client.
            ChannelFuture f = b.connect("localhost", 18080).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
