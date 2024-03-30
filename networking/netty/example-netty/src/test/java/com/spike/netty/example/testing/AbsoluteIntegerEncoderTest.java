package com.spike.netty.example.testing;

import com.spike.netty.support.ByteBufs;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @see EmbeddedChannel#writeOutbound(Object...)
 * @see EmbeddedChannel#readOutbound()
 * @see EmbeddedChannel#finish()
 */
public class AbsoluteIntegerEncoderTest {

    @Test
    public void simple() {
        ByteBuf bb = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            bb.writeInt(-1 * i);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsoluteIntegerEncoder());

        // 写outbound数据
        Assert.assertTrue(channel.writeOutbound(bb));
        Assert.assertTrue(channel.finish());

        // 读outbound数据
        for (int i = 1; i < 10; i++) {
            Assert.assertTrue(channel.readOutbound().equals(i));
        }
        Assert.assertNull(channel.readOutbound());
        ByteBufs.RELEASE(bb);
    }
}
