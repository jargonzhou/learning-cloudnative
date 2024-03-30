package com.spike.netty.example.testing;

import com.spike.netty.support.ByteBufs;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 限制帧最大长度的{@link ByteToMessageDecoder}
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {

    private final int maxLength;

    public FrameChunkDecoder(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();
        if (readableBytes > maxLength) {
            ByteBufs.CLEAR(in);
            throw new IllegalStateException(//
                    String.format("readableBytes(%d) > maxLength(%d)", readableBytes, maxLength));
        }

        out.add(in.readBytes(readableBytes));
    }

}
