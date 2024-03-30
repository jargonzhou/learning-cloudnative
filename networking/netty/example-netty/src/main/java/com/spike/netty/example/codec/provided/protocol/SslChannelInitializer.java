package com.spike.netty.example.codec.provided.protocol;

import com.spike.netty.support.ChannelHandlers;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * SSL/TLS支持
 *
 * @see io.netty.handler.ssl.SslContext
 * @see io.netty.handler.ssl.SslHandler
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SslContext sslContext;
    private final boolean startTls;

    public SslChannelInitializer(SslContext sslContext, boolean startTls) {
        this.sslContext = sslContext;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine sslEngine = sslContext.newEngine(ch.alloc());

        // IN MOST CASE, ADD FIRST IN THE PIPELINE
        ch.pipeline().addFirst(ChannelHandlers.SSL_NAME, new SslHandler(sslEngine, startTls));
    }

}
