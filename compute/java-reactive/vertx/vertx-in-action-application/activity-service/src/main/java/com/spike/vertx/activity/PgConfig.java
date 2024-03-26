package com.spike.vertx.activity;

import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.SslMode;
import io.vertx.sqlclient.PoolOptions;

import static com.spike.vertx.commons.Constants.ActivityService;

public class PgConfig {
    public static PgConnectOptions options() {
        return new PgConnectOptions()
                .setHost(ActivityService.POSTGRESQL_HOST)
                .setPort(ActivityService.POSTGRESQL_PORT)
                .setDatabase(ActivityService.POSTGRESQL_DB)
                .setUser(ActivityService.POSTGRESQL_USER)
                .setPassword(ActivityService.POSTGRESQL_PASSWORD)
                .setSslMode(SslMode.DISABLE); // com.ongres.scram:client:2.1
    }

    public static PoolOptions poolOptions() {
        return new PoolOptions()
                .setMaxSize(5);
    }
}