package com.spike.dataengineering.nio.channel.selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import static com.spike.dataengineering.nio.channel.selector.SelectorTest.SERVER_PORT;

// example: 'Java IO, NIO and NIO.2' - 8. Selectors
// send random number repeatedly unless closed.
public class Client {
    private static final Logger LOG = LoggerFactory.getLogger(Client.class);

    private Selector selector;
    private SocketChannel sc;

    private final String serverHost;
    private final int serverPort;

    ByteBuffer buffer = ByteBuffer.allocateDirect(2 * 1024);

    Charset charset = StandardCharsets.UTF_8;
    CharsetDecoder decoder = charset.newDecoder();
    ByteBuffer randomBuffer;
    CharBuffer charBuffer;


    public Client(int serverPort) {
        this.serverHost = "127.0.0.1";
        this.serverPort = serverPort;
    }

    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void start() {
        try (Selector selector = Selector.open();
             SocketChannel sc = SocketChannel.open()) {

            if (!sc.isOpen() || !selector.isOpen()) {
                LOG.warn("Socket channel or select cannot be opened!");
                System.exit(-1);
            }

            sc.configureBlocking(false); // non-blocking
            sc.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
            sc.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
            sc.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

            // register to selector
            LOG.info("Register {} to {}", sc, selector);
            sc.register(selector, SelectionKey.OP_CONNECT); // connect

            LOG.info("Connect to {}:{}", serverHost, serverPort);
            sc.connect(new InetSocketAddress(serverHost, serverPort));

            // wait for connection
            while (selector.select(1000) > 0) {
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();

                    try (SocketChannel scInKey = (SocketChannel) key.channel()) {
                        if (key.isConnectable()) {
                            LOG.info("Connected: {}", scInKey);
                            if (scInKey.isConnectionPending()) { // close pending connections
                                scInKey.finishConnect();
                            }

                            // read/write
                            int numRead = -1;
                            while ((numRead = scInKey.read(buffer)) != -1) {
                                // expect greeting response
                                if (numRead == 0) {
                                    continue;
                                }

                                buffer.flip();
                                charBuffer = decoder.decode(buffer);
                                LOG.info("Response[{}]: {}", numRead, charBuffer);

                                if (buffer.hasRemaining()) {
                                    buffer.compact();
                                } else {
                                    buffer.clear();
                                }

                                int r = new Random().nextInt(100);
                                if (r == 50) {
                                    LOG.info("Close channel: {}", scInKey);
                                    break;
                                } else {
                                    randomBuffer = ByteBuffer.wrap(String.valueOf(r).getBytes());
                                    scInKey.write(randomBuffer);

                                    try {
                                        Thread.sleep(1500);
                                    } catch (InterruptedException e) {
                                        // ignore
                                    }
                                }
                            }
                        }
                    }

                    iter.remove();
                }

            }

        } catch (IOException e) {
            LOG.error("IO ERROR", e);
        }
    }


    public void stop() {
        LOG.info("Stop client");
        if (sc != null) {
            try {
                LOG.info("Close channel: {}", sc);
                sc.close();
            } catch (IOException e) {
                LOG.error("Close channel failed: {}", sc, e);
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

    public static void main(String[] args) throws IOException {
        Client client = new Client(SERVER_PORT);
        try {
            client.start();
        } finally {
            client.stop();
        }
    }
}