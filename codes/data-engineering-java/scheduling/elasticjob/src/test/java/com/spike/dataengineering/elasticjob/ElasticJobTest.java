package com.spike.dataengineering.elasticjob;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.spike.dataengineering.elasticjob.job.MyJob;
import com.spike.dataengineering.elasticjob.listener.MyDistributeOnceJobListener;
import com.spike.dataengineering.elasticjob.listener.MyJobListener;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.infra.pojo.JobConfigurationPOJO;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.OneOffJobBootstrap;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.lite.internal.snapshot.SnapshotService;
import org.apache.shardingsphere.elasticjob.lite.lifecycle.api.JobAPIFactory;
import org.apache.shardingsphere.elasticjob.lite.lifecycle.api.JobConfigurationAPI;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperConfiguration;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;
import org.apache.shardingsphere.elasticjob.tracing.api.TracingConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ElasticJobTest {

    // 一次性调度
    @Test
    public void oneOff() {
        CoordinatorRegistryCenter registryCenter = registryCenter();

        // 作业配置
        // more: https://shardingsphere.apache.org/elasticjob/current/cn/user-manual/configuration/java-api/
        String jobName = "myJob-oneOff";
        int shardingTotalCount = 2;
        JobConfiguration jobConfiguration = JobConfiguration.newBuilder(jobName, shardingTotalCount)
                .jobErrorHandlerType("LOG") // 错误处理策略
                // 注意: 修改后, 删除console中 '作业维度' 中的作业
                .jobListenerTypes(MyJobListener.TYPE)
                .jobListenerTypes(MyDistributeOnceJobListener.TYPE)
                .build();

        // 事件追踪配置
        // console: '作业历史'
        // 表: JOB_EXECUTION_LOG, JOB_STATUS_TRACE_LOG
        DataSource dataSource = dataSource();
        TracingConfiguration<DataSource> tracingConfiguration = new TracingConfiguration<>("RDB", dataSource);
        jobConfiguration.getExtraConfigurations().add(tracingConfiguration);

        OneOffJobBootstrap bootstrap = new OneOffJobBootstrap(registryCenter, new MyJob(), jobConfiguration);

        // call 3 times
        bootstrap.execute();
        bootstrap.execute();
        bootstrap.execute();

        // 配置作业导出
        // echo "dump@myJob-oneOff" | nc 192.168.3.179 9888
        new Thread(() -> {
            SnapshotService snapshotService = new SnapshotService(registryCenter(), 9888);
            snapshotService.listen();
        }).start();

        // delay
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 定时调度
    @Test
    public void schedule() {
        CoordinatorRegistryCenter registryCenter = registryCenter();
        MyJob myJob = new MyJob();
        String jobName = "myJob";
        int shardingTotalCount = 2;
        JobConfiguration jobConfiguration = JobConfiguration.newBuilder(jobName, shardingTotalCount)
                .cron("0/5 * * * * ?")
                .build();
        ScheduleJobBootstrap bootstrap = new ScheduleJobBootstrap(registryCenter, myJob, jobConfiguration);

        bootstrap.schedule();

        // delay
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 注册中心
    // more: https://shardingsphere.apache.org/elasticjob/current/cn/user-manual/configuration/java-api/
    private CoordinatorRegistryCenter registryCenter() {
        CoordinatorRegistryCenter registryCenter = new ZookeeperRegistryCenter(
                new ZookeeperConfiguration("localhost:2181", "my-job-ns"));
        registryCenter.init();
        return registryCenter;
    }

    private DataSource dataSource() {
        MysqlDataSource result = new MysqlDataSource();
        result.setUrl("jdbc:mysql://localhost:3306/elasticjob");
        result.setPort(3306);
        result.setDatabaseName("elasticjob");
        result.setUser("elasticjob");
        result.setPassword("elasticjob");
        return result;
    }

    @Test
    public void testDataSource() throws SQLException {
        DataSource dataSource = dataSource();
        try (Connection conn = dataSource.getConnection()) {
            Assertions.assertThat(conn).isNotNull();
            try (ResultSet rs = conn.createStatement().executeQuery("SHOW TABLES");) {
                ResultSetMetaData meta = rs.getMetaData();
                int colmax = meta.getColumnCount();
                int i;
                Object o = null;
                for (; rs.next(); ) {
                    for (i = 0; i < colmax; ++i) {
                        o = rs.getObject(i + 1);
                        System.out.print(o.toString() + " ");
                    }
                    System.out.println();
                }
            }
        }
    }

    @Test
    public void opsAPI() {
        JobConfigurationAPI api =
                JobAPIFactory.createJobConfigurationAPI("localhost:2181", "my-job-ns", null);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // 获取作业配置
        JobConfigurationPOJO job = api.getJobConfiguration("myJob-oneOff");
        Assertions.assertThat(job).isNotNull();
        System.err.println(gson.toJson(job));

        // more: https://shardingsphere.apache.org/elasticjob/current/cn/user-manual/usage/operation-api/
    }

}
