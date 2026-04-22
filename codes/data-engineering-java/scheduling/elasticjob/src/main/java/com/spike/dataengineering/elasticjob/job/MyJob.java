package com.spike.dataengineering.elasticjob.job;

import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyJob implements SimpleJob {
    private static final Logger LOG = LoggerFactory.getLogger(MyJob.class);

    @Override
    public void execute(ShardingContext context) {
        LOG.info("context: {}", context);

    }
}
