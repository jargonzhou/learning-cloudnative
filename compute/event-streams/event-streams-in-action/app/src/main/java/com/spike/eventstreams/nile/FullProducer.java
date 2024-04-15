package com.spike.eventstreams.nile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.net.InetAddress;

public class FullProducer implements IProducer {
    private final KafkaProducer<String, String> producer;
    private final String goodTopic;
    private final String badTopic;
    private final DomainService domainService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public FullProducer(String servers, String goodTopic, String badTopic, DomainService domainService) {
        this.producer = new KafkaProducer<>(IProducer.createConfig(servers));
        this.goodTopic = goodTopic;
        this.badTopic = badTopic;
        this.domainService = domainService;
    }

    private String newBadEvent(String message) {
        try {
            return MAPPER.writeValueAsString(new BadEvent(message));
        } catch (JsonProcessingException e) {
            return "{\"error\": \"" + message + "\"}";
        }
    }

    @Override
    public void process(String value) {
        try {
            // not use domain POJO
            ObjectNode root = (ObjectNode) MAPPER.readTree(value);
            JsonNode ipNode = root.path("shopper").path("ipAddress");
            if (ipNode.isMissingNode()) {
                IProducer.write(this.producer, this.badTopic, newBadEvent("shopper.ipAddress missing"));
            } else {
                InetAddress ip = InetAddress.getByName(ipNode.textValue());
                DomainService.City cityResponse = domainService.city(ip);
                root.withObject("shopper").put("country", cityResponse.country);
                root.withObject("shopper").put("city", cityResponse.city);
                IProducer.write(this.producer, this.goodTopic, MAPPER.writeValueAsString(root));
            }
        } catch (Exception e) {
            IProducer.write(this.producer, this.badTopic, newBadEvent(e.getClass().getSimpleName() + ": " + e.getMessage()));
        }
    }

}
