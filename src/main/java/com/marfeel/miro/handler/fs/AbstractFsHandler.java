package com.marfeel.miro.handler.fs;

import com.marfeel.miro.handler.MiroHandler;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class AbstractFsHandler implements MiroHandler {

    private final Vertx vertx;
    private final Path path;

    protected  AbstractFsHandler(Vertx vertx, String fileName) {
        this.vertx = Objects.requireNonNull(vertx, "Vertx can not be null");
        this.path = Paths.get(this.getClass().getResource("/"+fileName).getPath());
    }

    @Override
    public Future<String> processRequest(RoutingContext context, URL url) {
        Future future = Future.future();

        vertx.fileSystem().readFile(path.toString(), ar -> {
            if (ar.succeeded()) {
                future.complete(ar.result().toString());
            } else {
                future.fail(ar.cause());
            }
        });

        return future;
    }
}
