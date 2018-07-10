package com.marfeel.miro;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

import java.util.Objects;
import java.util.function.Consumer;

public class ApplicationVerticle extends AbstractVerticle {

    private final Vertx vertx;
    private final Consumer<Router> routes;
    private final Integer port;

    public ApplicationVerticle(Vertx vertx, Consumer<Router> routes, Integer port) {
        this.vertx = Objects.requireNonNull(vertx, "Vertx can not be null");
        this.routes = Objects.requireNonNull(routes, "Routes can not be null");
        this.port = Objects.requireNonNull(port, "Http Server port can not be null");
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);
        //router.route().handler(CorsHandler.create("*"));
        routes.accept(router);

        HttpServerOptions serverOptions = new HttpServerOptions();
        serverOptions.setMaxInitialLineLength(3 * HttpServerOptions.DEFAULT_MAX_INITIAL_LINE_LENGTH);
        serverOptions.setMaxChunkSize(3 * HttpServerOptions.DEFAULT_MAX_CHUNK_SIZE);
        serverOptions.setCompressionSupported(true);
        serverOptions.setCompressionLevel(4); // default is 6

        vertx.createHttpServer(serverOptions).requestHandler(router::accept).listen(this.port, listen -> {
            if (listen.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(listen.cause());
            }
        });
    }
}
