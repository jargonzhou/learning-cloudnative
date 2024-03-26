package com.spike.vertx.eventstats.publicapi;

import com.google.common.collect.Sets;
import com.spike.vertx.commons.JwtKeys;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.client.HttpResponse;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.ext.web.client.predicate.ResponsePredicate;
import io.vertx.rxjava3.ext.web.codec.BodyCodec;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;
import io.vertx.rxjava3.ext.web.handler.CorsHandler;
import io.vertx.rxjava3.ext.web.handler.JWTAuthHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.spike.vertx.commons.Constants.*;

public class PublicApiVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(PublicApiVerticle.class);

    private WebClient webClient;
    private JWTAuth jwtAuth;
    private String publicKey;
    private String privateKey;

    @Override
    public Completable rxStart() {
        LOG.info("Current working dir: {}", System.getProperty("user.dir"));
        try {
            publicKey = JwtKeys.publicKey("../keys");
            privateKey = JwtKeys.privateKey("../keys");
        } catch (IOException e) {
            LOG.error("JWT ERROR", e);
            return Completable.error(e);
        }


        this.webClient = WebClient.create(vertx);

        Router router = Router.router(vertx);
        this.setupRouter(router);
        vertx.createHttpServer()
                .requestHandler(router)
                .rxListen(PublicApi.HTTP_PORT)
                .subscribe(
                        httpServer -> LOG.info("{} started on {}",
                                this.getClass().getSimpleName(), PublicApi.HTTP_PORT),
                        err -> LOG.error("{} failed to start on {}",
                                this.getClass().getSimpleName(), PublicApi.HTTP_PORT));

        return Completable.complete();
    }

    private void setupRouter(Router router) {
        router.route().handler(this.corsHandler());

        BodyHandler bodyHandler = BodyHandler.create();
        router.post().handler(bodyHandler);
        router.put().handler(bodyHandler);

        JWTAuthOptions jwtAuthOptions = new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("RS256")
                        .setBuffer(publicKey))
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("RS256")
                        .setBuffer(privateKey));
        this.jwtAuth = JWTAuth.create(vertx, jwtAuthOptions);
        JWTAuthHandler jwtAuthHandler = JWTAuthHandler.create(jwtAuth); // authentication handler

        // User profile service
        router.post(PublicApi.HTTPApi.REGISTER)
                .handler(this::register);
        router.post(PublicApi.HTTPApi.TOKEN)
                .handler(this::token);

        router.get(PublicApi.HTTPApi.USER_PATH)
                .handler(jwtAuthHandler) // chaining the handlers
                .handler(this::checkUser)
                .handler(this::fetchUser);
        router.put(PublicApi.HTTPApi.USER_PATH)
                .handler(jwtAuthHandler)
                .handler(this::checkUser)
                .handler(this::updateUser);

        router.get(PublicApi.HTTPApi.USER_TOTAL_PATH)
                .handler(jwtAuthHandler)
                .handler(this::checkUser)
                .handler(this::totalSteps);
        router.get(PublicApi.HTTPApi.USER_TOTAL_YEAR_MONTH_PATH)
                .handler(jwtAuthHandler)
                .handler(this::checkUser)
                .handler(this::monthlySteps);
        router.get(PublicApi.HTTPApi.USER_TOTAL_YEAR_MONTH_DAY_PATH)
                .handler(jwtAuthHandler)
                .handler(this::checkUser)
                .handler(this::dailySteps);
    }

    private CorsHandler corsHandler() {
        return CorsHandler.create("*")
                .allowedHeaders(Sets.newHashSet(
                        "x-requested-with",
                        "Access-Control-Allow-Origin",
                        "origin",
                        "Content-Type",
                        "accept",
                        "Authorization"))
                .allowedMethods(Sets.newHashSet(
                        HttpMethod.GET,
                        HttpMethod.POST,
                        HttpMethod.OPTIONS,
                        HttpMethod.PUT));
    }

    // => User profile service

    private void register(RoutingContext rc) {
        webClient.post(UserProfileService.HTTP_PORT,
                        UserProfileService.HOST,
                        UserProfileService.HTTPApi.REGISTER)
                .putHeader("Content-Type", "application/json")
//                .expect(ResponsePredicate.SC_SUCCESS) // may fail
                .rxSendJsonObject(rc.body().asJsonObject())
                .subscribe(
                        res -> this.sendStatusCode(rc, res.statusCode()),
                        err -> this.sendBadGateway(rc, err));
    }


    private void token(RoutingContext rc) {
        JsonObject payload = rc.body().asJsonObject();
        String username = payload.getString("username");
        webClient.post(UserProfileService.HTTP_PORT,
                        UserProfileService.HOST,
                        UserProfileService.HTTPApi.AUTHENTICATE)
                .expect(ResponsePredicate.SC_SUCCESS)
                .rxSendJsonObject(payload)
                .flatMap(resp -> fetchUserDetails(username))
                .map(resp -> resp.body().getString("deviceId"))
                .map(deviceId -> this.makeJwtToken(username, deviceId))
                .subscribe(
                        token -> sendToken(rc, token),
                        err -> handleAuthError(rc, err));
    }

    private Single<HttpResponse<JsonObject>> fetchUserDetails(String username) {
        return webClient.get(UserProfileService.HTTP_PORT,
                        UserProfileService.HOST,
                        "/" + username)
                .expect(ResponsePredicate.SC_OK)
                .as(BodyCodec.jsonObject())
                .rxSend();
    }

    private void checkUser(RoutingContext rc) {
        String subject = rc.user().principal().getString("sub");
        if (!rc.pathParam("username").equals(subject)) {
            this.sendStatusCode(rc, 403);
        } else {
            rc.next(); // pass
        }
    }


    private void fetchUser(RoutingContext rc) {
        String username = rc.pathParam("username");
        webClient.get(UserProfileService.HTTP_PORT,
                        UserProfileService.HOST,
                        "/" + username)
                .as(BodyCodec.jsonObject())
                .rxSend()
                .subscribe(
                        resp -> forwardJsonOrStatusCode(rc, resp),
                        err -> sendBadGateway(rc, err));
    }


    private void updateUser(RoutingContext rc) {
        String username = rc.pathParam("username");
        webClient.put(UserProfileService.HTTP_PORT,
                        UserProfileService.HOST,
                        "/" + username)
                .putHeader("Content-Type", "application/json")
                .expect(ResponsePredicate.SC_SUCCESS)
                .rxSendJsonObject(rc.body().asJsonObject())
                .subscribe(
                        resp -> rc.response().end(),
                        err -> sendBadGateway(rc, err));
    }

    // => Activity service
    private void totalSteps(RoutingContext rc) {
        String deviceId = rc.user().principal().getString("deviceId");

        this.callActivityServiceSteps(rc,
                String.format(ActivityService.HTTPApi.DEVICE_TOTAL, deviceId));
    }

    private void monthlySteps(RoutingContext rc) {
        String deviceId = rc.user().principal().getString("deviceId");
        String year = rc.pathParam("year");
        String month = rc.pathParam("month");
        this.callActivityServiceSteps(rc,
                String.format(ActivityService.HTTPApi.DEVICE_MONTHLY,
                        deviceId, year, month));
    }

    private void dailySteps(RoutingContext rc) {
        String deviceId = rc.user().principal().getString("deviceId");
        String year = rc.pathParam("year");
        String month = rc.pathParam("month");
        String day = rc.pathParam("day");
        this.callActivityServiceSteps(rc,
                String.format(ActivityService.HTTPApi.DEVICE_DAILY,
                        deviceId, year, month, day));
    }

    private void callActivityServiceSteps(RoutingContext rc, String requestURI) {
        webClient.get(ActivityService.HTTP_PORT,
                        ActivityService.HOST,
                        requestURI)
                .as(BodyCodec.jsonObject())
                .rxSend()
                .subscribe(
                        resp -> forwardJsonOrStatusCode(rc, resp),
                        err -> sendBadGateway(rc, err));
    }

    // helpers

    /**
     * Generate JWT token on claim `deviceId`, subject `username`.
     */
    private String makeJwtToken(String username, String deviceId) {
        JsonObject claims = new JsonObject()
                .put("deviceId", deviceId);
        JWTOptions jwtOptions = new JWTOptions()
                .setAlgorithm("RS256")
                .setExpiresInMinutes(10_080) // 7 days
                .setIssuer("public-api")
                .setSubject(username);
        return jwtAuth.generateToken(claims, jwtOptions);
    }

    private void sendStatusCode(RoutingContext rc, int statusCode) {
        rc.response().setStatusCode(statusCode).end();
    }

    private void sendBadGateway(RoutingContext rc, Throwable err) {
        LOG.error("Gateway ERROR", err);
        rc.fail(502); // reject
    }

    private void sendToken(RoutingContext rc, String token) {
        rc.response()
                .putHeader("Content-Type", "application/jwt")
                .end(token);
    }

    private void handleAuthError(RoutingContext rc, Throwable err) {
        LOG.error("Auth ERROR", err);
        rc.fail(401); // reject
    }

    private void forwardJsonOrStatusCode(RoutingContext rc, HttpResponse<JsonObject> resp) {
        if (resp.statusCode() != 200) {
            this.sendStatusCode(rc, resp.statusCode());
        } else {
            rc.response()
                    .putHeader("Content-Type", "application/json")
                    .end(resp.body().encode());
        }
    }
}
