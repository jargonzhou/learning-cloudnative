package com.spike.dataengineering.netty.example.time;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(TimeServerHandler.class);

    // The Channel of the ChannelHandlerContext is now active
    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1) send message as soon as a connection is established
//        final ByteBuf time = ctx.alloc().buffer(4); // (2) get current ByteBufAllocator to allocate a new buffer
//        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
//
//        final ChannelFuture f = ctx.writeAndFlush(time); // (3) write the message: ByteBuf reader index and writer index
//        // ChannelFuture: represent an IO operation which has not yet occurred
//        f.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) {
//                assert f == future;
//                ctx.close(); // also return a ChannelFuture
//            }
//        }); // (4)

        // use POJO
        ChannelFuture f = ctx.writeAndFlush(new UnixTime());
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        LOG.error("exceptionCaught", cause);
        ctx.close();
    }
}
