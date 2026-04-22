package com.spike.dataengineering.netty.protocol.udp;

import java.net.InetSocketAddress;

public class LogEvent {
//    public static final byte SEPERATOR = (byte) ':';
    public static final byte SEPERATOR = (byte) '$';

    // message source
    private final InetSocketAddress source;
    // message log file
    private final String logfile;
    // message content
    private final String msg;
    // received timestamp
    private final long received;

    // outgoing
    public LogEvent(String logfile, String msg) {
        this(null, logfile, msg, -1);
    }

    // incoming
    public LogEvent(InetSocketAddress source, String logfile, String msg, long received) {
        this.source = source;
        this.logfile = logfile;
        this.msg = msg;
        this.received = received;
    }

    public InetSocketAddress getSource() {
        return source;
    }

    public String getLogfile() {
        return logfile;
    }

    public String getMsg() {
        return msg;
    }

    public long getReceived() {
        return received;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(received)
                .append(" [").append(source.toString()).append("] ")
                .append(" [").append(logfile).append("] ")
                .append(msg);
        return sb.toString();
    }
}
