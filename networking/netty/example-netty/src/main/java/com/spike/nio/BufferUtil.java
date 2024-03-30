package com.spike.nio;

import java.nio.Buffer;

/**
 * Buffer工具类
 */
public final class BufferUtil {
    /**
     * 查看Buffer的详情
     *
     * @param buffer
     * @return
     */
    public static String details(Buffer buffer) {
        StringBuffer sb = new StringBuffer();

        sb.append("position=");
        sb.append(buffer.position());
        sb.append(", capacity=");
        sb.append(buffer.capacity());
        sb.append(", limit=");
        sb.append(buffer.limit());
        sb.append(": ").append(buffer.toString());

        return sb.toString();
    }
}
