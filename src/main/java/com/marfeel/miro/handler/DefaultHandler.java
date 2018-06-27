/*
 * Copyright (c) 2018 by Marfeel Solutions (http://www.marfeel.com)
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Marfeel Solutions S.L and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Marfeel Solutions S.L and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Marfeel Solutions SL.
 */

package com.marfeel.miro.handler;

import com.marfeel.miro.service.MiroService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;

import java.net.URL;
import java.util.Objects;

public class DefaultHandler implements Handler<RoutingContext> {

    private final MiroService miroService;

    public DefaultHandler(MiroService miroService) {
        this.miroService = Objects.requireNonNull(miroService, "MiroService can not be null");
    }

    @Override
    public void handle(RoutingContext routingContext) {
        parseRequest(routingContext)
                .compose(r -> this.processRequest(routingContext, r))
                .compose(this::prepareResult)
                .setHandler(responseResult -> handleResult(responseResult, routingContext));
    }

    Future<URL> parseRequest(RoutingContext context) {
        URL url;
        try {
            String qp = context.request().getParam("url");
            url = new URL(qp.startsWith("http") ? qp : "http://" + qp);
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
        return Future.succeededFuture(url);
    }

    Future<String> processRequest(RoutingContext context, URL url) {
        return miroService.readAndDecorateUrl(url);
    }

    Future<String> prepareResult(String result) {
        return Future.succeededFuture(result);
    }

    // TODO hay que propagar los headers tal cual los recibimos, es algo que no hacemos

    void handleResult(AsyncResult<String> responseResult, RoutingContext context) {
        if (responseResult.succeeded()) {
            context.response().setStatusCode(200)
                    //.putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(responseResult.result().length()))
                    //.setChunked(true)
                    .end(responseResult.result());
        } else {
            context.response().setStatusCode(500)
                    .putHeader(HttpHeaders.CONTENT_LENGTH, responseResult.result() != null ?
                                String.valueOf(responseResult.result().length()) : String.valueOf(16))
                    .write(responseResult.cause().getMessage()).end();
        }
    }
}
