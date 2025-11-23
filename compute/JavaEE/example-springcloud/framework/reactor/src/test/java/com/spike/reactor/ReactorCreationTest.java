package com.spike.reactor;

import com.spike.reactor.support.Logs;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * <pre>
 * @see Mono#just(Object)
 * @see Mono#justOrEmpty(Object)
 * @see Mono#justOrEmpty(Optional)
 * @see Mono#fromCallable(Callable)
 * @see Mono#fromRunnable(Runnable)
 * @see Mono#fromSupplier(Supplier)
 * @see Mono#fromFuture(CompletableFuture)
 * @see Mono#fromCompletionStage(CompletionStage)
 * @see Mono#from(Publisher)
 *
 * @see Flux#range(int, int)
 *
 * @see Mono#empty()
 * @see Mono#never()
 * @see Mono#error(Throwable)
 *
 * @see Mono#defer(Supplier)
 *
 * @see Flux#just(Object[])
 * @see Flux#fromArray(Object[])
 * @see Flux#fromIterable(Iterable)
 * @see Flux#range(int, int)
 * @see Flux#from(Publisher)
 *
 * @see Flux#empty()
 * @see Flux#never()
 * @see Flux#error(Throwable)
 *
 * @see Flux#defer(Supplier)
 *
 * factory methods:
 * @see Flux#push(Consumer)
 * @see Flux#create(Consumer)
 * @see Flux#generate(Consumer)
 * 
 * instance methods:
 * @see Flux#handle(BiConsumer)
 *
 * wrapping disposable resources into reactive methods:
 * @see Flux#using(Callable, Function)
 * @see Flux#usingWhen(Publisher, Function, Function)
 * </pre>
 */
public class ReactorCreationTest {

    @Test
    public void monoJust() {
        // org.reactivestreams.Publisher
        Mono<String> mono = Mono.just("A")
                .log() // logging
                ;

        StepVerifier.create(mono)
                .expectNextMatches(s -> s.equals("A"))
                .verifyComplete();
    }

    @Test
    public void fluxJust() {
        // org.reactivestreams.Publisher
        Flux<String> flux = Flux.just("Hello", "world")
                .log();

        StepVerifier.create(flux)
                .expectNext("Hello", "world")
                .verifyComplete();
    }


    @Test
    public void push() throws InterruptedException {
        // Asynchronous but single-threaded
        Flux<Object> flux = Flux.push(emitter -> IntStream.range(1, 10)
                        .forEach(emitter::next))
                .delayElements(Duration.ofMillis(1))
                .log();
        Disposable d = flux.subscribe();

        Thread.sleep(1000L);
        d.dispose();
    }

    @Test
    public void create() throws InterruptedException {
        // Asynchronous and Multi-threaded
        Logger logger = Logs.getLogger("create");
        ExecutorService es = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(10);
        Flux<Object> flux = Flux.create(emitter -> {
            emitter.onDispose(() -> logger.info("Disposed"));
            IntStream.range(1, 11)
                    .forEach(v -> {
                        es.submit(new Runnable() {
                            @Override
                            public void run() {
                                emitter.next(v + ThreadLocalRandom.current().nextInt(10));
                                latch.countDown();
                            }
                        });
                    });

            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            emitter.complete();
        });

        Disposable d = flux.log().subscribe();

        Thread.sleep(1000L);
        d.dispose();
    }

    @Test
    public void generate() throws InterruptedException {
        // Synchronous
        // with internal state
        // example: fibonacci numbers
        Flux<Object> flux = Flux.generate(() -> Tuples.of(0L, 1L),
                        (state, sink) -> {
                            sink.next(state.getT2());
                            return Tuples.of(state.getT2(), state.getT1() + state.getT2());
                        })
                .delayElements(Duration.ofMillis(1))
                .take(7);

        Disposable d = flux.log().subscribe();
        Thread.sleep(1000L);
        d.dispose();
    }

    @Test
    public void using() {
        Logger logger = Logs.getLogger("using");

        // create a stream depend on a disposable resource
        logger.info("case 1: success");
        Flux<String> flux = Flux.using(
                Connection::newConnection,
                connection -> Flux.fromIterable(connection.getData()),
                connection -> {
                    try {
                        connection.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        flux.log().subscribe();

        logger.info("case 2: fail");
        Connection.FAIL = true;
        flux = Flux.using(
                Connection::newConnection,
                connection -> Flux.fromIterable(connection.getData()),
                connection -> {
                    try {
                        connection.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        flux.log().subscribe();
    }

    public static class Connection implements AutoCloseable {
        public static boolean FAIL = false; // for test
        private static final Logger LOG = Logs.getLogger(Connection.class);

        private Connection() {
        }

        public static Connection newConnection() {
            LOG.info("Connection created.");
            return new Connection();
        }

        public Iterable<String> getData() {
            if (FAIL) {
                throw new RuntimeException("Communication error.");
            }
            return List.of("Some", "data");
        }

        @Override
        public void close() throws Exception {
            LOG.info("Connection closed.");
        }
    }

    @Test
    public void usingWhen() throws InterruptedException {
        // retrieve the managed resource reactively

        // ???
//Publisher<D> resourceSupplier,
//			Function<? super D, ? extends Publisher<? extends T>> resourceClosure,
//			Function<? super D, ? extends Publisher<?>> asyncComplete,
//			BiFunction<? super D, ? super Throwable, ? extends Publisher<?>> asyncError,
//			//the operator itself accepts null for asyncCancel, but we won't in the public API
//			Function<? super D, ? extends Publisher<?>> asyncCancel
        Flux<Object> flux = Flux.usingWhen(
                // resourceSupplier
                Transaction.begin(),
                // resourceClosure
                transaction -> transaction.insertRows(Flux.just("A", "B")),
                // asyncComplete
                Transaction::commit,
                // asyncError
                (transaction, throwable) -> transaction.rollback(),
                // asyncCancel
                Transaction::close);


        flux.log().subscribe();


        Thread.sleep(1000L);
    }

    public static class Transaction {
        private static final Logger LOG = Logs.getLogger(Transaction.class);

        public static boolean FAIL_INSERT = false; // for test
        public static boolean FAIL_COMMIT = false; // for test
        public static boolean FAIL_ROLLBACK = false; // for test
        private static final AtomicLong currentId = new AtomicLong(0L);
        private final long id;

        public Transaction(long id) {
            this.id = id;
            LOG.info("T {} created", id);
        }

        public static Mono<Transaction> begin() {
            LOG.info("Begin transaction");
            return Mono.defer(() -> Mono.just(new Transaction(currentId.incrementAndGet())));
        }

        public Mono<Void> close() {
            LOG.info("Close transaction");
            return Mono.defer(Mono::empty);
        }

        public Flux<String> insertRows(Publisher<String> rows) {
            return Flux.from(rows)
                    .delayElements(Duration.ofMillis(100))
                    .flatMap(r -> {
                        if (FAIL_INSERT) {
                            return Mono.error(new RuntimeException("Error: " + r));
                        } else {
                            return Mono.just(r);
                        }
                    });
        }

        public Mono<Void> commit() {
            return Mono.defer(() -> {
                LOG.info("T {} commit.", id);
                if (FAIL_COMMIT) {
                    return Mono.error(new RuntimeException("Commit failed"));
                } else {
                    return Mono.empty();
                }
            });
        }

        public Mono<Void> rollback() {
            return Mono.defer(() -> {
                LOG.info("T {} rollback", id);
                if (FAIL_ROLLBACK) {
                    return Mono.error(new RuntimeException("Rollback failed"));
                } else {
                    return Mono.empty();
                }
            });
        }
    }
}
