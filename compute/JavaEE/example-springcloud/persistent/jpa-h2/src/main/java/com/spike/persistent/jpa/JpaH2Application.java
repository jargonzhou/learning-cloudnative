package com.spike.persistent.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories // 开启JPA仓库
@EnableTransactionManagement // 开启事务管理
@SpringBootApplication
public class JpaH2Application {
    public static void main(String[] args) {
        SpringApplication.run(JpaH2Application.class, args);
    }
}
