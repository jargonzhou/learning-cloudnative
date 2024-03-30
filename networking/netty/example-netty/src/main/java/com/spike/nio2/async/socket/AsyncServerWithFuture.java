package com.spike.nio2.async.socket;

import com.spike.nio2.SocketConstants;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

/**
 * NIO2 features:<br/>
 * AsynchronousServerSocketChannel with Future
 *
 * @author zhoujiagen
 * @see AsynchronousServerSocketChannel
 */
public class AsyncServerWithFuture {
    public static void main(String[] args) {
        try (AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel.open()) {
            if (!channel.isOpen()) {
                System.out.println("cannot open AsynchronousServerSocketChannel");
                return;
            }

            // set options
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

            // bind
            channel
                    .bind(new InetSocketAddress(SocketConstants.DEFAULT_HOST, SocketConstants.DEFAULT_PORT));

            System.out.println("wait for connections...");
            while (true) {
                Future<AsynchronousSocketChannel> future = channel.accept();

                try (AsynchronousSocketChannel clientChannel = future.get()) {
                    System.out.println("incoming connection from " + clientChannel.getRemoteAddress());

                    ByteBuffer bb = ByteBuffer.allocateDirect(1024);
                    // echo
                    while (clientChannel.read(bb).get() != -1) {
                        bb.flip();
                        System.out.println(bb);
                        clientChannel.write(bb).get();

                        if (bb.hasRemaining()) {
                            bb.compact();
                        } else {
                            bb.clear();
                        }
                    }
                    System.out.println("incoming connection from " + clientChannel.getRemoteAddress()
                            + " served done");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
