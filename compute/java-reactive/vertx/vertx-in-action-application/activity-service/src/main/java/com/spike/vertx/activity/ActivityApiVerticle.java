package com.spike.vertx.activity;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.pgclient.PgBuilder;
import io.vertx.rxjava3.sqlclient.Row;
import io.vertx.rxjava3.sqlclient.RowSet;
import io.vertx.rxjava3.sqlclient.SqlClient;
import io.vertx.rxjava3.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDateTime;

import static com.spike.vertx.commons.Constants.ActivityService;


public class ActivityApiVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(ActivityApiVerticle.class);

    // DEPRECATED: use SqlClient
    // private PgPool pgPool;
    private SqlClient sqlClient;

    @Override
    public Completable rxStart() {
        this.sqlClient = PgBuilder.client()
                .using(vertx)
                .connectingTo(PgConfig.options())
                .with(PgConfig.poolOptions())
                .build();

        Router router = Router.router(vertx);
        router.get(ActivityService.HTTPApi.DEVICE_TOTAL_PATH).handler(this::totalSteps);
        router.get(ActivityService.HTTPApi.DEVICE_MONTHLY_PATH).handler(this::stepsOnMonth);
        router.get(ActivityService.HTTPApi.DEVICE_DAILY_PATH).handler(this::stepsOnDay);
        router.get(ActivityService.HTTPApi.RANKING).handler(this::ranking);

        return vertx.createHttpServer()
                .requestHandler(router)
                .rxListen(ActivityService.HTTP_PORT)
                .doOnSuccess(httpServer -> {
                    LOG.info("{} started on {}",
                            this.getClass().getSimpleName(), ActivityService.HTTP_PORT);
                })
                .doOnError(err -> {
                    LOG.error("{} failed to start on {}",
                            this.getClass().getSimpleName(), ActivityService.HTTP_PORT);
                })
                .ignoreElement();
    }

    private void totalSteps(RoutingContext rc) {
        String deviceId = rc.pathParam("deviceId");
        sqlClient.preparedQuery(PgSQLs.totalStepsCount())
                .rxExecute(Tuple.of(deviceId))
                .map(rs -> rs.iterator().next())
                .subscribe(
                        row -> this.sendCount(rc, row),
                        err -> this.handleError(rc, err));
    }

    private void sendCount(RoutingContext rc, Row row) {
        Integer count = row.getInteger(0);
        if (count != null) {
            JsonObject payload = new JsonObject()
                    .put("count", count);
            rc.response()
                    .putHeader("Content-Type", "application/json")
                    .end(payload.encode());
        } else {
            this.send404(rc);
        }
    }

    private void stepsOnMonth(RoutingContext rc) {
        String deviceId = rc.pathParam("deviceId");
        String year = rc.pathParam("year");
        String month = rc.pathParam("month");

        try {
            LocalDateTime dateTime = LocalDateTime.of(
                    Integer.parseInt(year),
                    Integer.parseInt(month),
                    1, 0, 0);

            sqlClient.preparedQuery(PgSQLs.monthlyStepsCount())
                    .rxExecute(Tuple.of(deviceId, dateTime))
                    .map(rs -> rs.iterator().next())
                    .subscribe(
                            row -> this.sendCount(rc, row),
                            err -> this.handleError(rc, err));
        } catch (DateTimeException | NumberFormatException e) {
            this.sendBadRequest(rc);
        }
    }

    private void stepsOnDay(RoutingContext rc) {
        String deviceId = rc.pathParam("deviceId");
        String year = rc.pathParam("year");
        String month = rc.pathParam("month");
        String day = rc.pathParam("day");

        try {
            LocalDateTime dateTime = LocalDateTime.of(
                    Integer.parseInt(year),
                    Integer.parseInt(month),
                    Integer.parseInt(day), 0, 0);

            sqlClient.preparedQuery(PgSQLs.dailyStepsCount())
                    .rxExecute(Tuple.of(deviceId, dateTime))
                    .map(rs -> rs.iterator().next())
                    .subscribe(
                            row -> this.sendCount(rc, row),
                            err -> this.handleError(rc, err));
        } catch (DateTimeException | NumberFormatException e) {
            this.sendBadRequest(rc);
        }
    }

    private void ranking(RoutingContext rc) {
        sqlClient.preparedQuery(PgSQLs.rankingLast24Hours())
                .rxExecute()
                .subscribe(
                        rs -> sendRanking(rc, rs),
                        err -> handleError(rc, err));
    }

    private void sendRanking(RoutingContext rc, RowSet<Row> rs) {
        JsonArray data = new JsonArray();
        rs.forEach(row -> {
            String deviceId = row.getString(0);
            Integer count = row.getInteger(1);
            data.add(new JsonObject()
                    .put("deviceId", deviceId)
                    .put("stepsCount", count));
        });
        rc.response()
                .putHeader("Content-Type", "application/json")
                .end(data.encode());
    }

    // helpers

    private void handleError(RoutingContext rc, Throwable err) {
        LOG.error("ERROR", err);
        rc.response().setStatusCode(500).end();
    }

    private void send404(RoutingContext rc) {
        rc.response().setStatusCode(404).end();
    }

    private void sendBadRequest(RoutingContext rc) {
        rc.response().setStatusCode(400).end();
    }

    @Override
    public Completable rxStop() {
        if (sqlClient != null) {
            sqlClient.rxClose()
                    .toObservable()
                    .subscribe(v -> LOG.info("Pg connection pool closed."),
                            err -> LOG.error("Pg connection pool close failed", err));
        }
        return Completable.complete();
    }
}
