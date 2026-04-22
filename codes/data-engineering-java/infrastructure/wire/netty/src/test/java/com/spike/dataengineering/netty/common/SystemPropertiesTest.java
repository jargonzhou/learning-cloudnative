package com.spike.dataengineering.netty.common;

import io.netty.util.internal.SystemPropertyUtil;
import org.junit.jupiter.api.Test;

public class SystemPropertiesTest {
    @Test
    public void systemProperties() {
        System.err.println(SystemPropertyUtil.get("io.netty.allocator.type"));

        System.setProperty("io.netty.allocator.type", "pooled");
        System.err.println(SystemPropertyUtil.get("io.netty.allocator.type"));
    }
}
