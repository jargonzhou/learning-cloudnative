package com.spike.dataengineering.netty.channel;

import io.netty.channel.epoll.Epoll;
import org.junit.jupiter.api.Test;

public class EpollTest {
    @Test
    public void supported() {
        boolean available = Epoll.isAvailable();
        System.err.println();
        if (!available) {
            Epoll.unavailabilityCause().printStackTrace();
        }
    }
}
