package com.spike.vertx.eventstats.publicapi;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.reactivex.rxjava3.core.Completable;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.mongo.MongoClient;
import io.vertx.rxjava3.pgclient.PgBuilder;
import io.vertx.rxjava3.sqlclient.SqlClient;
import io.vertx.rxjava3.sqlclient.Tuple;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.spike.vertx.commons.Constants.PublicApi;
import static com.spike.vertx.commons.Constants.UserProfileService;

@ExtendWith(VertxExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // support tests order
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("PublicApiVerticle Tests")
public class TestPublicApiVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(TestPublicApiVerticle.class);

    private static final Map<String, JsonObject> registrations = Maps.newHashMap();
    private static RequestSpecification requestSpecification;

    @BeforeAll
    public static void setUpBeforeAll(Vertx vertx, VertxTestContext testContext) {
        registrations.put("Foo", new JsonObject()
                .put("username", "Foo")
                .put("password", "foo-123")
                .put("email", "foo@email.me")
                .put("city", "Lyon")
                .put("deviceId", "a1b2c3")
                .put("makePublic", true));

        registrations.put("Bar", new JsonObject()
                .put("username", "Bar")
                .put("password", "bar-#$69")
                .put("email", "bar@email.me")
                .put("city", "Tassin-La-Demi-Lune")
                .put("deviceId", "def1234")
                .put("makePublic", false));

        requestSpecification = new RequestSpecBuilder()
                .addFilters(Lists.newArrayList(new ResponseLoggingFilter(), new RequestLoggingFilter()))
                .setBaseUri("http://localhost:" + PublicApi.HTTP_PORT)
                .setBasePath("/api/v1")
                .build();

        JsonObject mongoConfig = new JsonObject()
                .put("host", UserProfileService.MONGODB_HOST)
                .put("port", UserProfileService.MONGODB_PORT)
                .put("db_name", UserProfileService.MONGODB_DB_NAME)
                .put("username", UserProfileService.MONGODB_USERNAME)
                .put("password", UserProfileService.MONGODB_PASSWORD);
        MongoClient mongoClient = MongoClient.createShared(vertx, mongoConfig);
        SqlClient sqlClient = PgBuilder.client()
                .using(vertx)
                .connectingTo(com.spike.vertx.activity.PgConfig.options())
                .with(com.spike.vertx.activity.PgConfig.poolOptions())
                .build();

        String insertQuery = "INSERT INTO stepevent VALUES($1, $2, $3::timestamp, $4)";
        List<Tuple> data = Lists.newArrayList(
                Tuple.of("a1b2c3", 1, LocalDateTime.of(2019, 6, 16, 10, 3), 250),
                Tuple.of("a1b2c3", 2, LocalDateTime.of(2019, 6, 16, 12, 30), 1000),
                Tuple.of("a1b2c3", 3, LocalDateTime.of(2019, 6, 15, 23, 0), 5005)
        );

        mongoClient.rxRemoveDocuments(UserProfileService.MONGODB_COL_NAME, new JsonObject())
                .ignoreElement()
                .andThen(sqlClient.preparedQuery("DELETE FROM stepevent").rxExecute())
                .ignoreElement()
                .andThen(sqlClient.preparedQuery(insertQuery).rxExecuteBatch(data))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new PublicApiVerticle())) // deploy verticle
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new com.spike.vertx.eventstats.userprofile.UserProfileApiVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new com.spike.vertx.activity.ActivityApiVerticle()))
                .ignoreElement()
                .andThen(Completable.fromAction(sqlClient::close))
                .subscribe(
                        testContext::completeNow,
                        testContext::failNow);
    }

    @Test
    @Order(1)
    @DisplayName("Register some users")
    void registerUsers() {
        registrations.forEach((key, registration) -> {
            RestAssured.given(requestSpecification)
                    .contentType(ContentType.JSON)
                    .body(registration.encode())
                    .post("/register")
                    .then()
                    .assertThat()
                    .statusCode(200);
        });
    }

    private final HashMap<String, String> tokens = new HashMap<>();

    @Test
    @Order(2)
    @DisplayName("Get JWT tokens to access the API")
    void obtainToken() {
        registrations.forEach((key, registration) -> {

            JsonObject login = new JsonObject()
                    .put("username", key)
                    .put("password", registration.getString("password"));

            String token = RestAssured.given(requestSpecification)
                    .contentType(ContentType.JSON)
                    .body(login.encode())
                    .post("/token")
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .contentType("application/jwt")
                    .extract()
                    .asString();

            Assertions.assertThat(token)
                    .isNotNull()
                    .isNotBlank();

            tokens.put(key, token);
        });
    }

    @Test
    @Order(3)
    @DisplayName("Fetch a user data")
    void fetchSomeUser() {
        JsonPath jsonPath = RestAssured.given(requestSpecification)
                .headers("Authorization", "Bearer " + tokens.get("Foo")) // with token
                .get("/Foo")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        JsonObject foo = registrations.get("Foo");
        List<String> props = Lists.newArrayList("username", "email", "city", "deviceId");
        props.forEach(prop -> Assertions.assertThat(jsonPath.getString(prop)).isEqualTo(foo.getString(prop)));
        Assertions.assertThat(jsonPath.getBoolean("makePublic")).isEqualTo(foo.getBoolean("makePublic"));
    }

    @Test
    @Order(4)
    @DisplayName("Fail at fetching another user data")
    void failToFatchAnotherUser() {
        RestAssured.given(requestSpecification)
                .headers("Authorization", "Bearer " + tokens.get("Foo"))
                .get("/Bar")
                .then()
                .assertThat()
                .statusCode(403);
    }

    @Test
    @Order(5)
    @DisplayName("Update some user data")
    void updateSomeUser() {
        String originalCity = registrations.get("Foo").getString("city");
        boolean originalMakePublic = registrations.get("Foo").getBoolean("makePublic");
        JsonObject updates = new JsonObject()
                .put("city", "Nevers")
                .put("makePublic", false);

        RestAssured.given(requestSpecification)
                .headers("Authorization", "Bearer " + tokens.get("Foo"))
                .contentType(ContentType.JSON)
                .body(updates.encode())
                .put("/Foo")
                .then()
                .assertThat()
                .statusCode(200);

        JsonPath jsonPath = RestAssured.given(requestSpecification)
                .headers("Authorization", "Bearer " + tokens.get("Foo"))
                .get("/Foo")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        Assertions.assertThat(jsonPath.getString("city")).isEqualTo(updates.getString("city"));
        Assertions.assertThat(jsonPath.getBoolean("makePublic")).isEqualTo(updates.getBoolean("makePublic"));

        updates
                .put("city", originalCity)
                .put("makePublic", originalMakePublic);

        RestAssured.given(requestSpecification)
                .headers("Authorization", "Bearer " + tokens.get("Foo"))
                .contentType(ContentType.JSON)
                .body(updates.encode())
                .put("/Foo")
                .then()
                .assertThat()
                .statusCode(200);

        jsonPath = RestAssured.given(requestSpecification)
                .headers("Authorization", "Bearer " + tokens.get("Foo"))
                .get("/Foo")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        Assertions.assertThat(jsonPath.getString("city")).isEqualTo(originalCity);
        Assertions.assertThat(jsonPath.getBoolean("makePublic")).isEqualTo(originalMakePublic);
    }

    @Test
    @Order(6)
    @DisplayName("Check some user stats")
    void checkSomeUserStats() {
        JsonPath jsonPath = RestAssured.given(requestSpecification)
                .headers("Authorization", "Bearer " + tokens.get("Foo"))
                .get("/Foo/total")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        Assertions.assertThat(jsonPath.getInt("count")).isNotNull().isEqualTo(6255);

        jsonPath = RestAssured.given(requestSpecification)
                .headers("Authorization", "Bearer " + tokens.get("Foo"))
                .get("/Foo/2019/06")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        Assertions.assertThat(jsonPath.getInt("count")).isNotNull().isEqualTo(6255);

        jsonPath = RestAssured.given(requestSpecification)
                .headers("Authorization", "Bearer " + tokens.get("Foo"))
                .get("/Foo/2019/06/15")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        Assertions.assertThat(jsonPath.getInt("count")).isNotNull().isEqualTo(5005);
    }

    @Test
    @Order(7)
    @DisplayName("Check that you cannot access somebody else's stats")
    void cannotAccessSomebodyElseStats() {
        RestAssured.given(requestSpecification)
                .headers("Authorization", "Bearer " + tokens.get("Foo"))
                .get("/Bar/total")
                .then()
                .assertThat()
                .statusCode(403);
    }
}
