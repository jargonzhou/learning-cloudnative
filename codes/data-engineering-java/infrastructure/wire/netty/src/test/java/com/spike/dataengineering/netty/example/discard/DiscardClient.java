package com.spike.dataengineering.netty.example.discard;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscardClient {
    private static final Logger LOG = LoggerFactory.getLogger(DiscardClient.class);

    private final int serverPort;

    public DiscardClient(int serverPort) {
        this.serverPort = serverPort;
    }

    public void run() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap()
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, false)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardClientHandler());
                        }
                    });

            ChannelFuture f = b.connect("localhost", serverPort).sync();
            Channel channel = f.channel();
            LOG.info("Channel: {}, {}", channel.id(), channel.localAddress());
//            channel.writeAndFlush(Unpooled.wrappedBuffer("Hello there".getBytes()))
//                    .sync()
//                    .addListener(ChannelFutureListener.CLOSE);

            f.channel().closeFuture().sync();
            LOG.info("Closed.");
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int port = 18080;
        new DiscardClient(port).run();
    }
}
