package com.marfeel.miro;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class FiberLauncher {

    private static final Logger logger = LoggerFactory.getLogger(FiberLauncher.class);

    public static void main(String[] args) {

        Vertx.vertx().deployVerticle(FiberVerticle.class.getName(), new DeploymentOptions().setInstances(4), h -> {
            if (h.succeeded()) {
                logger.info("Success: {0}", h.result());
            } else {
                logger.error("Something went wrong: {0}", h.cause());
            }
        });
    }
}
