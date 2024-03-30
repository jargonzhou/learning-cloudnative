package com.spike.nio2.socket;

import com.spike.nio.channel.socket.GreetingServerScoketChannel;
import com.spike.nio.channel.socket.GreetingSocketChannel;
import com.spike.nio2.SocketConstants;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * A blocking echo TCP server
 *
 * @author zhoujiagen
 * @see GreetingServerScoketChannel
 * @see GreetingSocketChannel
 */
public class BlockingTCPServer {
    public static void main(String[] args) {
        ByteBuffer bb = ByteBuffer.allocate(1024);

        // create server socket channel
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            // test server socket channel state
            if (serverChannel.isOpen()) {

                // blocking mode
                serverChannel.configureBlocking(true);

                // options
                serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
                serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

                // bind
                serverChannel.bind(new InetSocketAddress(SocketConstants.DEFAULT_HOST,
                        SocketConstants.DEFAULT_PORT));

                System.out.println("waiting for connections...");
                while (true) {
                    try (SocketChannel clientChannel = serverChannel.accept()) {
                        System.out.println("incoming connection from " + clientChannel.getRemoteAddress());

                        // echo back
                        while (clientChannel.read(bb) != -1) {
                            bb.flip();
                            clientChannel.write(bb);

                            if (bb.hasRemaining()) {
                                bb.compact();
                            } else {
                                bb.clear();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("server socket channel cannot open");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
