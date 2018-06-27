package com.marfeel.miro.configuration;

import com.marfeel.miro.handler.AmpHandler;
import com.marfeel.miro.handler.DefaultHandler;
import com.marfeel.miro.handler.HealthHandler;
import com.marfeel.miro.service.MiroService;
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

    public static final String AMP_PATH = "/amp";
    public static final String HEALTH_PATH = "/health";
    public static final String DEFAULT_PATH = "/";

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Consumer<Router> addMarfeelRoutes(final MiroService miroService) {
        return router -> {
            Assert.notNull(router, "Router can not be null");
            Assert.notNull(miroService, "MiroService can not be null");

            router.route().handler(TimeoutHandler.create(10000));
            router.get(AMP_PATH).handler(ampHandler(miroService));
            router.get(HEALTH_PATH).handler(new HealthHandler());
            router.get(DEFAULT_PATH).handler((defaultHandler(miroService)));
        };
    }

    private AmpHandler ampHandler(MiroService miroService) {
        return new AmpHandler(miroService);
    }

    private DefaultHandler defaultHandler(MiroService miroService) {
        return new DefaultHandler(miroService);
    }

}
