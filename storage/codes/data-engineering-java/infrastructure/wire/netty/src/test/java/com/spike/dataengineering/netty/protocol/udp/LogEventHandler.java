package com.spike.dataengineering.netty.protocol.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(LogEventHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEvent logEvent) throws Exception {
        try {
            LOG.info("Received: {}", logEvent.toString());
        } finally {
            ReferenceCountUtil.release(logEvent); // 释放
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOG.error("ERROR", cause);
        ctx.close();
    }
}
