package com.spike.reactor;

import com.spike.reactor.support.Logs;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * <pre>
 * @see Flux#onBackpressureBuffer()
 * @see Flux#onBackpressureDrop()
 * @see Flux#onBackpressureLatest()
 * @see Flux#onBackpressureError()
 *
 * @see Flux#limitRate(int)
 * @see Flux#limitRequest(long)
 * </pre>
 */
public class ReactorBackpressureTest {
    @Test
    public void backpressure() throws InterruptedException {
        Logger logger = Logs.getLogger("backpressure");
        Flux.range(1, 10)
                .delayElements(Duration.ofMillis(10)) // parallel scheduler
                .onBackpressureBuffer(2, v -> logger.info("Overflow: {}", v)) // small buffer
                .delayElements(Duration.ofMillis(100)) // delay
                .subscribe(v -> logger.info("{}", v),
                        e -> logger.error(e.getMessage(), e),
                        () -> logger.info("complete"));

        // expect error:
        // reactor.core.Exceptions$OverflowException:
        // The receiver is overrun by more signals than expected (bounded queue...)

        Thread.sleep(2000L);
    }
}
