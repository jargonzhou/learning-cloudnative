package com.spike.dataengineering.netty.example.discard;

import org.apache.commons.net.telnet.TelnetClient;
import org.assertj.core.api.Assertions;
import org.assertj.core.internal.InputStreams;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DiscardServerTest {
    private static final Logger LOG = LoggerFactory.getLogger(DiscardServerTest.class);

    @Test
    public void telnet() throws IOException {
        // given:  server started

        // example: https://commons.apache.org/proper/commons-net/examples/telnet/TelnetClientExample.java
        TelnetClient tc = new TelnetClient();
        tc.connect("localhost", 18080);

        OutputStream output = tc.getOutputStream();
        byte[] data = "Hello there".getBytes();
        output.write(data);
        output.flush();

//        InputStream input = tc.getInputStream();
//        Assertions.assertThat(input).isEmpty(); // blocked

        tc.disconnect();
    }

    @Test
    public void client() throws InterruptedException {

    }
}
