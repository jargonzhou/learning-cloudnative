package com.spike.springcloud.framework.domain.woker;

import java.util.Map;

/**
 * Worker API.
 */
public interface IWorker {
    String CONFIG_PREFIX = "spike.workers";
    String BEAN_NAME_PREFIX = "worker.";

    /**
     * Set worker's name.
     *
     * @param name work name.
     */
    void setName(String name);

    String getName();

    /**
     * Initialize worker parameters.
     *
     * @param params worker parameter.
     */
    void init(Map<String, Object> params);

    /**
     * Do the work.
     *
     * @return work result.
     */
    Object work();
}
