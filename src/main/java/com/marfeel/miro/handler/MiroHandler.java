package com.marfeel.miro.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public interface MiroHandler extends Handler<RoutingContext> {

    Logger L = LoggerFactory.getLogger(MiroHandler.class);

    String UNEXPECTED_ERROR_HTML = "<html>\n" +
            "<body>\n" +
            "<h1>Something went wrong! </h1>\n" +
            "<h2>Our Engineers are on it</h2>\n" +
            "<a href=\"/\">Go Home</a>\n" +
            "</body>\n" +
            "</html>";

    @Override
    default void handle(RoutingContext routingContext) {
        parseRequest(routingContext)
                .compose(r -> processRequest(routingContext, r))
                .setHandler(responseResult -> handleResult(responseResult, routingContext));
    }

    default Future<MiroRequest> parseRequest(RoutingContext context) {
        try {
            return buildURL(context).map(url -> new MiroRequest(url, context.request().headers()));
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
    }

    Future<MiroResponse> processRequest(RoutingContext context, MiroRequest url);

    // TODO hay que propagar los headers tal cual los recibimos, es algo que no hacemos
    default void handleResult(AsyncResult<MiroResponse> responseResult, RoutingContext context) {
        if (responseResult.succeeded()) {
            final MiroResponse response = responseResult.result();

            context.response().setStatusCode(response.getStatusCode());
            response.getHeaders().map(headers -> context.response().headers().addAll(headers)).orElse(null);

            if (response.getStatusCode() != 301 && response.getStatusCode() != 302) {
                context.response().end(response.getHtml().orElse(UNEXPECTED_ERROR_HTML));
            } else {
                context.response().end();
            }
        } else {
            context.response().setStatusCode(500).end(responseResult.cause().getMessage());
        }
    }

    //
    // following ones should be private, JDK9 or newer
    //

    default Future<URL> buildURL(RoutingContext context) throws MalformedURLException {
        URL url;
        // query param takes precedence over anything else
        String qp = context.request().getParam("url");
        if (qp != null) {
            url = new URL(qp.startsWith("http") ? qp : "http://" + qp);
        } else {
            String protocol = context.request().getHeader("X-Forwarded-Proto");
            String forwardedhost = context.request().getHeader("X-Forwarded-Host");
            String host = context.request().getHeader("Host");
            String uri = context.request().absoluteURI();

            if (L.isInfoEnabled()) {
                L.info("forwarded proto is {}", protocol);
                L.info("forwarded host is {}", forwardedhost);
                L.info("host is {}", host);
                L.info("absoluteUri is {}", uri);
            }

            url = new URL(protocol + forwardedhost + uri);
        }
        return Future.succeededFuture(url);
    }

}