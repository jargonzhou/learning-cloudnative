package com.spike.nio2.socket;

import com.spike.nio2.SocketConstants;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * UDP server
 *
 * @author zhoujiagen
 */
public class UDPServer {
    public static void main(String[] args) {
        ByteBuffer bb = ByteBuffer.allocateDirect(SocketConstants.MAX_UDP_PACKET_SIZE);

        try (DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)) {
            if (!channel.isOpen()) {
                System.out.println("datagram channel cannot open");
                return;
            }
            System.out.println("server was opend");

            // set options
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            channel.setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024);

            // bind
            channel
                    .bind(new InetSocketAddress(SocketConstants.DEFAULT_HOST, SocketConstants.DEFAULT_PORT));

            while (true) {
                SocketAddress clientAddr = channel.receive(bb);

                bb.flip();
                System.out.println("received " + bb.limit() + " bytes from " + clientAddr.toString()
                        + "! sending then back...");

                channel.send(bb, clientAddr);

                bb.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
