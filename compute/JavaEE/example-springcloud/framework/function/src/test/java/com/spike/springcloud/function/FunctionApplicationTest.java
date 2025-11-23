package com.spike.springcloud.function;

import com.spike.springcloud.function.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalSpringBootTest // 函数测试
public class FunctionApplicationTest {
    @Autowired
    private FunctionCatalog catalog; // 函数目录

    @Autowired
    private QuoteService quoteService;

    //
    // dispatching
    //
    @Test
    public void pack() {
        final Long orderId = 1L;
        final OrderAcceptedMessage message = new OrderAcceptedMessage(orderId);

        Function<OrderAcceptedMessage, Long> function =
                catalog.lookup(Function.class, "pack");

        Assertions.assertThat(function.apply(message)).isEqualTo(orderId);
    }

    @Test
    public void label() {
        final Long orderId = 1L;
        Function<Flux<Long>, Flux<OrderDispatchedMessage>> function =
                catalog.lookup(Function.class, "label");

        function.apply(Flux.just(orderId))
                .subscribe(msg ->
                        Assertions.assertThat(msg.getOrderId()).isEqualTo(orderId));
    }

    @Test
    public void packAndLabel() {
        final Long orderId = 1L;
        final OrderAcceptedMessage message = new OrderAcceptedMessage(orderId);
        Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>> function =
                catalog.lookup(Function.class, "pack|label"); // pipeline

        StepVerifier.create(function.apply(message))
                .expectNextMatches(orderDispatchedMessage ->
                        orderId.equals(orderDispatchedMessage.getOrderId()))
                .verifyComplete();
    }

    //
    // quote
    //

    @Test
    public void allQuotes() {
        Supplier<Flux<Quote>> function = catalog.lookup(Supplier.class, "allQuotes");

        StepVerifier.create(function.get(), QuoteService.quotes.size())
                .expectNextMatches(quote -> {
                    System.err.println(quote);
                    return QuoteService.quotes.get(0).author().equals(quote.author());
                })

                .thenRequest(QuoteService.quotes.size() - 1)
                .thenConsumeWhile(Objects::nonNull, System.err::println)
                .verifyComplete();
    }

    @Test
    public void randomQuote() {
        Supplier<Mono<Quote>> function = catalog.lookup(Supplier.class, "randomQuote");

        StepVerifier.create(function.get())
                .expectNextMatches(quote -> {
                    System.err.println(quote);
                    return QuoteService.quotes.contains(quote);
                })
                .verifyComplete();
    }

    @Test
    public void genreQuote() {
        Function<Mono<Genre>, Mono<Quote>> function = catalog.lookup(Function.class, "genreQuote");

        StepVerifier.create(function.apply(Mono.just(Genre.FANTASY)))
                .expectNextMatches(quote -> {
                    System.err.println(quote);
                    return QuoteService.quotes.contains(quote);
                })
                .verifyComplete();
    }

    @Test // NO NEED TO TEST.
    public void logQuote() {
        Consumer<Quote> function = catalog.lookup(Consumer.class, "logQuote");
        function.accept(quoteService.getRandomQuote().block());
    }
}
