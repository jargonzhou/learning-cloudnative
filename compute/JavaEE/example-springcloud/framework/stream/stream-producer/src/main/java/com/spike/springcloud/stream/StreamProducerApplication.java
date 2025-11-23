package com.spike.springcloud.stream;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// example from 'Cloud Native Spring in Action': order-service
// ops: https://github.com/jargonzhou/application-store/tree/main/data-engineering/rabbitmq
@SpringBootApplication
@EnableTransactionManagement // 开启事务管理
public class StreamProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(StreamProducerApplication.class, args);
    }


    // 配置RabbitMQ事务管理器
    // or 使用已有事务管理器
    @Bean
    public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }
}
