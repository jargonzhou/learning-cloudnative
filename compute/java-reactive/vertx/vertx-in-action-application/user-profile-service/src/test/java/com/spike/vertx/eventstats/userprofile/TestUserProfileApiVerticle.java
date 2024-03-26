package com.spike.vertx.eventstats.userprofile;

import io.reactivex.rxjava3.core.Maybe;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.IndexOptions;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.mongo.MongoClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.spike.vertx.commons.Constants.UserProfileService;

@ExtendWith(VertxExtension.class)
@DisplayName("UserProfileApiVerticle Tests")
public class TestUserProfileApiVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(TestUserProfileApiVerticle.class);

    private static RequestSpecification requestSpecification;
    private MongoClient mongoClient;

    @BeforeAll
    public static void setUpBeforeAll() {
        requestSpecification = new RequestSpecBuilder()
                .addFilters(Arrays.asList(new ResponseLoggingFilter(), new RequestLoggingFilter()))
                .setBaseUri("http://localhost:" + UserProfileService.HTTP_PORT)
                .build();
    }

    @BeforeEach
    public void setUp(Vertx vertx, VertxTestContext testContext) {
        // profiles> db.createUser({user: "root", pwd: "root", roles:[{role: "readWrite", db: "profiles"}]});
        JsonObject mongoConfig = new JsonObject()
                .put("host", UserProfileService.MONGODB_HOST)
                .put("port", UserProfileService.MONGODB_PORT)
                .put("db_name", UserProfileService.MONGODB_DB_NAME)
                .put("username", UserProfileService.MONGODB_USERNAME)
                .put("password", UserProfileService.MONGODB_PASSWORD);

        this.mongoClient = MongoClient.createShared(vertx, mongoConfig);


        JsonObject key1 = new JsonObject()
                .put("username", 1);
        JsonObject key2 = new JsonObject()
                .put("deviceId", 1);
        IndexOptions options = new IndexOptions()
                .unique(true);
        mongoClient.rxCreateIndexWithOptions(UserProfileService.MONGODB_COL_NAME, key1, options)
                .andThen(mongoClient.createIndexWithOptions(UserProfileService.MONGODB_COL_NAME, key2, options))
                .andThen(this.dropAllUser())
                .flatMapSingle(res -> vertx.rxDeployVerticle(new UserProfileApiVerticle())) // deploy verticle
                .subscribe(
                        ok -> testContext.completeNow(),
                        err -> {
                            LOG.error("ERROR", err);
                            testContext.failNow(err);
                        });
    }

    private Maybe<MongoClientDeleteResult> dropAllUser() {
        return mongoClient.rxRemoveDocuments(UserProfileService.MONGODB_COL_NAME, new JsonObject());
    }

    @AfterEach
    public void tearDown(Vertx vertx, VertxTestContext testContext) {
        this.dropAllUser()
                .doFinally(mongoClient::close)
                .subscribe(
                        ok -> testContext.completeNow(),
                        testContext::failNow);
    }

    @Test
    @DisplayName("Register a user")
    public void register() {
        String response = RestAssured.given()
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(this.basicUser().encode())
                .when()
                .post(UserProfileService.HTTPApi.REGISTER)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
        Assertions.assertThat(response).isEmpty();
    }

    private JsonObject basicUser() {
        return new JsonObject()
                .put("username", "abc")
                .put("password", "123")
                .put("email", "abc@example.com")
                .put("city", "Lyon")
                .put("deviceId", "a1b2c3")
                .put("makePublic", true);
    }

    // TODO(zhoujiagen): more tests
}
