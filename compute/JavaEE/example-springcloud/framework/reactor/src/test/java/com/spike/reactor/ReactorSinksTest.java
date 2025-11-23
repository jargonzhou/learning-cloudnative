package com.spike.reactor;

import com.spike.reactor.support.Logs;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import reactor.core.publisher.SignalType;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Processors will be removed in 3.5. Prefer using Sinks.Many instead.
 * <pre>
 * @see org.reactivestreams.Processor
 *
 * @see reactor.core.publisher.DirectProcessor
 * @see reactor.core.publisher.UnicastProcessor
 * @see reactor.core.publisher.EmitterProcessor
 * @see reactor.core.publisher.ReplayProcessor
 * </pre>
 *
 * @see reactor.core.publisher.Sinks.One
 * @see reactor.core.publisher.Sinks.Many
 * @see Sinks.ManySpec#unicast()
 * @see Sinks.ManySpec#multicast()
 */
public class ReactorSinksTest {

    //
    // sink:
    // allow safe manual triggering of signals in a standalone fashion,
    // creating a Publisher-like structure capable of dealing ith multiple Subscriber.

    @Test
    public void one() throws InterruptedException {
        Sinks.EmitFailureHandler handler = new Sinks.EmitFailureHandler() {
            @Override
            public boolean onEmitFailure(SignalType signalType, Sinks.EmitResult emitResult) {
                return false; // not retries
            }
        };
        Sinks.One<Integer> one = Sinks.one();

        one.asMono().log()
                .subscribe();

        one.emitValue(1, handler);

        Thread.sleep(500);
    }

    @Test
    public void many() throws InterruptedException {
        Logger logger = Logs.getLogger("many");
        Sinks.Many<Integer> many = Sinks.many().replay().all();
        ExecutorService es = Executors.newFixedThreadPool(3);

        // Spec. Rule 1.3 - onSubscribe, onNext, onError and onComplete signaled to a Subscriber MUST be signaled serially.
        es.execute(() -> {
            logger.info("emit 1");
            many.emitNext(1, Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(2)));
        });
        es.execute(() -> {
            logger.info("emit 2");
            many.emitNext(2, Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(2)));
        });
        es.execute(() -> {
            logger.info("emit 3");
            many.emitNext(3, Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(2)));

            logger.info("emit 4");
            Sinks.EmitResult er = many.tryEmitNext(4);
            logger.info("emit 4: {}", er);
        });


        many.asFlux().log().subscribe();

        Thread.sleep(5000);
    }
}
