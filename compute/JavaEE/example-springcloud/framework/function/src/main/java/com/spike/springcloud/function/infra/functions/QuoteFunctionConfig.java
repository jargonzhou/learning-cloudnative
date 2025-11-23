package com.spike.springcloud.function.infra.functions;

import com.spike.springcloud.function.domain.Genre;
import com.spike.springcloud.function.domain.Quote;
import com.spike.springcloud.function.domain.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class QuoteFunctionConfig {
    private static final Logger LOG = LoggerFactory.getLogger("quote");

    // [] => Flux<Quote>
    @Bean
    public Supplier<Flux<Quote>> allQuotes(QuoteService quoteService) {
        return () -> {
            LOG.info("Getting all quotes");
            return quoteService.getAllQuotes()
                    .delaySequence(Duration.ofSeconds(1));
        };
    }

    // [] => Mono<Quote>
    @Bean
    public Supplier<Mono<Quote>> randomQuote(QuoteService quoteService) {
        return () -> {
            LOG.info("Getting random quote");
            return quoteService.getRandomQuote();
        };
    }

    // Mono<Genre> => Mono<Quote>
    @Bean
    public Function<Mono<Genre>, Mono<Quote>> genreQuote(QuoteService quoteService) {
        return mono -> mono.flatMap(genre -> {
            LOG.info("Getting quote for type {}", genre);
            return quoteService.getRandomQuoteByGenre(genre);
        });
    }

    // Mono<Quote> => []
    @Bean
    public Consumer<Mono<Quote>> logQuote() {
        return mono -> mono.subscribe(quote -> {
            LOG.info("Quote: '{}' by {}", quote.content(), quote.author());
        });
    }
}
