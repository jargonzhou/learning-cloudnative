package com.spike.springframework.flux.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@Configuration
@EnableR2dbcAuditing // 开启R2DBC审计
public class DataConfig {
}
