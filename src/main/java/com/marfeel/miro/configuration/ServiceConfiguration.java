package com.marfeel.miro.configuration;

import com.marfeel.miro.service.FetchService;
import com.marfeel.miro.service.MiroService;
import com.marfeel.miro.service.analyzer.MiroAnalyzer;
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
    public MiroService miroService(FetchService fetchService, List<MiroAnalyzer> analyzerList) {
        return new MiroService(fetchService, analyzerList);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FetchService fetchService(HttpClient httpClient) {
        return new FetchService(httpClient);
    }
}
