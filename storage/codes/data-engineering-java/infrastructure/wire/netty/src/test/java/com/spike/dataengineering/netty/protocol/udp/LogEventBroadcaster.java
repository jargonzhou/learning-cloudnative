package com.spike.dataengineering.netty.protocol.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

public class LogEventBroadcaster {
    private static final Logger LOG = LoggerFactory.getLogger(LogEventBroadcaster.class);

    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress recipient, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap()
                .group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true) // broadcast
                .handler(new LogEventEncoder(recipient)); // encoder
        this.file = file;
    }

    public void run() throws IOException {
        Channel channel = bootstrap.bind(0).syncUninterruptibly().channel();
        LOG.info("{} running on {}", this.getClass().getSimpleName(), channel);

        long pointer = 0;
        for (; ; ) {
            long len = file.length();
            if (len < pointer) {
                // file was reset
                pointer = len;
            } else if (len > pointer) {
                // content was added
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);
                String line;
                LogEvent logEvent;
                while ((line = raf.readLine()) != null) {
                    LOG.info("Read line to send: {}", line);
                    logEvent = new LogEvent(file.getAbsolutePath(), line);
                    channel.writeAndFlush(logEvent);
                }
                pointer = raf.getFilePointer();
                raf.close();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.interrupted(); // clear interrupt status
                break;
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws IOException {
        LOG.info("Working dir: {}", System.getProperty("user.dir"));
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(
                new InetSocketAddress("255.255.255.255", 18081), // monitor port
                new File("infrastructure/wire/netty/src/test/resources/udp.broadcast.log"));

        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }
}
