package com.spike.reactor.support;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;


public class LogsTest {
    @Test
    public void test() {
        Logger logger = Logs.getLogger("test");
        logger.info("Hello");
    }
}
