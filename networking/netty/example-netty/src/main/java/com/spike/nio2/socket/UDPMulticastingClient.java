package com.spike.nio2.socket;

import com.spike.nio2.SocketConstants;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;

/**
 * multicast client
 *
 * @author zhoujiagen
 */
public class UDPMulticastingClient {
    public static void main(String[] args) {
        ByteBuffer bb = ByteBuffer.allocateDirect(SocketConstants.MAX_UDP_PACKET_SIZE);

        try (DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)) {
            InetAddress groupAddress = InetAddress.getByName(SocketConstants.DEFAULT_MULTICAST_GROUPIP);
            if (!groupAddress.isMulticastAddress()) {
                System.out.println("not a valid multicast address");
                return;
            }

            if (!channel.isOpen()) {
                System.out.println("datagram channel cannot open");
                return;
            }

            // set options
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

            // bind
            channel.bind(new InetSocketAddress(SocketConstants.DEFAULT_PORT));

            // join the multicast group
            NetworkInterface ni = NetworkInterface.getByName(SocketConstants.DEFAULT_NETINTERFACE_NAME);
            MembershipKey key = channel.join(groupAddress, ni);

            // wait for data
            while (true) {
                if (key.isValid()) {
                    channel.receive(bb);
                    bb.flip();
                    System.out.println(SocketConstants.DEFAULT_CHARSET.decode(bb));
                    bb.clear();
                } else {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
