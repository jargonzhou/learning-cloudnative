package com.spike.dataengineering.netty.example.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)
    private static final Logger LOG = LoggerFactory.getLogger(DiscardServerHandler.class);

    // Invoked when the current Channel has read a message from the peer.

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("channelActive: {}", ctx.channel().id());
    }

    /**
     * {@inheritDoc}
     *
     * @see io.netty.util.ReferenceCountUtil#release(Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        LOG.info("channelRead: {}, msg={}", ctx.channel().id(), msg);
        // ByteBuf is a reference-counted object.
        // it is the handler's responsibility to release any reference-counted object passed to the handler.

        // looking into the received data
        ByteBuf data = (ByteBuf) msg;
        try {
            while (data.isReadable()) {
                System.out.print((char) data.readByte());
                System.out.flush();
            }
            // System.out.println(data.toString(StandardCharsets.UTF_8));
        } finally {
            ReferenceCountUtil.release(msg);
        }

        // Discard the received data silently.
        // ((ByteBuf) msg).release(); // (3)
    }

    // Gets called if a Throwable was thrown.
    // - by Netty: IO error
    // - by handler implementation: exception thrown while processing events
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        LOG.error("exceptionCaught", cause);
        ctx.close();
    }
}