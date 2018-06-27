package com.marfeel.miro;

import com.marfeel.miro.configuration.SpringVerticleFactory;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private final SpringVerticleFactory verticleFactory;
    private final Vertx vertx;
    private final int verticleInstances;
    private final long verticleDeployTimeout;

    @Autowired
    public Application(SpringVerticleFactory verticleFactory, Vertx vertx,
                       @Value("${vertx.verticle.instances}") int verticleInstances,
                       @Value("${vertx.verticle.deploy-timeout-ms}") long verticleDeployTimeout) {
        this.verticleFactory = verticleFactory;
        this.vertx = vertx;
        this.verticleInstances = verticleInstances;
        this.verticleDeployTimeout = verticleDeployTimeout;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener
    public void deployVerticle(ApplicationReadyEvent event) {
        vertx.registerVerticleFactory(verticleFactory);

        final CountDownLatch deployLatch = new CountDownLatch(verticleInstances);
        final String verticleName = verticleFactory.prefix() + ":" + ApplicationVerticle.class.getName();
        final AtomicBoolean failed = new AtomicBoolean(false);

        logger.info("Started deploying verticle. The number of verticles to be used  = {0}", verticleInstances);

        IntStream.rangeClosed(1, verticleInstances).forEach(instance ->
                deployVerticle(deployLatch, failed, verticleName, instance));

        try {
            if (!deployLatch.await(verticleDeployTimeout, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Failed due to timeout while waiting for verticle deployments");
            } else if (failed.get()) {
                throw new RuntimeException("Failed while deploying verticles");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void deployVerticle(CountDownLatch deployLatch, AtomicBoolean failed,
                                String verticle, int verticleInstance) {
        vertx.deployVerticle(verticle, ar -> {
            if (ar.failed()) {
                logger.error("{0} # {1} - Failed to deploy", ar.cause(), verticle, verticleInstance);
                failed.compareAndSet(false, true);
            } else if (ar.succeeded()) {
                logger.info("{0} # {1} - Succeeded to deploy", verticle, verticleInstance);
            }
            deployLatch.countDown();
        });
    }
}
