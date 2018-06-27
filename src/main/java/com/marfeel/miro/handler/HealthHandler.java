/*
 * Copyright (c) 2018 by Marfeel Solutions (http://www.marfeel.com)
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Marfeel Solutions S.L and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Marfeel Solutions S.L and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Marfeel Solutions SL.
 */

package com.marfeel.miro.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class HealthHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        routingContext.response().setStatusCode(200).end("{\"active\": true}");
    }
}
