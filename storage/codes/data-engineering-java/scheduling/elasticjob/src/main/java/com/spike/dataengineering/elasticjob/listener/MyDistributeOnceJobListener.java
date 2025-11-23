package com.spike.dataengineering.elasticjob.listener;

import org.apache.shardingsphere.elasticjob.infra.listener.ShardingContexts;
import org.apache.shardingsphere.elasticjob.lite.api.listener.AbstractDistributeOnceElasticJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 在一个作业服务器节点执行
public class MyDistributeOnceJobListener extends AbstractDistributeOnceElasticJobListener {
    private static final Logger LOG = LoggerFactory.getLogger(MyDistributeOnceJobListener.class);

    public static final String TYPE = "distributedOnceJobListener";

    public static long startTimeoutMills = 3000;
    public static long completeTimeoutMills = 3000;

    public MyDistributeOnceJobListener() {
        super(startTimeoutMills, completeTimeoutMills);
    }

    public MyDistributeOnceJobListener(long startedTimeoutMilliseconds, long completedTimeoutMilliseconds) {
        super(startedTimeoutMilliseconds, completedTimeoutMilliseconds);
    }

    @Override
    public void doBeforeJobExecutedAtLastStarted(ShardingContexts contexts) {
        LOG.info("doBeforeJobExecutedAtLastStarted: {}", contexts);
    }

    @Override
    public void doAfterJobExecutedAtLastCompleted(ShardingContexts contexts) {
        LOG.info("doAfterJobExecutedAtLastCompleted: {}", contexts);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
