package com.spike.dataengineering.netty.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

// example in 'Netty in Action' List 9.1
// io.netty.handler.codec.FixedLengthFrameDecoder
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) {
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf byteBuf,
                          List<Object> list) throws Exception {
        while (byteBuf.readableBytes() >= frameLength) {
            ByteBuf out = byteBuf.readBytes(frameLength);
            list.add(out);
        }
    }
}
