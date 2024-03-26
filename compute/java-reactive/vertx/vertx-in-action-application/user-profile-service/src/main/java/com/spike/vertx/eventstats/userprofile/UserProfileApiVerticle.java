package com.spike.vertx.eventstats.userprofile;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;
import io.vertx.ext.auth.mongo.MongoAuthenticationOptions;
import io.vertx.ext.auth.mongo.MongoAuthorizationOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.auth.mongo.MongoAuthentication;
import io.vertx.rxjava3.ext.auth.mongo.MongoUserUtil;
import io.vertx.rxjava3.ext.mongo.MongoClient;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static com.spike.vertx.commons.Constants.UserProfileService;

public class UserProfileApiVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(UserProfileApiVerticle.class);

    private MongoClient mongoClient;
    private MongoAuthentication authProvider;
    private MongoUserUtil userUtil;

    private JsonObject mongoConfig() {
        return new JsonObject()
                .put("host", UserProfileService.MONGODB_HOST)
                .put("port", UserProfileService.MONGODB_PORT)
                .put("db_name", UserProfileService.MONGODB_DB_NAME)
                .put("username", UserProfileService.MONGODB_USERNAME)
                .put("password", UserProfileService.MONGODB_PASSWORD);
    }

    @Override
    public Completable rxStart() {
        this.mongoClient = MongoClient.createShared(vertx, this.mongoConfig());
        this.authProvider = MongoAuthentication.create(mongoClient, new MongoAuthenticationOptions());
        this.userUtil = MongoUserUtil.create(mongoClient,
                new MongoAuthenticationOptions(),
                new MongoAuthorizationOptions());

        Router router = Router.router(vertx);
        BodyHandler bodyHandler = BodyHandler.create();
        router.put().handler(bodyHandler);
        router.post().handler(bodyHandler);

        router.post(UserProfileService.HTTPApi.REGISTER)
                .handler(this::validateRegistration)
                .handler(this::register);
        router.get(UserProfileService.HTTPApi.USERNAME_PATH)
                .handler(this::fetchUser);
        router.put(UserProfileService.HTTPApi.USERNAME_PATH)
                .handler(this::updateUser);
        router.post(UserProfileService.HTTPApi.AUTHENTICATE)
                .handler(this::authenticate);
        router.get(UserProfileService.HTTPApi.OWNS_DEVICE_ID_PATH)
                .handler(this::whoOwns);

        return vertx.createHttpServer()
                .requestHandler(router)
                .listen(UserProfileService.HTTP_PORT)
                .doOnSuccess(httpServer -> LOG.info("{} started on {}",
                        this.getClass().getSimpleName(), UserProfileService.HTTP_PORT))
                .doOnError(err -> LOG.error("{} failed to start on {}",
                        this.getClass().getSimpleName(), UserProfileService.HTTP_PORT, err))
                .ignoreElement();
    }


    private void validateRegistration(RoutingContext rc) {
        JsonObject body = this.jsonBody(rc);
        if (this.anyRegistrationFieldIsMissing(body) || this.anyRegistrationFieldIsWrong(body)) {
            rc.fail(400);
        } else {
            rc.next();
        }
    }


    private boolean anyRegistrationFieldIsMissing(JsonObject body) {
        return !(body.containsKey("username") &&
                body.containsKey("password") &&
                body.containsKey("email") &&
                body.containsKey("city") &&
                body.containsKey("deviceId") &&
                body.containsKey("makePublic"));
    }

    private final Pattern validUsername = Pattern.compile("\\w[\\w+|-]*");
    private final Pattern validDeviceId = Pattern.compile("\\w[\\w+|-]*");

    // Email regexp from https://www.owasp.org/index.php/OWASP_Validation_Regex_Repository
    private final Pattern validEmail = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    private boolean anyRegistrationFieldIsWrong(JsonObject body) {
        return !validUsername.matcher(body.getString("username")).matches() ||
                !validEmail.matcher(body.getString("email")).matches() ||
                body.getString("password").trim().isEmpty() ||
                !validDeviceId.matcher(body.getString("deviceId")).matches();
    }

    private void register(RoutingContext rc) {
        JsonObject body = this.jsonBody(rc);
        String username = body.getString("username");
        String password = body.getString("password");
        JsonObject extraInfo = new JsonObject()
                .put("$set", new JsonObject()
                        .put("email", body.getString("email"))
                        .put("city", body.getString("city"))
                        .put("deviceId", body.getString("deviceId"))
                        .put("makePublic", body.getString("makePublic")));

        userUtil.rxCreateUser(username, password)
                .flatMapMaybe(docId -> insertExtraInto(extraInfo, docId))
                .ignoreElement()
                .subscribe(
                        () -> this.completeRegistration(rc),
                        err -> this.handleRegistrationError(rc, err)
                );
    }

    private Maybe<JsonObject> insertExtraInto(JsonObject extraInfo, String docId) {
        JsonObject query = new JsonObject().put("_id", docId);
        return mongoClient
                .rxFindOneAndUpdate(UserProfileService.MONGODB_COL_NAME, query, extraInfo)
                .onErrorResumeNext(err -> this.deleteInCompleteUser(query, err));
    }

    private void completeRegistration(RoutingContext rc) {
        rc.response().end();
    }

    private void handleRegistrationError(RoutingContext rc, Throwable err) {
        if (this.isIndexViolated(err)) {
            LOG.error("Registration failed", err);
            rc.fail(400);
        } else {
            this.fail500(rc, err);
        }
    }

    private Maybe<JsonObject> deleteInCompleteUser(JsonObject query, Throwable err) {
        if (this.isIndexViolated(err)) {
            return mongoClient.rxRemoveDocuments(UserProfileService.MONGODB_COL_NAME, query)
                    .flatMap(del -> Maybe.error(err));
        } else {
            return Maybe.error(err);
        }
    }

    private boolean isIndexViolated(Throwable err) {
        return err.getMessage().contains("E11000");
    }

    private void fetchUser(RoutingContext rc) {
        String username = rc.pathParam("username");
        JsonObject query = new JsonObject()
                .put("username", username);
        JsonObject fields = new JsonObject()
                .put("_id", 0)
                .put("username", 1)
                .put("email", 1)
                .put("deviceId", 1)
                .put("city", 1)
                .put("makePublic", 1);

        mongoClient.rxFindOne(UserProfileService.MONGODB_COL_NAME, query, fields)
                .toSingle()
                .subscribe(
                        data -> this.completeFetchRequest(rc, data),
                        err -> this.handleFetchError(rc, err)
                );
    }

    private void updateUser(RoutingContext rc) {
        String username = rc.pathParam("username");
        JsonObject body = this.jsonBody(rc);
        JsonObject query = new JsonObject().put("username", username);
        JsonObject updates = new JsonObject();
        if (body.containsKey("city")) {
            updates.put("city", body.getString("city"));
        }
        if (body.containsKey("email")) {
            updates.put("email", body.getString("email"));
        }
        if (body.containsKey("makePublic")) {
            updates.put("makePublic", body.getBoolean("makePublic"));
        }

        if (updates.isEmpty()) {
            rc.response().setStatusCode(200).end();
            return;
        }
        updates = new JsonObject().put("$set", updates);
        mongoClient.rxFindOneAndUpdate(UserProfileService.MONGODB_COL_NAME, query, updates)
                .ignoreElement()
                .subscribe(
                        () -> this.completeEmptySuccess(rc),
                        err -> this.handleUpdateError(rc, err)
                );
    }

    private void authenticate(RoutingContext rc) {
        authProvider.rxAuthenticate(new UsernamePasswordCredentials(this.jsonBody(rc)))
                .subscribe(data -> this.completeEmptySuccess(rc),
                        err -> this.handleAuthenticateError(rc, err));
    }

    private void handleAuthenticateError(RoutingContext rc, Throwable err) {
        LOG.error("Authentication failed", err);
        rc.response().setStatusCode(401).end();
    }

    private void whoOwns(RoutingContext rc) {
        String deviceId = rc.pathParam("deviceId");
        JsonObject query = new JsonObject()
                .put("deviceId", deviceId);
        JsonObject fields = new JsonObject()
                .put("_id", 0)
                .put("username", 1)
                .put("deviceId", 1);
        mongoClient.rxFindOne(UserProfileService.MONGODB_COL_NAME, query, fields)
                .toSingle()
                .subscribe(
                        data -> this.completeFetchRequest(rc, data),
                        err -> this.handleFetchError(rc, err));
    }

    // helpers
    private JsonObject jsonBody(RoutingContext rc) {
        if (rc.body() != null) {
            JsonObject body = rc.body().asJsonObject();
            if (body != null) {
                return body;
            }
        }
        return new JsonObject();
    }

    private void fail500(RoutingContext rc, Throwable err) {
        LOG.error("ERROR", err);
        rc.fail(500);
    }

    private void completeEmptySuccess(RoutingContext rc) {
        rc.response().setStatusCode(200).end();
    }

    private void completeFetchRequest(RoutingContext rc, JsonObject data) {
        rc.response()
                .putHeader("Content-Type", "application/json")
                .end(data.encode());
    }

    private void handleFetchError(RoutingContext rc, Throwable err) {
        if (err instanceof NoSuchElementException) { // ???
            rc.fail(404);
        } else {
            fail500(rc, err);
        }
    }

    private void handleUpdateError(RoutingContext rc, Throwable err) {
        this.fail500(rc, err);
    }

}