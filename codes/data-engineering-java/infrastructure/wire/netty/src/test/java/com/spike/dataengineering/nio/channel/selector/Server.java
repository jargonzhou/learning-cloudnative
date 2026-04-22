package com.spike.dataengineering.nio.channel.selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

import static com.spike.dataengineering.nio.channel.selector.SelectorTest.SERVER_PORT;

// example: 'Java IO, NIO and NIO.2' - 8. Selectors
// send greeting when accepting connection
// echo client requests
public class Server {
    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private Selector selector;
    private ServerSocketChannel ssc;
    private final int port;

    // internal data buffer
    private final Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
    // internal read buffer
    private final ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (Selector selector = Selector.open();
             ServerSocketChannel ssc = ServerSocketChannel.open()) {
            this.selector = selector;
            this.ssc = ssc;
            if (!ssc.isOpen() || !selector.isOpen()) {
                LOG.warn("Server socket channel or select cannot be opened!");
                System.exit(-1);
            }

            ssc.configureBlocking(false); // non-blocking
            // options
            ssc.setOption(StandardSocketOptions.SO_RCVBUF, 256 * 1024);
            ssc.setOption(StandardSocketOptions.SO_REUSEADDR, true);

            // bind
            LOG.info("Bind to port: {}", SERVER_PORT);
            ssc.bind(new InetSocketAddress(port));

            // register to selector
            LOG.info("Register {} to {}", ssc, selector);
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // select: blocking
                // ready keys since last invoke
                // returns when:
                // at least one channel is selected,
                // Selector#wakeup invoked,
                // current thread is interrupted
                LOG.info("Do select...");
                int readyKeyCnt = selector.select(); // blocking
                // select: non-blocking
                // int readyKeyCnt = selector.selectNow();
                if (readyKeyCnt == 0) {
                    LOG.info("Found nothing in select");
                    continue;
                } else {
                    LOG.info("Found {} keys in select", readyKeyCnt);
                }

                // selected-keys: the ready set
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    LOG.info("Processing key: {}", key);
                    if (!key.isValid()) {
                        LOG.warn("Processing key NOT VALID: {}", key);
                        continue;
                    }

                    if (key.isAcceptable()) {
                        this.processAccept(key);
                    } else if (key.isReadable()) {
                        this.processRead(key);
                    } else if (key.isWritable()) {
                        this.processWrite(key);
                    }

                    // remove the key
                    iter.remove();
                }
            }

        } catch (IOException e) {
            LOG.error("IO ERROR", e);
        }
    }

    private void processAccept(SelectionKey key) {
        LOG.info("Processing accept...");
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel clientChannel = channel.accept();
            if (clientChannel == null) {
                LOG.warn("Accepted channel NULL");
                return;
            }
            clientChannel.configureBlocking(false); // non-blocking
            while (!clientChannel.isConnected()) {
                LOG.info("Waiting to connected: {}", clientChannel);
            }
            long currentTime = System.currentTimeMillis();
            LOG.info("Writing current time {} to channel {}", currentTime, clientChannel);
            clientChannel.write(ByteBuffer.wrap("Hello, now is ".concat(String.valueOf(currentTime))
                    .concat("!").getBytes()));

            keepDataTrack.put(clientChannel, new ArrayList<>());

            // register to selector: interest op set: read
            clientChannel.register(selector, SelectionKey.OP_READ);

        } catch (IOException e) {
            LOG.error("Accept IO ERROR", e);
        }
    }

    private void processRead(SelectionKey key) {
        LOG.info("Processing read...");
        SocketChannel channel = (SocketChannel) key.channel();

        buffer.clear();
        int numRead = -1;
        try {
            numRead = channel.read(buffer);
        } catch (IOException e) {
            LOG.error("Read SocketChannel IO ERROR", e);
        }
        if (numRead == -1) {
            this.keepDataTrack.remove(channel);
            LOG.info("Close channel: {}", channel);
            try {
                channel.close();
            } catch (IOException e) {
                LOG.error("Close channel failed: {}", channel, e);
            }
            // add to cancelled-key set
            key.cancel();
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        LOG.info("Request from channel {}: {}", channel, new String(data));

        // response to client
        this.responseRead(key, data);
    }

    private void responseRead(SelectionKey key, byte[] data) {
        SocketChannel channel = (SocketChannel) key.channel();
        List<byte[]> channelData = keepDataTrack.get(channel);
        channelData.add(data);

        // set interest op set: write
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void processWrite(SelectionKey key) {
        LOG.info("Processing write...");
        SocketChannel channel = (SocketChannel) key.channel();

        List<byte[]> channelData = keepDataTrack.get(channel);
        if (channelData == null) {
            return;
        }
        Iterator<byte[]> iter = channelData.iterator();
        while (iter.hasNext()) {
            byte[] data = iter.next();
            try {
                LOG.info("Response to channel {}: {}", channel, new String(data));
                channel.write(ByteBuffer.wrap(data));
            } catch (IOException e) {
                LOG.error("Response to channel {} failed: {}", channel, new String(data), e);
            }
            iter.remove(); // remove
        }

        // set interest op set: read
        key.interestOps(SelectionKey.OP_READ);
    }


    public void stop() {
        LOG.info("Stop server");
        if (ssc != null) {
            try {
                LOG.info("Close channel: {}", ssc);
                ssc.close();
            } catch (IOException e) {
                LOG.error("Close channel failed: {}", ssc, e);
            }
        }
        if (selector != null) {
            try {
                LOG.info("Close selector: {}", selector);
                selector.close();
            } catch (IOException e) {
                LOG.error("Close selector failed: {}", selector, e);
            }
        }
    }


    public static void main(String[] args) {
        Server server = new Server(SERVER_PORT);
        try {
            server.start();
        } finally {
            server.stop();
        }
    }
}