package com.spike.dataengineering.netty.protocol.udp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {
    private static final Logger LOG = LoggerFactory.getLogger(LogEventDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          DatagramPacket datagramPacket,
                          List<Object> out) throws Exception {
        ByteBuf data = datagramPacket.content();
        LOG.info("Data: \n{}", ByteBufUtil.prettyHexDump(data));

        int sepIndex = data.indexOf(0, data.readableBytes(), LogEvent.SEPERATOR);
//        String filename = data.slice(0, sepIndex).toString(StandardCharsets.UTF_8);
//        // slice: index, length
//        String msg = data.slice(sepIndex + 1, data.readableBytes() - filename.length() - 1).toString(StandardCharsets.UTF_8);
        String filename = data.readBytes(sepIndex).toString(StandardCharsets.UTF_8);
        data.skipBytes(1);
        String msg = data.readBytes(data.readableBytes()).toString(StandardCharsets.UTF_8);

        LogEvent logEvent = new LogEvent(datagramPacket.sender(), filename, msg, System.currentTimeMillis());
        out.add(logEvent);
    }
}
