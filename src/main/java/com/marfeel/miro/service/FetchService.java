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
    private final long fetchTimeOut;

    public FetchService(HttpClient httpClient, long fetchTimeOut) {
        this.httpClient = Objects.requireNonNull(httpClient, "HttpClient can not be null");
        this.fetchTimeOut = fetchTimeOut <= 0 ? 1000 : fetchTimeOut;
    }

    public Future<MiroResponse> fetchUrl(MiroRequest miroRequest) {
        Future<MiroResponse> future = Future.future();

        HttpClientRequest request = httpClient.requestAbs(HttpMethod.GET, miroRequest.getURL().toString(),
                response -> response.bodyHandler(buffer -> future.complete(new MiroResponse(buffer.toString(), response.headers(), response.statusCode())))
        ).exceptionHandler(exception -> future.fail(exception)).setTimeout(fetchTimeOut).setFollowRedirects(false);

        miroRequest.getHeaders().names().forEach(header -> {
           if (!"Accept-Encoding".equalsIgnoreCase(header)) {
               request.putHeader(header, miroRequest.getHeaders().get(header));
           }
        });
        request.end();

        return future;
    }
}
