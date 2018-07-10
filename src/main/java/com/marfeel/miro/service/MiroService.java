package com.marfeel.miro.service;

import com.marfeel.miro.handler.MiroRequest;
import com.marfeel.miro.handler.MiroResponse;
import com.marfeel.miro.service.analyzer.MiroAnalyzer;
import com.marfeel.miro.service.decorator.MiroDecorator;
import io.vertx.core.Future;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Objects;

public class MiroService {

    private final FetchService fetchService;
    private final List<MiroAnalyzer> analyzerList;

    public MiroService(FetchService fetchService, List<MiroAnalyzer> analyzerList) {
        this.fetchService = Objects.requireNonNull(fetchService, "FetchService can not be null");
        this.analyzerList = Objects.requireNonNull(analyzerList, "MiroAnalyzers can not be null");
    }

    public Future<MiroResponse> readAndDecorateUrl(MiroRequest request) {
        return this.fetchService.fetchUrl(request).compose(this::handleResult);
    }

    private Future<MiroResponse> handleResult(MiroResponse result) {
        return result.getRedirect().map(redirect -> Future.succeededFuture(result))
                .orElseGet(() -> result.getHtml().map(html -> {
                                    Document[] docs = new Document[]{Jsoup.parse(html)};
                                    this.analyzerList.stream().forEach(analyzer -> {
                                        if (analyzer.applyDecorators(docs[0])) {
                                            analyzer.getDecorators().forEach(decorator -> {
                                                docs[0] = ((MiroDecorator) decorator).decorate(docs[0]);
                                            });
                                        }
                                    });
                                    return Future.succeededFuture(new MiroResponse(docs[0].html(), result.getHeaders().orElse(null), result.getStatusCode()));
                                }).orElseGet(() -> Future.failedFuture("MiroResponse does not have neither body nor response headers")));
    }
}
