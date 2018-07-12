package com.marfeel.miro;

import co.paralleluniverse.fibers.Suspendable;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.sync.SyncVerticle;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

import static io.vertx.ext.sync.Sync.awaitResult;
import static io.vertx.ext.sync.Sync.fiberHandler;

public class FiberVerticle extends SyncVerticle {

    private Integer port = 8080;
    private WebClient webClient;

    @Suspendable
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);

        HttpServerOptions serverOptions = new HttpServerOptions();
        serverOptions.setMaxInitialLineLength(3 * HttpServerOptions.DEFAULT_MAX_INITIAL_LINE_LENGTH);
        serverOptions.setMaxChunkSize(3 * HttpServerOptions.DEFAULT_MAX_CHUNK_SIZE);
        //serverOptions.setCompressionSupported(true);
        //serverOptions.setCompressionLevel(4); // default is 6

        webClient = WebClient.create(vertx, new WebClientOptions().setFollowRedirects(false).setMaxPoolSize(8196).setHttp2MaxPoolSize(8196).setKeepAlive(true).setVerifyHost(false)
                .setTryUseCompression(true).setHttp2ClearTextUpgrade(true).setSsl(true).setTrustAll(true));

        vertx.createHttpServer(serverOptions).requestHandler(fiberHandler(this::getWebContent)).listen(this.port, "localhost");
    }

    @Suspendable
    void getWebContent(HttpServerRequest request) {
        final HttpResponse<Buffer> response = awaitResult(h -> webClient.getAbs(request.getParam("url")).send(h));
        request.response().putHeader(HttpHeaderNames.CONTENT_TYPE, "text/html").end(response.bodyAsString("UTF-8"));
    }
}
