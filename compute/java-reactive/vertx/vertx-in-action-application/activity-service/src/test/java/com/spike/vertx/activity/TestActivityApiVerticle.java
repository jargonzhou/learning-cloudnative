package com.spike.vertx.activity;

import com.google.common.collect.Lists;
import com.spike.vertx.commons.Constants;
import io.reactivex.rxjava3.core.Completable;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.pgclient.PgBuilder;
import io.vertx.rxjava3.sqlclient.SqlClient;
import io.vertx.rxjava3.sqlclient.Tuple;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@DisplayName("ActivityApiVerticle Tests")
@ExtendWith(VertxExtension.class)
public class TestActivityApiVerticle {
    private static RequestSpecification requestSpecification;

    @BeforeAll
    public static void setUpBeforeAll() {
        requestSpecification = new RequestSpecBuilder()
                .addFilters(Arrays.asList(new ResponseLoggingFilter(), new RequestLoggingFilter()))
                .setBaseUri("http://localhost:" + Constants.ActivityService.HTTP_PORT)
                .build();
    }

    @BeforeEach
    public void setUp(Vertx vertx, VertxTestContext testContext) {
        String insertQuery = "INSERT INTO stepevent VALUES($1, $2, $3::timestamp, $4)";
        LocalDateTime now = LocalDateTime.now();
        List<Tuple> data = Lists.newArrayList(
                Tuple.of("123", 1, LocalDateTime.of(2019, 4, 1, 23, 0), 6541),
                Tuple.of("123", 2, LocalDateTime.of(2019, 5, 20, 10, 0), 200),
                Tuple.of("123", 3, LocalDateTime.of(2019, 5, 21, 10, 10), 100),
                Tuple.of("456", 1, LocalDateTime.of(2019, 5, 21, 10, 15), 123),
                Tuple.of("123", 4, LocalDateTime.of(2019, 5, 21, 11, 0), 320),
                Tuple.of("abc", 1, now.minusHours(1), 1000),
                Tuple.of("def", 1, now.minusHours(2), 100),
                Tuple.of("def", 2, now.minusMinutes(30), 900),
                Tuple.of("abc", 2, now, 1500)
        );
        SqlClient sqlClient = PgBuilder.client()
                .using(vertx)
                .connectingTo(PgConfig.options())
                .with(PgConfig.poolOptions())
                .build();
        sqlClient.preparedQuery("DELETE FROM stepevent")
                .rxExecute()
                .flatMap(rows -> sqlClient.preparedQuery(insertQuery).rxExecuteBatch(data))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new ActivityApiVerticle())) // deploy verticle
                .ignoreElement()
                .andThen(Completable.fromAction(sqlClient::close))
                .subscribe(
                        testContext::completeNow,
                        testContext::failNow);
    }

    @DisplayName("Test device stats API")
    @Test
    public void testDeviceStats(Vertx vertx, VertxTestContext testContext) {
        JsonPath jsonPath = RestAssured.given()
                .spec(requestSpecification)
                .accept(ContentType.JSON)
                .get(String.format(Constants.ActivityService.HTTPApi.DEVICE_TOTAL, "456"))
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        Assertions.assertThat(jsonPath.getInt("count")).isEqualTo(123);

        jsonPath = RestAssured.given()
                .spec(requestSpecification)
                .accept(ContentType.JSON)
                .get(String.format(Constants.ActivityService.HTTPApi.DEVICE_TOTAL, "123"))
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        Assertions.assertThat(jsonPath.getInt("count")).isEqualTo(7161);

        jsonPath = RestAssured.given()
                .spec(requestSpecification)
                .accept(ContentType.JSON)
                .get(String.format(Constants.ActivityService.HTTPApi.DEVICE_MONTHLY, "123", "2019", "04"))
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        Assertions.assertThat(jsonPath.getInt("count")).isEqualTo(6541);

        jsonPath = RestAssured.given()
                .spec(requestSpecification)
                .accept(ContentType.JSON)
                .get(String.format(Constants.ActivityService.HTTPApi.DEVICE_MONTHLY, "123", "2019", "05"))
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        Assertions.assertThat(jsonPath.getInt("count")).isEqualTo(620);

        jsonPath = RestAssured.given()
                .spec(requestSpecification)
                .accept(ContentType.JSON)
                .get(String.format(Constants.ActivityService.HTTPApi.DEVICE_DAILY, "123", "2019", "05", "20"))
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        Assertions.assertThat(jsonPath.getInt("count")).isEqualTo(200);

        testContext.completeNow();
    }

    @Test
    public void testRanking(Vertx vertx, VertxTestContext testContext) {
        JsonPath jsonPath = RestAssured.given()
                .spec(requestSpecification)
                .accept(ContentType.JSON)
                .get(Constants.ActivityService.HTTPApi.RANKING)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();
        List<HashMap<String, Object>> data = jsonPath.getList("$");
        Assertions.assertThat(data.size()).isEqualTo(2);
        Assertions.assertThat(data.get(0))
                .containsEntry("deviceId", "abc")
                .containsEntry("stepsCount", 2500);
        Assertions.assertThat(data.get(1))
                .containsEntry("deviceId", "def")
                .containsEntry("stepsCount", 1000);

        testContext.completeNow();
    }
}
