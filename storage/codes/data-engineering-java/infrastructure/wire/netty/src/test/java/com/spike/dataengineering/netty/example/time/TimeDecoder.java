package com.spike.dataengineering.netty.example.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

// alternative: ReplayingDecoder
public class TimeDecoder extends ByteToMessageDecoder { // (1) a ChannelInboundHandler dealing with fragmentation issue
    // keep calling `decode()` until it adds nothing to output
    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf byteBuf,
                          List<Object> list) throws Exception { // (2) input: byteBuf, internal cumulative buffer, output: list
        if (byteBuf.readableBytes() < 4) {
            return; // (3) add nothing to output when not enough data in buffer
        }

//        list.add(byteBuf.readBytes(4)); // (4) add to output: decode a message successfully, discard the read part of the buffer

        // use POJO
        list.add(new UnixTime(byteBuf.readUnsignedInt()));
    }
}
