package com.spike.dataengineering.netty.example.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(TimeClientHandler.class);

    // Invoked when the current Channel has read a message from the peer.
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf m = (ByteBuf) msg; // (1) read the data sent from a peer into ByteBuf
//        try {
//            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
//            System.out.println(new Date(currentTimeMillis));
//            ctx.close(); // close the channel
//        } finally {
//            m.release();
//        }

        // use POJO: see TimeDecoder
        UnixTime m = (UnixTime) msg;
        System.out.print(m);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        LOG.error("exceptionCaught", cause);
        ctx.close();
    }
}
