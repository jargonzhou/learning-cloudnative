package com.spike.dataengineering.elasticjob.listener;

import org.apache.shardingsphere.elasticjob.infra.listener.ElasticJobListener;
import org.apache.shardingsphere.elasticjob.infra.listener.ShardingContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 在每个作业服务器节点执行
public class MyJobListener implements ElasticJobListener {
    private static final Logger LOG = LoggerFactory.getLogger(MyJobListener.class);

    public static final String TYPE = "simpleJobListener";

    @Override
    public void beforeJobExecuted(ShardingContexts contexts) {
        LOG.info("beforeJobExecuted: {}", contexts);
    }

    @Override
    public void afterJobExecuted(ShardingContexts contexts) {
        LOG.info("afterJobExecuted: {}", contexts);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
