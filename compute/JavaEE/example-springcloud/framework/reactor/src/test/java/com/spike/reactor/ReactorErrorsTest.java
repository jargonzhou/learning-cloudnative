package com.spike.reactor;

import com.spike.reactor.support.Logs;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <pre>
 * static fallback value:
 * @see Flux#onErrorReturn(Object)
 *
 * fallback method, dynamic fallback value, cacth and rethrow:
 * @see Flux#onErrorResume(Function)
 *
 * catch and rethrow:
 * @see Flux#onErrorMap(Function)
 *
 * catch and swallow the error:
 * @see Flux#onErrorComplete()
 *
 * log or react on the side:
 * @see Flux#doOnError(Consumer)
 * @see Flux#doOnError(Class, Consumer)
 * @see Flux#doOnError(Predicate, Consumer)
 *
 * the finally block:
 * @see Flux#doFinally(Consumer)
 *
 * exceptions:
 * @see reactor.core.Exceptions#propagate(Throwable)
 * @see reactor.core.Exceptions#unwrap(Throwable)
 *
 * retry:
 * @see Flux#retry()
 * @see Flux#retryWhen(Retry)
 *
 * @see Flux#switchIfEmpty(Publisher)
 *
 * @see Flux#timeout(Duration)
 * </pre>
 */
public class ReactorErrorsTest {

    public static boolean FAIL_RB = false; // for test

    private static final Logger LOG_RETRY = Logs.getLogger("retry");

    @Test
    public void retry() throws InterruptedException {
        LOG_RETRY.info("case 1: success");
        Flux<String> flux = Flux.just("user-1")
                .flatMap(user -> recommendedBooks(user)
                        // retry
                        .retryWhen(Retry.backoff(4, Duration.ofMillis(100)))
                        // timeout
                        .timeout(Duration.ofSeconds(3))
                        // error resume
                        .onErrorResume(e -> Flux.just("The Martian")));

        flux.log().subscribe();

        Thread.sleep(1000L);

        LOG_RETRY.info("case 2: failure");
        FAIL_RB = true;
        Flux<String> flux2 = Flux.just("user-1")
                .flatMap(user -> recommendedBooks(user)
                        // retry
                        .retryWhen(Retry.backoff(4, Duration.ofMillis(100)))
                        // timeout
                        .timeout(Duration.ofSeconds(3))
                        // error resume
                        .onErrorResume(e -> Flux.just("The Martian")));

        flux2.log().subscribe();


        Thread.sleep(5000L);
        // how to test this???
    }

    private Flux<String> recommendedBooks(String userId) {
        return Flux.defer(() -> {
                    if (FAIL_RB) {
                        // fail
                        return Flux.<String>error(new RuntimeException("Err"))
                                .delaySequence(Duration.ofMillis(100));
                    } else {
                        return Flux.just("Blue Mars", "The Expanse")
                                .delaySequence(Duration.ofMillis(50));
                    }
                })
                .doOnSubscribe(s -> LOG_RETRY.info("Request for {}", userId));
    }

    @Test
    public void uncheckedException() {
        Logger logger = Logs.getLogger("uncheckedException");
        Flux.just("1")
                .map(v -> {
                    throw new IllegalArgumentException(v);
                })
                .subscribe(v -> logger.info("{}", v),
                        e -> logger.error(e.getMessage(), e) // ERROR [main]
                );
    }

    @Test
    public void checkedException() {
        Logger logger = Logs.getLogger("checkedException");

        Flux.range(1, 10)
                .map(i -> {
                    try {
                        return convert(i);
                    } catch (IOException e) {
                        throw Exceptions.propagate(e); // wrap in unchecked exception
                    }
                })
                .log()
                .subscribe(v -> logger.info("{}", v),
                        e -> {
                            if (Exceptions.unwrap(e) instanceof IOException) {
                                logger.error("IO error", e); // ERROR [main]: IO error
                            } else {
                                logger.error(e.getMessage(), e);
                            }
                        }
                );

        Flux.range(1, 10)
                // flatMap
                .flatMap(i -> {
                    try {
                        return Flux.just(convert(i));
                    } catch (IOException e) {
                        return Flux.error(e); // error
                    }
                })
                .log()
                .subscribe(v -> logger.info("{}", v),
                        e -> {
                            logger.error(e.getMessage(), e); // ERROR [main]: boom 4
                        }
                );
    }

    private String convert(int v) throws IOException {
        if (v > 3) {
            throw new IOException("boom " + v);
        }
        return "OK " + v;
    }
}
