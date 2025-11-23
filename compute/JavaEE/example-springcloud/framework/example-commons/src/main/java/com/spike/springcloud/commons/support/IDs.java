package com.spike.springcloud.commons.support;

import com.google.common.base.Preconditions;

import java.util.Objects;
import java.util.UUID;

public final class IDs {

    private IDs() {
    }

    public static String next() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String next(String serviceId) {
        Preconditions.checkArgument(Objects.nonNull(serviceId) && !serviceId.isEmpty(),
                "invalid serviceId");
        return serviceId + "." + next();
    }

}
