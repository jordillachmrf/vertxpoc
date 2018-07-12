package com.marfeel.miro.configuration;

import com.marfeel.miro.service.FetchService;
import com.marfeel.miro.service.FetchSyncService;
import com.marfeel.miro.service.IFetchService;
import com.marfeel.miro.service.MiroService;
import com.marfeel.miro.service.analyzer.MiroAnalyzer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Configuration
public class ServiceConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MiroService miroService(IFetchService fetchService, List<MiroAnalyzer> analyzerList) {
        return new MiroService(fetchService, analyzerList);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public IFetchService fetchService(HttpClient httpClient, Vertx vertx) {
        return new FetchService(httpClient, 15000);
        //return new FetchSyncService(vertx, 15000);
    }
}
