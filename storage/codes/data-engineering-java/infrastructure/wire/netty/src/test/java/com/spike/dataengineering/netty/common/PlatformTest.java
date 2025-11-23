package com.spike.dataengineering.netty.common;

import io.netty.util.internal.PlatformDependent;
import org.junit.jupiter.api.Test;

public class PlatformTest {
    @Test
    public void platform() {
        System.err.println("ANDROID: "+ PlatformDependent.isAndroid());
        System.err.println("WINDOWS: "+ PlatformDependent.isWindows());
        System.err.println("OSX: "+ PlatformDependent.isOsx());

        boolean hasUnsafe = PlatformDependent.hasUnsafe();
        System.err.println("hasUnsafe: " + hasUnsafe);
        if (!hasUnsafe) {
            PlatformDependent.getUnsafeUnavailabilityCause().printStackTrace();
        }

        // class loaders
    }
}
