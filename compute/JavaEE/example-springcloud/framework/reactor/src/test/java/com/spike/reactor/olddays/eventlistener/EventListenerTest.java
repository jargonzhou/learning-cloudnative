package com.spike.reactor.olddays.eventlistener;

import com.spike.reactor.domain.Temperature;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@SpringBootTest(classes = {EventListenerTest.TestApplication.class})
public class EventListenerTest {

    @Configuration
    public static class TestApplication implements ApplicationEventPublisherAware {
        private ApplicationEventPublisher eventPublisher;

        @Bean
        public TemperatureSensor sensor() {
            return new TemperatureSensor(eventPublisher);
        }

        @Bean
        public TestEventListener listener() {
            return new TestEventListener();
        }

        @Override
        public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
            this.eventPublisher = eventPublisher;
        }
    }

    public static class TestEventListener {

        @EventListener(classes = {Temperature.class})
        public void onEvent(Temperature temperature) {
            System.err.println(temperature);
        }
    }

    @Test
    public void contextLoaded() throws InterruptedException {
        Thread.sleep(10_000);
    }

}
