package com.spike.nio2.socket;

import com.spike.nio2.SocketConstants;

import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * conncetionless UDP client
 *
 * @author zhoujiagen
 */
public class UDPConnectionlessClient {
    public static void main(String[] args) {
        ByteBuffer helloBuffer = ByteBuffer.wrap("Echo this: i am a simple server!".getBytes());
        ByteBuffer bb = ByteBuffer.allocateDirect(SocketConstants.MAX_UDP_PACKET_SIZE);

        try (DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)) {
            if (!channel.isOpen()) {
                System.out.println("datagram channel cannot open");
                return;
            }

            // set options
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            channel.setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024);

            // send data
            InetSocketAddress serverAddress =
                    new InetSocketAddress(SocketConstants.DEFAULT_HOST, SocketConstants.DEFAULT_PORT);
            int sendBytes = channel.send(helloBuffer, serverAddress);
            System.out.println("send " + sendBytes + " bytes to server");

            // receive
            channel.receive(bb);
            bb.flip();
            System.out.println(SocketConstants.DEFAULT_CHARSET.decode(bb));
            bb.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
