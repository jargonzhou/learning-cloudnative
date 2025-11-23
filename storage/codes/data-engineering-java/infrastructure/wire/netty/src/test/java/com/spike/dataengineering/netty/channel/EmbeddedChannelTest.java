package com.spike.dataengineering.netty.channel;

import com.spike.dataengineering.netty.example.codec.AbsIntegerEncoder;
import com.spike.dataengineering.netty.example.codec.FrameChunkDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmbeddedChannelTest {

    // example in 'Netty in Action' List 9.2
    @Test
    public void inbound() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        ChannelHandler handler = new FixedLengthFrameDecoder(3);
        EmbeddedChannel channel = new EmbeddedChannel(handler);
        // write data to channel
        Assertions.assertThat(channel.writeInbound(input.retain())).isTrue();
        // mark channel finished
        Assertions.assertThat(channel.finish()).isTrue();

        // read message: expect 3 times
        ByteBuf read = channel.readInbound();
        Assertions.assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        read = channel.readInbound();
        Assertions.assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        read = channel.readInbound();
        Assertions.assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        read = channel.readInbound();
        Assertions.assertThat(read).isNull();

        Assertions.assertThat(buf.release()).isTrue();
    }

    @Test
    public void inbound2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        ChannelHandler handler = new FixedLengthFrameDecoder(3);
        EmbeddedChannel channel = new EmbeddedChannel(handler);
        // write data to channel
        Assertions.assertThat(channel.writeInbound(input.readBytes(2))).isFalse(); // not ready for read
        Assertions.assertThat(channel.writeInbound(input.readBytes(7))).isTrue();
        // mark channel finished
        Assertions.assertThat(channel.finish()).isTrue();

        // read message: expect 3 times
        ByteBuf read = channel.readInbound();
        Assertions.assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        read = channel.readInbound();
        Assertions.assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        read = channel.readInbound();
        Assertions.assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        read = channel.readInbound();
        Assertions.assertThat(read).isNull();

        Assertions.assertThat(buf.release()).isTrue();
    }

    @Test
    public void outbound() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);
        }
        ChannelHandler handler = new AbsIntegerEncoder();
        EmbeddedChannel channel = new EmbeddedChannel(handler);
        Assertions.assertThat(channel.writeOutbound(buf)).isTrue();
        Assertions.assertThat(channel.finish()).isTrue();

        // read bytes
        for (int i = 1; i < 10; i++) {
            Assertions.assertThat(channel.<Integer>readOutbound()).isEqualTo(i);
        }
    }

    @Test
    public void exception() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        ChannelHandler handler = new FrameChunkDecoder(3); // max: 3
        EmbeddedChannel channel = new EmbeddedChannel(handler);
        Assertions.assertThat(channel.writeInbound(input.readBytes(2))).isTrue();
        Assertions.assertThatExceptionOfType(TooLongFrameException.class) // expected exception
                .isThrownBy(() -> {
                    channel.writeInbound(input.readBytes(4));
                });
        Assertions.assertThat(channel.writeInbound(input.readBytes(3))).isTrue();
        Assertions.assertThat(channel.finish()).isTrue();

        // read frames
        ByteBuf read = channel.readInbound();
        Assertions.assertThat(read).isEqualTo(buf.readSlice(2));
        read.release();

        read = channel.readInbound();
        Assertions.assertThat(read).isEqualTo(buf.skipBytes(4).readSlice(3));
        read.release();

        Assertions.assertThat(buf.release()).isTrue();
    }
}
