package com.marfeel.miro.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.net.URL;

public interface MiroHandler extends Handler<RoutingContext> {

    @Override
    default void handle(RoutingContext routingContext) {
        parseRequest(routingContext)
                .compose(r -> processRequest(routingContext, r))
                .setHandler(responseResult -> handleResult(responseResult, routingContext));
    }

    default Future<URL> parseRequest(RoutingContext context) {
        URL url;
        try {
            String qp = context.request().getParam("url");
            url = new URL(qp.startsWith("http") ? qp : "http://" + qp);
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
        return Future.succeededFuture(url);
    }

    Future<String> processRequest(RoutingContext context, URL url);

    // TODO hay que propagar los headers tal cual los recibimos, es algo que no hacemos
    default void handleResult(AsyncResult<String> responseResult, RoutingContext context) {
        if (responseResult.succeeded()) {
            context.response().setStatusCode(200)
                    //.putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(responseResult.result().length()))
                    //.setChunked(true)
                    .end(responseResult.result());
        } else {
            context.response().setStatusCode(500).end(responseResult.cause().getMessage());
        }
    }

}
