package com.spike.dataengineering.netty.example.discard;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscardClientHandler extends ChannelDuplexHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DiscardClientHandler.class);

    // The Channel of the ChannelHandlerContext is now active
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("channelActive: {}", ctx.channel().id());
        ChannelFuture f = ctx.channel()
                .writeAndFlush(Unpooled.wrappedBuffer("Hello there".getBytes()))
                .sync(); // need force sync
//        f.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                ctx.executor().schedule(() -> {
//                    channelFuture.channel().close(); // close the channel
//                }, 3, TimeUnit.SECONDS);
//            }
//        });
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        LOG.error("exceptionCaught", cause);
        ctx.close();
    }

}
