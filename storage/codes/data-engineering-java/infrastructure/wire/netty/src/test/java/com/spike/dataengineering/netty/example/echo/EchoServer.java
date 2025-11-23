package com.spike.dataengineering.netty.example.echo;

import com.spike.dataengineering.netty.example.discard.DiscardServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServer {
    private static final Logger LOG = LoggerFactory.getLogger(DiscardServer.class);

    private int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1) boss: accept incoming connections
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // worker: handle the traffic of the accepted connection
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2) helper class to set up a server
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3) instantiate a new Channel to accept incoming connections
                    // always be evaluated by a newly accepted Channel
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4) a special handler to help configure a new Channel
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5) NioServerSocketChannel parameters
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6) NioSocketChannel parameters

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)
            LOG.info("{} started on port {}", this.getClass().getSimpleName(), port);

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 18080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new EchoServer(port).run();
    }
}
