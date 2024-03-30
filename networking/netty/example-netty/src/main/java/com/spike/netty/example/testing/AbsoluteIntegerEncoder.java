package com.spike.netty.example.testing;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 整数绝对值{@link MessageToMessageEncoder}
 */
public class AbsoluteIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        while (msg.readableBytes() >= Integer.SIZE / Byte.SIZE) {
            int value = Math.abs(msg.readInt());
            out.add(value);
        }
    }

}
