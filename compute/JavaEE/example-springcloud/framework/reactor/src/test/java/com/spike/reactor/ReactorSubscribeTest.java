package com.spike.reactor;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * @see Flux#subscribe()
 * @see Flux#subscribe(Consumer)
 * @see Flux#subscribe(Consumer, Consumer)
 * @see Flux#subscribe(Consumer, Consumer, Runnable)
 * @see Flux#subscribe(Consumer, Consumer, Runnable, Context)
 * @see Flux#subscribe(Consumer, Consumer, Runnable, Consumer)
 * @see Flux#subscribe(Subscriber)
 * @see BaseSubscriber
 */
public class ReactorSubscribeTest {
    @Test
    public void subscribe() throws InterruptedException {
        Disposable d = Flux.interval(Duration.ofMillis(50))
                .log()
                // request Long.MAX_VALUE
                .subscribe();

        Thread.sleep(1000L);

        // cancel subscription
        d.dispose();
    }

    @Test
    public void testSubscriber() {
        CustomSubscriber<String> subscriber = Mockito.spy(new CustomSubscriber<>());
        Flux.just("A", "B", "C")
                .subscribe(subscriber);

        Mockito.verify(subscriber, Mockito.times(3))
                .onNext(Mockito.anyString());
    }

    // org.reactivestreams.Subscriber
    // org.reactivestreams.Subscription
    // reactor.core.Disposable
    public static class CustomSubscriber<T> extends BaseSubscriber<T> {
        @Override
        public void hookOnSubscribe(Subscription subscription) {
            // request 1
            request(1);
        }

        @Override
        public void hookOnNext(T value) {
            System.err.println("onNext: " + value);

            // request 1
            request(1);
        }
    }
}
