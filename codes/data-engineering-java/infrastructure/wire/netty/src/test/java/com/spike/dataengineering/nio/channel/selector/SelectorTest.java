package com.spike.dataengineering.nio.channel.selector;

/**
 * <pre>
 * {@link java.nio.channels.Selector}
 * {@link java.nio.channels.SelectableChannel}
 *
 * 注册: {@link java.nio.channels.SelectionKey}
 * - interest set: 下次select时会测试是否准备好的操作集合
 * - ready set: selector中已准备好的channel上操作集合
 * </pre>
 */
public class SelectorTest {
    // POSIX select() system call

    // SelectableChannel:
    // ServerSocketChannel
    // SocketChannel
    // DatagramChannel
    // java.nio.channels.Pipe.SourceChannel
    // java.nio.channels.Pipe.SinkChannel

    public static final int SERVER_PORT = 18080;
}
