package com.marfeel.miro.service;

import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Objects;

public class FetchService {

    private final Logger L = LoggerFactory.getLogger(FetchService.class);

    private final HttpClient httpClient;

    public FetchService(HttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient, "HttpClient can not be null");
    }

    public Future<String> fetchUrl(URL url) {
        Future<String> future = Future.future();

        httpClient.requestAbs(HttpMethod.GET, url.toString(), response -> {
            response.bodyHandler(buffer -> future.complete(buffer.toString()))
                    .exceptionHandler(exception -> future.fail(exception));
        }).end();

        return future;
    }
}
