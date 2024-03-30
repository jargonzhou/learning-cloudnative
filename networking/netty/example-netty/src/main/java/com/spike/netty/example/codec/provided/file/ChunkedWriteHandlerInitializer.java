package com.spike.netty.example.codec.provided.file;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * 将文件由操作系统拷贝到内存中支持: 异步的写大文件流
 *
 * @see io.netty.handler.stream.ChunkedWriteHandler
 * @see io.netty.handler.stream.ChunkedStream
 * @see io.netty.handler.stream.ChunkedFile
 * @see io.netty.handler.stream.ChunkedNioFile
 * @see io.netty.handler.stream.ChunkedNioStream
 */
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {

    private final File file;
    private final SslContext sslContext;

    public ChunkedWriteHandlerInitializer(File file, SslContext sslContext) {
        this.file = file;
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new SslHandler(sslContext.newEngine(ch.alloc())));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new WriteStreamHandler());
    }

    // ======================================== classes
    public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            // 连接建立时写数据
            ctx.fireChannelActive();
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }
    }

}
