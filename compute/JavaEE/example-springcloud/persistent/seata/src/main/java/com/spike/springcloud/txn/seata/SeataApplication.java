package com.spike.springcloud.txn.seata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// example: https://github.com/apache/incubator-seata-samples
// ops: https://github.com/jargonzhou/application-store/tree/main/ops/seata
@SpringBootApplication
public class SeataApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeataApplication.class, args);
    }
}
