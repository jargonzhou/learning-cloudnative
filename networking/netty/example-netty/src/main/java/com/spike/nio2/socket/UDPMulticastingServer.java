package com.spike.nio2.socket;

import com.spike.nio2.SocketConstants;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;

/**
 * multicast server
 *
 * @author zhoujiagen
 */
public class UDPMulticastingServer {

    // public static void main(String[] args) throws Exception {
    // // check multicast supported
    // SocketConstants.rendererNetworkInterface();
    // }

    public static void main(String[] args) {
        try (DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)) {
            if (!channel.isOpen()) {
                System.out.println("datagram channel cannot open");
                return;
            }

            // set options
            NetworkInterface ni = NetworkInterface.getByName(SocketConstants.DEFAULT_NETINTERFACE_NAME);
            channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

            while (true) {
                Thread.sleep(2000L);

                System.out.println("sending data ...");
                InetSocketAddress groupAddress =
                        new InetSocketAddress(InetAddress.getByName(SocketConstants.DEFAULT_MULTICAST_GROUPIP),
                                SocketConstants.DEFAULT_PORT);
                channel.send(ByteBuffer.wrap(new Date().toString().getBytes()), groupAddress);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
