package com.spike.netty.example.codec;

import com.spike.netty.example.codec.CombinedByteCharCodec.ByteToCharacterDecoder;
import com.spike.netty.example.codec.CombinedByteCharCodec.CharacterToByteEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * codec use existed decoder and encoder
 *
 * @see CombinedChannelDuplexHandler
 */
public class CombinedByteCharCodec
        extends CombinedChannelDuplexHandler<ByteToCharacterDecoder, CharacterToByteEncoder> {

    public CombinedByteCharCodec() {
        super(new ByteToCharacterDecoder(), new CharacterToByteEncoder());
    }

    // ======================================== classes
    public static class ByteToCharacterDecoder extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
                throws Exception {
            while (in.readableBytes() >= 2) {
                out.add(in.readChar());
            }
        }
    }

    public static class CharacterToByteEncoder extends MessageToByteEncoder<Character> {
        @Override
        protected void encode(ChannelHandlerContext ctx, Character msg, ByteBuf out) throws Exception {
            out.writeByte(msg);
        }
    }
}
