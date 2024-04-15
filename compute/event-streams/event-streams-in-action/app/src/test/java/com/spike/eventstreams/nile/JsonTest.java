package com.spike.eventstreams.nile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.spike.eventstreams.GenEventsTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class JsonTest {

    @Test
    public void testRawEventsProcessing() throws IOException {
        System.out.println(System.getProperty("user.dir"));

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> rawEvents = GenEventsTest.readRawEvents();

        for (String rawEvent : rawEvents) {
            ObjectNode root = (ObjectNode) objectMapper.readTree(rawEvent);
            JsonNode ipNode = root.path("shopper").path("ipAddress");
            if (ipNode.isMissingNode()) {
                System.out.println(objectMapper.writeValueAsString(new BadEvent("shopper.ipAddress missing")));
            }
            InetAddress ip = InetAddress.getByName(ipNode.textValue());
            root.withObject("shopper").put("country", "CN");
            root.withObject("shopper").put("city", "Shanghai");
            System.out.println(objectMapper.writeValueAsString(root));
        }


    }
}
