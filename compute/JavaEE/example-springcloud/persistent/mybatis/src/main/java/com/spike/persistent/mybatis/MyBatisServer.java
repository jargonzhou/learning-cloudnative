package com.spike.persistent.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.spike.cloud.persistent.mybatis.dao.mapper")
public class MyBatisServer {
    public static void main(String[] args) {
        SpringApplication.run(MyBatisServer.class, args);
    }
}
