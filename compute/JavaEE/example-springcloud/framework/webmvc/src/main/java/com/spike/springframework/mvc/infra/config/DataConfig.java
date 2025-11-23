package com.spike.springframework.mvc.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@Configuration
//@EnableJdbcAuditing // 开启JDBC审计: @CreatedDate, @CreatedBy, @LastModifiedBy, @LastModifiedDate
public class DataConfig {
}
