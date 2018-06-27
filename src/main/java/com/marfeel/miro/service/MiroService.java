package com.marfeel.miro.service;

import com.marfeel.miro.service.analyzer.MiroAnalyzer;
import com.marfeel.miro.service.decorator.MiroDecorator;
import io.vertx.core.Future;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.List;
import java.util.Objects;

public class MiroService {

    private final FetchService fetchService;
    private final List<MiroAnalyzer> analyzerList;

    public MiroService(FetchService fetchService, List<MiroAnalyzer> analyzerList) {
        this.fetchService = Objects.requireNonNull(fetchService, "FetchService can not be null");
        this.analyzerList = Objects.requireNonNull(analyzerList, "MiroAnalyzers can not be null");
    }

    public Future<String> readAndDecorateUrl(URL url) {
        return this.fetchService.fetchUrl(url).compose(this::handleResult);
    }

    private Future<String> handleResult(String result) {
        //sync code, maybe we should execute it in a worker thread, this should be improved a lot!
        Document[] docs = new Document[]{Jsoup.parse(result)};
        this.analyzerList.stream().forEach(analyzer -> {
            if (analyzer.applyDecorators(docs[0])) {
                analyzer.getDecorators().forEach(decorator -> {
                    docs[0] = ((MiroDecorator) decorator).decorate(docs[0]);
                });
            }
        });

        return Future.succeededFuture(docs[0].html());
    }
}
