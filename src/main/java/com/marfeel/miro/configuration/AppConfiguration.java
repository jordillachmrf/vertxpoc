package com.marfeel.miro.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.marfeel.miro.ApplicationVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.util.function.Consumer;

@Configuration
public class AppConfiguration {

    @PostConstruct
    public void initJson() {
        Json.mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModules(new AfterburnerModule());
    }

    @Bean
    Vertx vertx(@Value("${vertx.worker-pool-size}") Integer workerPoolSize) {
        return Vertx.vertx(new VertxOptions().setWorkerPoolSize(workerPoolSize));
    }

    @Bean
    SpringVerticleFactory springVerticleFactory() {
        return new SpringVerticleFactory();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Spring verticles needs SCOPE_PROTOTYPE
    public ApplicationVerticle applicationVerticle(Vertx vertx, Consumer<Router> routes, @Value("${http.port}") int port)
            throws Exception {
        return new ApplicationVerticle(vertx, routes, port);
    }

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    HttpClient httpClient(
            @Value("${http-client.max-pool-size}") int maxPoolSize,
            @Value("${http-client.connect-timeout-ms}") int connectTimeoutMs,
            Vertx vertx) {
        final HttpClientOptions options = new HttpClientOptions().setMaxPoolSize(maxPoolSize).setConnectTimeout(connectTimeoutMs);
        options.setHttp2MaxPoolSize(maxPoolSize);
        options.setKeepAlive(true);
        options.setVerifyHost(false);
        options.setTryUseCompression(true);
        options.setMaxRedirects(8);
        //options.setProtocolVersion(HttpVersion.HTTP_2);
        /*options.setHttp2ClearTextUpgrade(true).
                setSsl(true).
                setUseAlpn(true).
                setProtocolVersion(HttpVersion.HTTP_2).
                setTrustAll(true);*/
        return vertx.createHttpClient(options);
    }
}
