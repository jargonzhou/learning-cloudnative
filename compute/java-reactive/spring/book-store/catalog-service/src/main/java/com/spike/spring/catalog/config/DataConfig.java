package com.spike.spring.catalog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@Configuration
@EnableJdbcAuditing // enable auditing for persistent entities
public class DataConfig {
}
