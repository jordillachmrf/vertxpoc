package com.marfeel.miro.configuration;

import com.marfeel.miro.handler.DefaultHandler;
import com.marfeel.miro.handler.HealthHandler;
import com.marfeel.miro.handler.fetch.FetchHandler;
import com.marfeel.miro.handler.fs.BigFsHandler;
import com.marfeel.miro.handler.fs.JsoupBigFsHandler;
import com.marfeel.miro.handler.fs.JsoupSmallFsHandler;
import com.marfeel.miro.handler.fs.SmallFsHandler;
import com.marfeel.miro.service.FetchService;
import com.marfeel.miro.service.MiroService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.TimeoutHandler;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;

import java.util.function.Consumer;

@Configuration
public class RoutingConfiguration {

    public static final String FETCH_PATH = "/fetch";
    public static final String FSS_PATH = "/fssmall";
    public static final String FSB_PATH = "/fsbig";
    public static final String JSS_PATH = "/jssmall";
    public static final String JSB_PATH = "/jsbig";

    public static final String HEALTH_PATH = "/health";
    public static final String DEFAULT_PATH = "/";

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Consumer<Router> addMarfeelRoutes(final Vertx vertx, final MiroService miroService, final FetchService fetchService) {
        return router -> {
            Assert.notNull(router, "Router can not be null");
            Assert.notNull(miroService, "MiroService can not be null");
            Assert.notNull(fetchService, "FetchService can not be null");
            Assert.notNull(vertx, "Vertx can not be null");

            router.route().handler(TimeoutHandler.create(10000));
            router.get(FETCH_PATH).handler(fetchHandler(fetchService));
            router.get(FSS_PATH).handler(smallFsHandler(vertx));
            router.get(FSB_PATH).handler(bigFsHandler(vertx));
            router.get(JSS_PATH).handler(jsoupSmallFsHandler(vertx));
            router.get(JSB_PATH).handler(jsoupBigFsHandler(vertx));
            router.get(HEALTH_PATH).handler(new HealthHandler());
            router.get(DEFAULT_PATH).handler((defaultHandler(miroService)));
        };
    }

    private FetchHandler fetchHandler(FetchService fetchService) {
        return new FetchHandler(fetchService);
    }

    private BigFsHandler bigFsHandler(Vertx vertx) { return new BigFsHandler(vertx);}

    private SmallFsHandler smallFsHandler(Vertx vertx) { return new SmallFsHandler(vertx);}

    private JsoupBigFsHandler jsoupBigFsHandler(Vertx vertx) { return new JsoupBigFsHandler(vertx);}

    private JsoupSmallFsHandler jsoupSmallFsHandler(Vertx vertx) { return new JsoupSmallFsHandler(vertx);}

    private DefaultHandler defaultHandler(MiroService miroService) {
        return new DefaultHandler(miroService);
    }

}
