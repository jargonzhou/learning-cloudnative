package com.spike.dataengineering.netty.protocol.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.util.Objects;

public class ChatServer {
    private static final Logger LOG = LoggerFactory.getLogger(ChatServer.class);

    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;
    // a group of Channels
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    // arguments
    private boolean secure = false;

    public ChatServer() {
        this.secure = false;
    }

    public ChatServer(boolean secure) {
        this.secure = secure;
    }

    public void destroy() {
        LOG.info("{} destroy...", this.getClass().getSimpleName());
        if (Objects.nonNull(channel)) {
            channel.close();
        }
        channelGroup.close();
        group.shutdownGracefully();
        LOG.info("{} destroy....", this.getClass().getSimpleName());
    }

    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(createInitializer(channelGroup));

        ChannelFuture f = bootstrap.bind(address).syncUninterruptibly();
        LOG.info("{} started on {}", this.getClass().getSimpleName(), address);
        this.channel = f.channel();
        return f;
    }

    protected ChannelInitializer<Channel> createInitializer(ChannelGroup channelGroup) {
        if (!this.secure) {
            return new ChatServerInitializer(channelGroup);
        } else {
            try {
                // self-signed certificate
                SelfSignedCertificate cert = new SelfSignedCertificate();
                SslContext sslContext = SslContextBuilder
                        .forServer(cert.certificate(), cert.privateKey())
                        .build();
                return new ChatServerInitializer.Secure(channelGroup, sslContext);
            } catch (CertificateException | SSLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer(true);
        ChannelFuture f = server.start(new InetSocketAddress(18080));

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                server.destroy();
            }
        }, "SHUTDOWN HOOK"));

        LOG.info("{} waiting for channel {} close.", ChatServer.class.getSimpleName(), f.channel().id());
        f.channel().closeFuture().syncUninterruptibly();
    }
}
