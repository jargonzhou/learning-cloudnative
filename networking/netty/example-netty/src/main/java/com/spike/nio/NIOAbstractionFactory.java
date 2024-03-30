package com.spike.nio;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;


/**
 * NIO抽象元素的工厂
 */
public final class NIOAbstractionFactory {

    /**
     * 生成ServerSocketChannel
     *
     * @param port
     * @param blocking 阻塞true, 非阻塞false
     * @return
     * @throws IOException
     */
    public static ServerSocketChannel serverSocketChannel(String host, int port, boolean blocking)
            throws IOException {
        if (StringUtils.isBlank(host)) {
            return null;
        }

        ServerSocketChannel ssc = ServerSocketChannel.open();

        ssc.socket().bind(new InetSocketAddress(host, port));
        ssc.configureBlocking(blocking);

        return ssc;
    }

}
