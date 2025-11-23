package com.spike.reactor.olddays.juc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <pre>
 * {@link java.util.concurrent.CompletableFuture}
 * {@link java.util.concurrent.CompletionStage}
 *
 * {@code public class CompletableFuture<T> implements Future<T>, CompletionStage<T> {}}
 * </pre>
 */
public class CompletableFutureTest {

    /**
     * continuation style programming on futures
     * <pre>
     * @see java.util.concurrent.CompletionStage#thenApply(Function)
     *
     * @see java.util.concurrent.CompletionStage#thenAccept(Consumer)
     * @see java.util.concurrent.CompletionStage#thenAcceptBoth(CompletionStage, BiConsumer) // both
     * @see java.util.concurrent.CompletionStage#acceptEither(CompletionStage, Consumer) // either
     * @see java.util.concurrent.CompletionStage#thenAcceptAsync(Consumer) // async
     *
     * @see java.util.concurrent.CompletionStage#thenRun(Runnable)
     *
     * @see java.util.concurrent.CompletionStage#thenCombine(CompletionStage, BiFunction)
     *
     * @see java.util.concurrent.CompletionStage#whenComplete(BiConsumer)
     * @see java.util.concurrent.CompletionStage#handle(BiFunction)
     * @see java.util.concurrent.CompletionStage#exceptionally(Function)
     * </pre>
     */
    @Test
    public void testCompletionStage() {
        testCompletionStage_accept();
        testCompletionStage_run();
        testCompletionStage_compose();
        testCompletionStage_combine();
        testCompletionStage_handle();
        testCompletionStage_exceptionally();
    }

    private void testCompletionStage_accept() {
        final CopyOnWriteArrayList<String> collector = new CopyOnWriteArrayList<>();
        CompletableFuture<String> cfa = CompletableFuture.supplyAsync(() -> "A");
        cfa.thenAccept(collector::add).join();

        Assertions.assertThat(collector).element(collector.size() - 1).isEqualTo("A");
    }

    private void testCompletionStage_run() {
        final CopyOnWriteArrayList<String> collector = new CopyOnWriteArrayList<>();
        CompletableFuture<String> cfa = CompletableFuture.supplyAsync(() -> {
            collector.add("A");
            return "A";
        });
        CompletableFuture<String> cfb = CompletableFuture.supplyAsync(() -> {
            collector.add("B");
            return "B";
        });

        cfa.runAfterBoth(cfb, () -> {
                    collector.add("C");
                })
                .join();
        Assertions.assertThat(collector).element(collector.size() - 1).isEqualTo("C");
    }


    private void testCompletionStage_compose() {
        CompletableFuture<String> cfa = CompletableFuture.supplyAsync(() -> "A");
        String result = cfa.thenCompose(s -> CompletableFuture.supplyAsync(() -> s + "B"))
                .join();
        Assertions.assertThat(result).isEqualTo("AB");
    }

    private void testCompletionStage_combine() {
        CompletableFuture<String> cfa = CompletableFuture.supplyAsync(() -> "A");
        CompletableFuture<String> cfb = CompletableFuture.supplyAsync(() -> "B");
        String result = cfa
                .thenCombine(cfb, (s1, s2) -> s1 + s2)
                .whenComplete((s, e) -> {
                    if (Objects.nonNull(e)) {
                        System.err.println(e);
                    }
                }).join();
        Assertions.assertThat(result).isEqualTo("AB");
    }

    private void testCompletionStage_handle() {
        final boolean flag = true;
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (flag) {
                throw new RuntimeException("flag");
            }
            return "RESULT";
        }).handle((s, e) -> {
            if (Objects.nonNull(e)) {// java.util.concurrent.CompletionException
                System.err.println(e);
                return "<RESULT>";
            }
            return s;
        }).join();
        Assertions.assertThat(result).isEqualTo("<RESULT>");
    }

    private void testCompletionStage_exceptionally() {
        final boolean flag = true;
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (flag) {
                throw new RuntimeException("flag");
            }
            return "RESULT";
        }).exceptionally(e -> {
            System.err.println(e.getMessage());
            return "<RESULT>";
        }).join();
        Assertions.assertThat(result).isEqualTo("<RESULT>");
    }

    @Test
    public void testCompletableFuture() throws ExecutionException, InterruptedException {
        final String result = "A";
        CompletableFuture<String> cf = new CompletableFuture<>();

        cf.complete(result);

        Assertions.assertThat(cf.get()).isEqualTo(result);
    }
}
