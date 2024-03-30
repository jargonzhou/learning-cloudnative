package com.spike.netty.example.codec.provided.file;

import io.netty.channel.*;

import java.io.File;
import java.io.FileInputStream;

/**
 * <pre>
 * 文件传输支持
 *
 * a region of a file that is sent via a Channel that supports zero-copy file transfer
 * </pre>
 *
 * @see io.netty.channel.FileRegion
 */
public class FileTransferHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        File file = new File(msg);

        final FileInputStream fis = new FileInputStream(file);
        FileRegion fileRegion = new DefaultFileRegion(fis.getChannel(), 0, file.length());

        ChannelFuture cf = ctx.channel().writeAndFlush(fileRegion);
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    System.out.println("Transfer failed: " + future.cause().toString());
                }
                fis.close();
            }
        });
    }
}
