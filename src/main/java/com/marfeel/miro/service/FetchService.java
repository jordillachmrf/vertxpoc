package com.marfeel.miro.service;

import com.marfeel.miro.handler.MiroRequest;
import com.marfeel.miro.handler.MiroResponse;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class FetchService {

    private final Logger L = LoggerFactory.getLogger(FetchService.class);

    private final HttpClient httpClient;

    public FetchService(HttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient, "HttpClient can not be null");
    }

    public Future<MiroResponse> fetchUrl(MiroRequest miroRequest) {
        Future<MiroResponse> future = Future.future();

        HttpClientRequest request = httpClient.requestAbs(HttpMethod.GET, miroRequest.getURL().toString(), response -> {
            response.bodyHandler(buffer -> future.complete(new MiroResponse(buffer.toString(), response.headers(), response.statusCode())))
                                .exceptionHandler(exception -> future.fail(exception));
        }).setFollowRedirects(false);

        miroRequest.getHeaders().names().forEach(header -> {
           if (!"Host".equalsIgnoreCase(header) && !"Accept-Encoding".equalsIgnoreCase(header)) {
               request.putHeader(header, miroRequest.getHeaders().get(header));
           }
        });
        request.end();

        return future;
    }
}
