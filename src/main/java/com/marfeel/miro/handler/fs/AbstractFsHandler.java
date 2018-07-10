package com.marfeel.miro.handler.fs;

import com.marfeel.miro.handler.MiroHandler;
import com.marfeel.miro.handler.MiroRequest;
import com.marfeel.miro.handler.MiroResponse;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

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
    public Future<MiroResponse> processRequest(RoutingContext context, MiroRequest request) {
        Future future = Future.future();

        vertx.fileSystem().readFile(path.toString(), ar -> {
            if (ar.succeeded()) {
                future.complete(new MiroResponse(ar.result().toString(), null, 200));
            } else {
                future.fail(ar.cause());
            }
        });

        return future;
    }
}
