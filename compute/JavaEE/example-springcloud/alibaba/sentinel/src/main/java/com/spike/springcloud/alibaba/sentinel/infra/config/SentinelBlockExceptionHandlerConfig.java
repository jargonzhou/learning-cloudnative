package com.spike.springcloud.alibaba.sentinel.infra.config;

import com.alibaba.csp.sentinel.slots.block.AbstractRule;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order(0)
public class SentinelBlockExceptionHandlerConfig {
    private static final Logger LOG = LoggerFactory.getLogger(SentinelBlockExceptionHandlerConfig.class);

    @ExceptionHandler
    @ResponseBody
    public String blockHandler(BlockException e) { // BlockException
        AbstractRule rule = e.getRule();
        LOG.info("Blocked by Sentinel: {}", rule);
        return "Blocked by Sentinel";
    }
}
