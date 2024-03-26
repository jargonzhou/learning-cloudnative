package com.spike.vertx.user;


import com.spike.vertx.commons.Constants;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserWebappVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(UserWebappVerticle.class);

    @Override
    public Completable rxStart() {
        Router router = Router.router(vertx);
        router.route().handler(StaticHandler.create("webroot/assets"));
        router.get("/*").handler(rc -> rc.reroute("/index.html"));
        return vertx.createHttpServer()
                .requestHandler(router)
                .rxListen(Constants.UserWebapp.HTTP_PORT)
                .doOnSuccess(httpServer -> LOG.info("{} started on {}",
                        this.getClass().getSimpleName(), Constants.UserWebapp.HTTP_PORT))
                .doOnError(err -> LOG.error("{} failed to start on {}",
                        this.getClass().getSimpleName(), Constants.UserWebapp.HTTP_PORT))
                .ignoreElement();
    }

}
