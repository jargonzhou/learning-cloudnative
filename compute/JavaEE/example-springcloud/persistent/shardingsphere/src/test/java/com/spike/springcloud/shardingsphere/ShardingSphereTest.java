package com.spike.springcloud.shardingsphere;

import com.spike.springcloud.shardingsphere.domain.service.ExampleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@SpringBootTest
public class ShardingSphereTest {
    @Autowired
    private ExampleService exampleService;

    @Test
    public void demo() {
        try {
            exampleService.initEnvironment();

            exampleService.run();
            // 分别运行.
//            List<Long> orderIds = exampleService.processInsertData();
//            exampleService.processDeleteData(orderIds);

            exampleService.cleanEnvironment();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // More:
    // single table
    // read write splitting
    // distributed transaction: XA, BASE
}
