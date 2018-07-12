package com.marfeel.miro.service;

import com.marfeel.miro.handler.MiroRequest;
import com.marfeel.miro.handler.MiroResponse;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FetchSyncService implements IFetchService {

    private final Logger L = LoggerFactory.getLogger(FetchSyncService.class);

    private final Vertx vertx;
    private final OkHttpClient httpClient;
    private final long fetchTimeOut;

    public FetchSyncService(Vertx vertx, long fetchTimeOut) {
        this.vertx = Objects.requireNonNull(vertx, "Vertx can not be null");
        this.fetchTimeOut = fetchTimeOut <= 0 ? 1000 : fetchTimeOut;
        this.httpClient = new OkHttpClient.Builder().followRedirects(false).followSslRedirects(false).followRedirects(false).
                                readTimeout(this.fetchTimeOut, TimeUnit.MILLISECONDS).build();
    }

    @Override
    public Future<MiroResponse> fetchUrl(MiroRequest miroRequest) {
        Future outerFuture = Future.future();

        vertx.executeBlocking(future -> {
            Request request = new Request.Builder().url(miroRequest.getURL().toString()).build();
            try {
                future.complete(this.httpClient.newCall(request).execute().body().string());
            } catch (IOException e) {
                future.fail(e);
            }
        }, false, res -> {
            if (res.succeeded()) {
                outerFuture.complete(new MiroResponse((String)res.result(), null, 200));
            } else {
                outerFuture.fail(res.cause());
            }
        });

        return outerFuture;
    }
}
