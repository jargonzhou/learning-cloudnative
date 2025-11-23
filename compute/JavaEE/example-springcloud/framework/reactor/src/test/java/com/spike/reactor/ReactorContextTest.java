package com.spike.reactor;

import com.spike.reactor.support.Logs;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @see reactor.util.context.Context
 * @see reactor.util.context.ContextView
 * @see Mono#deferContextual(Function) 
 * @see Mono#contextWrite(Function) 
 * @see Flux#deferContextual(Function) 
 * @see Mono#contextWrite(Function)
 */
public class ReactorContextTest {
    @Test
    public void contextSimple() {
        Logger logger = Logs.getLogger("contextSimple");

        String key = "message";
        Mono<String> mono = Mono.just("Hello")
                .flatMap(v -> Mono.deferContextual(contextView ->
                        Mono.just(v + " " + contextView.getOrDefault(key, "World"))))
                .contextWrite(context -> context.put(key, "World!")) // effect on above operators
                .doOnEach(v -> logger.info("{}", v));

        StepVerifier.create(mono)
                .expectNext("Hello World!")
                .verifyComplete();
    }

    @Test
    public void contextSimple2() {
        Logger logger = Logs.getLogger("contextSimple2");

        String key = "message";
        Mono<String> mono = Mono.just("Hello")
                .contextWrite(context -> context.put(key, "World!")) // effect on above operators
                .flatMap(v -> Mono.deferContextual(contextView ->
                        Mono.just(v + " " + contextView.getOrDefault(key, "World"))))
                .doOnEach(v -> logger.info("{}", v));

        StepVerifier.create(mono)
                .expectNext("Hello World")
                .verifyComplete();
    }


    @Test
    public void context() {
        Logger logger = Logs.getLogger("context");

        final String key = "random";
        Flux<Integer> flux = Flux.range(0, 10)
                .flatMap(v -> Mono.deferContextual(contextView ->
                        Mono.just(v + (Integer) contextView.getOrEmpty(key).orElse(0))))
                .contextWrite(context -> context.put(key, 1)) // BUG: return context;
                .doOnNext(v -> logger.info("{}", v))

                .publishOn(Schedulers.parallel()) // execute in parallel

                .flatMap(v -> Mono.deferContextual(contextView ->
                        Mono.just(v + (Integer) contextView.getOrEmpty(key).orElse(0))))
                .contextWrite(context -> context.put(key, 10))
                .doOnNext(v -> logger.info("{}", v));


        Iterable<Integer> expected = Stream.iterate(11, v -> v < 21, v -> v + 1).toList();
        StepVerifier.create(flux)
                .expectNextSequence(expected)
                .verifyComplete();

    }
}
