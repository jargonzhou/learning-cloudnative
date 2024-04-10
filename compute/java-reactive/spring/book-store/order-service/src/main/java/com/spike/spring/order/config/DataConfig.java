package com.spike.spring.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@Configuration
@EnableR2dbcAuditing // enable R2DBC auditing for persistent entities
public class DataConfig {
}
