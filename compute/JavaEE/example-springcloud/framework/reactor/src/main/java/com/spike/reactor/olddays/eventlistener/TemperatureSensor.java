package com.spike.reactor.olddays.eventlistener;

import com.spike.reactor.domain.Temperature;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TemperatureSensor {
    // bound to Spring application context
    private final ApplicationEventPublisher eventPublisher;
    private final Random rnd = new Random();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public TemperatureSensor(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    // trigger start
    @PostConstruct
    public void startProcessing() {
        this.executorService.schedule(this::probe, 1, TimeUnit.SECONDS);
    }

    private void probe() {
        double temperature = 16 + rnd.nextGaussian() * 10;
        eventPublisher.publishEvent(new Temperature(temperature));
        // schedule next
        executorService.schedule(this::probe, rnd.nextInt(5000), TimeUnit.MILLISECONDS);
    }
}
