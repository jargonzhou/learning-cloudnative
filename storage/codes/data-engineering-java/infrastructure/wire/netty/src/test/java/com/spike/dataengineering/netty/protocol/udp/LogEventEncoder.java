package com.spike.dataengineering.netty.protocol.udp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(LogEventDecoder.class);

    private final InetSocketAddress recipient;

    public LogEventEncoder(InetSocketAddress recipient) {
        this.recipient = recipient;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
        byte[] file = logEvent.getLogfile().getBytes(StandardCharsets.UTF_8);
        byte[] msg = logEvent.getMsg().getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = ctx.alloc().buffer(file.length + msg.length + 1);
        buf.clear();
        buf.writeBytes(file);
        buf.writeByte(LogEvent.SEPERATOR);
        buf.writeBytes(msg);

        LOG.info("Data: \n{}", ByteBufUtil.prettyHexDump(buf));

        out.add(new DatagramPacket(buf, recipient));
    }
}
